/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.vmt.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.falcon.api.serialize.JSONMapper;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.FileUploadResult;
import net.duckling.vmt.domain.HttpPost;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.email.Email;
import net.duckling.vmt.domain.email.EmailDetail;
import net.duckling.vmt.domain.email.EmailFile;
import net.duckling.vmt.domain.email.EmailGetter;
import net.duckling.vmt.domain.email.EmailGroup;
import net.duckling.vmt.domain.email.EmailSendablePeople;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IClbService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.ISendEmailService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.impl.FileSaverBridge;
import net.duckling.vmt.service.thread.BatchSendEmailJob;
import net.duckling.vmt.service.thread.JobThread;
import net.duckling.vmt.web.helper.JSONHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/enhanced/email")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class EnhancedEmailController {
	private static final Logger LOG=Logger.getLogger(EnhancedEmailController.class);
	@Autowired
	private IOrgService orgService;
	
	@Autowired
	private IGroupService groupService;
	
	@Autowired
	private IClbService clbService;
	
	@Autowired
	private ISendEmailService sendEmailService;
	@Autowired
	private IUserService userService;
	
	@Autowired
	private VmtConfig vmtConfig;
	
	public LdapOrg getMainOrg(VmtSessionUser user){
			List<LdapOrg> orgs=orgService.getAdminOrgs(user.getUserInfo().getUmtId());
			if(CommonUtils.isNull(orgs)){
				return null;
			}
			String domain=user.getCoreMailDomain();
			if(CommonUtils.isNull(domain)){
				return null;
			}
			for(LdapOrg org:orgs){
				if(org.isMainDomain(domain)){
					return org;
				}
			}
			return null;
	}
	public boolean isCanUseEmail(VmtSessionUser user){
		String domain=user.getCoreMailDomain();
		if(domain==null){
			return false;
		}
		return sendEmailService.isMember(domain,user.getUserInfo().getUmtId())||isAdminPeople(user);
	}
	public boolean isAdminPeople(VmtSessionUser user){
		return getMainOrg(user)!=null;
	}
	@RequestMapping("removeGroup")
	@ResponseBody
	public JsonResult removeGroup(@RequestParam("groupId")int groupId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(!isCanUseEmail(user)){
			return new JsonResult("权限不足");
		}
		EmailGroup eg=sendEmailService.getGroupById(groupId);
		if(eg==null){
			return new JsonResult("组未找到");
		}
		if(!eg.getGroupMasterUmtId().equals(user.getUserInfo().getUmtId())){
			return new JsonResult("该组并不是您的历史发件人组");
		}
		sendEmailService.removeGroup(groupId);
		JsonResult jr=new JsonResult();
		return jr;
	}
	@RequestMapping("renameGroup")
	@ResponseBody
	public JsonResult renameGroup(@RequestParam("groupId")int groupId,@RequestParam("name")String name,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(!isCanUseEmail(user)){
			return new JsonResult("权限不足");
		}
		EmailGroup eg=sendEmailService.getGroupById(groupId);
		if(eg==null){
			return new JsonResult("组未找到");
		}
		if(!eg.getGroupMasterUmtId().equals(user.getUserInfo().getUmtId())){
			return new JsonResult("该组并不是您的历史发件人组");
		}
		sendEmailService.updateGroupName(groupId,name);
		JsonResult jr=new JsonResult();
		jr.setData(name);
		return jr;
	}
	
	@RequestMapping("getGroupMember")
	@ResponseBody
	public JsonResult get(@RequestParam("groupId")int groupId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(!isCanUseEmail(user)){
			return new JsonResult("权限不足");
		}
		EmailGroup eg=sendEmailService.getGroupById(groupId);
		if(eg==null){
			return new JsonResult("组未找到");
		}
		if(!eg.getGroupMasterUmtId().equals(user.getUserInfo().getUmtId())){
			return new JsonResult("该组并不是您的历史发件人组");
		}
		List<EmailGetter> egs=sendEmailService.getGetterByGroupId(groupId);
		JsonResult jr=new JsonResult();
		jr.setData(egs);
		return jr;
		
	}
	private ModelAndView getModelAndView(String defaultView,VmtSessionUser user){
		ModelAndView mv=new ModelAndView(defaultView);
		LdapOrg mainOrg=getMainOrg(user);
		if(mainOrg==null&&!sendEmailService.isMember(user.getCoreMailDomain(), user.getUserInfo().getUmtId())){
			return new ModelAndView("user/no_emails_msg");
		}
		mv.addObject("isAdmin",mainOrg!=null);
		return mv;
	}
	
	@RequestMapping(params="func=getters")
	public ModelAndView getters(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		ModelAndView mv=getModelAndView("user/email_group",user);
		mv.addObject("groups",sendEmailService.getGroupByUmtId(user.getUserInfo().getUmtId()));
		return mv;
	}
	@RequestMapping(params="func=manage")
	public ModelAndView manage(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		ModelAndView mv=getModelAndView("user/email_manage",user);
		if(Boolean.getBoolean(mv.getModel().get("isAdmin").toString())){
			return new ModelAndView("user/no_emails_msg");
		}
		return extractAdmin(mv,user);
	}
	private ModelAndView extractAdmin(ModelAndView mv,VmtSessionUser user){
		LdapOrg org=getMainOrg(user);
		if(org==null){
			return null;
		}
		mv.addObject("mainOrg",org);
		List<LdapUser> lUsers=userService.searchUsersByUmtId(org.getDn(), org.getAdmins());
		List<EmailSendablePeople> people=new ArrayList<EmailSendablePeople>();
		for(LdapUser lu:lUsers){
			EmailSendablePeople sep=new EmailSendablePeople();
			sep.setCstnetId(lu.getCstnetId());
			sep.setUmtId(lu.getUmtId());
			sep.setUserName(lu.getName());
			people.add(sep);
		}
		mv.addObject("admins",people);
		List<EmailSendablePeople> member=sendEmailService.getSenableByDN(org.getDn());
		mv.addObject("members",member);
		return mv;
	}
	@RequestMapping("/download/{fileId}")
	public void downloadView(@PathVariable("fileId")int fileId,HttpServletRequest request,HttpServletResponse response,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		EmailFile ef=sendEmailService.getFileByClbId(fileId);
		if(ef!=null){
			return;
		}
		clbService.download(fileId, new FileSaverBridge(response,request));
	}
	@RequestMapping("/download/file/{fileId}")
	public void download(@PathVariable("fileId")int fileId,HttpServletRequest request,HttpServletResponse response,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		EmailFile ef=sendEmailService.getFileByClbId(fileId);
		if(ef==null){
			return;
		}
		if(!ef.getUploaderUmtId().equals(user.getUserInfo().getUmtId())){
			return;
		}
		clbService.download(fileId, new FileSaverBridge(response,request));
	}
	@RequestMapping
	public ModelAndView display(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		return  getModelAndView("user/email_send",user);
	}
	@RequestMapping(params="func=log")
	public ModelAndView displayLog(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		ModelAndView mv=getModelAndView("user/email_log",user);
		mv.addObject("emails",sendEmailService.getEmailByUmtId(user.getUserInfo().getUmtId()));
		return mv;
	}
	@RequestMapping("removeMember")
	@ResponseBody
	public JsonResult removeMember(
			@RequestParam("umtId")String umtId,
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		LdapOrg org=getMainOrg(user);
		if(org==null){
			return new JsonResult("权限不足");
		}
		sendEmailService.removeSendable(org.getDn(),umtId);
		return new JsonResult();
	}
	@RequestMapping("getHistoryGroup")
	@ResponseBody
	public JsonResult getHistoryGroup(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(!isCanUseEmail(user)){
			return new JsonResult("权限不足");
		}
		JsonResult jdo=new JsonResult();
		jdo.setData(sendEmailService.getGroupByUmtId(user.getUserInfo().getUmtId()));
		return jdo;
	}
	@RequestMapping("addMember")
	@ResponseBody
	public JsonResult addMember(
			@RequestParam("umtId")String umtId,
			@RequestParam("cstnetId")String cstnetId,
			@RequestParam("truename")String userName,
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		LdapOrg org=getMainOrg(user);
		if(org==null){
			return new JsonResult("权限不足");
		}
		EmailSendablePeople sep=new EmailSendablePeople();
		sep.setCstnetId(cstnetId);
		sep.setUmtId(umtId);
		sep.setOrgDn(org.getDn());
		sep.setUserName(userName);
		sep.setDomain(user.getCoreMailDomain());
		sendEmailService.addEmailSendable(sep);
		
		
		
		return new JsonResult();
	}
	@RequestMapping("getSendLogDetail")
	@ResponseBody
	public JsonResult getDetail(@RequestParam("emailId")int emailId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		JsonResult jr=new JsonResult();
		if(!isCanUseEmail(user)){
			return new JsonResult("权限不足");
		}
		Email email=sendEmailService.getEmailById(emailId);
		if(!email.getSenderUmtId().equals(user.getUserInfo().getUmtId())){
			return new JsonResult("该邮件并非您所发");
		}
		List<EmailGetter> getters=sendEmailService.getGetterByEmailId(emailId);
		if(CommonUtils.isNull(getters)){
			return new JsonResult("接收人为空");
		}
		List<EmailFile> files=sendEmailService.getFilesByEmailId(emailId);
		EmailDetail ed=new EmailDetail();
		ed.setEmail(email);
		ed.setFiles(files);
		ed.setGetters(getters);
		jr.setData(ed);
		return jr;
		
		
	}
	
	@RequestMapping("/getMyTeam")
	@ResponseBody
	public JsonResult getOrgAndGroup(@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		if(!isCanUseEmail(user)){
			return new JsonResult("您没有权限发送群发邮件");
		}
		JsonResult result = new JsonResult();
		String umtId = user.getUserInfo().getUmtId();
		List<LdapOrg> orgs = orgService.getMyOrgs(umtId);
		List<LdapGroup> groups = groupService.getMyGroups(umtId);
		result.setData(merge(orgs, groups));
		return result;
	}
	private List<Object> merge(List<LdapOrg> org, List<LdapGroup> group) {
		List<Object> result = new ArrayList<Object>();
		if (!CommonUtils.isNull(org)) {
			for (LdapOrg o : org) {
				result.add(o);
			}
		}
		if (!CommonUtils.isNull(group)) {
			for (LdapGroup g : group) {
				result.add(g);
			}
		}
		return result;
	}
	/***
	 * FireFox上传文件
	 * */
	@RequestMapping(value="/upload",method = RequestMethod.POST,headers = { "X-File-Name" })
	@ResponseBody
	public FileUploadResult uploadFile(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader("X-File-Name") String fileName) throws IOException {
		fileName=LdapUtils.decode(fileName);
		FileUploadResult result=new FileUploadResult();
		
		result.setFileName(fileName);
		if(fileName.length()>=255){
			result.setClbId(-2);
			return result;
		}
		int clbId=clbService.uploadUnResize(request.getInputStream(), fileName,request.getContentLength());
		result.setClbId(clbId);
		return result;
	}
	/***
	 * IE上传文件
	 * */
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody
	public void uploadFile(
			@RequestParam("qqfile") MultipartFile uplFile,
			HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		String fileName=LdapUtils.decode(uplFile.getOriginalFilename());
		FileUploadResult result=new FileUploadResult();
		result.setFileName(fileName);
		if(fileName.length()>=255){
			result.setClbId(-2);
			JSONHelper.writeJSONObject(response, JSONMapper.getJSONString(result));
			return;
		}
		//IE兼容，不这么整，会下载json
		int clbId=clbService.uploadUnResize(uplFile.getInputStream(), fileName,(int)uplFile.getSize());
		result.setClbId(clbId);
		JSONHelper.writeJSONObject(response, JSONMapper.getJSONString(result));
	}
	@RequestMapping("/send")
	@ResponseBody
	public JsonResult sendSms(
			@RequestParam("txtContent")String txtContent,
			@RequestParam("emailContent") String content,
			@RequestParam("emailTitle")String title,
			@RequestParam("name[]") String name[],
			@RequestParam("cstnetId[]") String cstnetIds[],
			@RequestParam("umtId[]") String umtId[],
			@RequestParam("clbId[]")int clbId[],
			@RequestParam("fileName[]")String fileName[],
			@RequestParam("sendToDchat")boolean sendToDhat,
			@RequestParam("saveAsGroup")boolean saveAsGroup,
			@RequestParam("groupName")String groupName,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		//保存邮件
		Email email=new Email();
		email.setTitle(title);
		email.setContent(content);
		email.setSenderCstnetId(user.getUserInfo().getCstnetId());
		email.setSenderName(user.getUserInfo().getTrueName());
		email.setSenderUmtId(user.getUserInfo().getUmtId());
		sendEmailService.saveEmail(email);
		int groupId=0;
		//是否需要保存成一个组
		if(saveAsGroup){
			EmailGroup eg=new EmailGroup();
			eg.setGroupMasterUmtId(user.getUserInfo().getUmtId());
			eg.setGroupName(CommonUtils.isNull(groupName)?title:groupName);
			sendEmailService.saveGroup(eg);
			groupId=eg.getId();
		}
		//保存收件人
		List<EmailGetter> egs=new ArrayList<EmailGetter>();
		for(int i=0;i<name.length;i++){
			EmailGetter eg=new EmailGetter();
			eg.setGetterName(name[i]);
			eg.setGetterCstnetId(cstnetIds[i]);
			eg.setGetterUmtId(umtId[i]);
			eg.setEmailId(email.getId());
			eg.setGroupId(groupId);
			egs.add(eg);
		}
		sendEmailService.saveGetter(egs);
		
		//有文件
		List<EmailFile> efs=new ArrayList<EmailFile>();
		if(!CommonUtils.isNull(clbId)&&clbId[0]!=-1){
			
			for(int i=0;i<clbId.length;i++){
				EmailFile ef=new EmailFile();
				ef.setClbId(clbId[i]);
				ef.setEmailId(email.getId());
				ef.setUploaderUmtId(user.getUserInfo().getUmtId());
				ef.setUploaderName(user.getUserInfo().getTrueName());
				ef.setUploaderCstnetId(user.getUserInfo().getCstnetId());
				ef.setFileName(fileName[i]);
				efs.add(ef);
			}
			sendEmailService.saveFiles(efs);
		}
		//发送给dchat
		if(sendToDhat){
			HttpPost post=new HttpPost(vmtConfig.getDchatNoticeUrl());
			post.add("sender", user.getUserInfo().getCstnetId());
			post.add("title", title);
			post.add("content", txtContent);
			for(EmailGetter eg:egs){
				post.add("names",eg.getGetterCstnetId());
			}
			String result=post.post();
			if("{\"status\":\"ok\"}".equals(result)){
				LOG.info("send to dchat msg is OK! sender:"+user.getUserInfo().getCstnetId()+",getter:"+Arrays.toString(cstnetIds));
			}else{
				LOG.error("send to dchat fail ,msg is"+result+",sender:"+user.getUserInfo().getCstnetId()+",getter:"+Arrays.toString(cstnetIds));
			}
		}
		
		
		JobThread.addJobThread(new BatchSendEmailJob(email, egs, efs));
		return new JsonResult();
	}
}
