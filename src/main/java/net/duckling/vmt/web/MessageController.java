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
/**
 * 
 */
package net.duckling.vmt.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.adapter.LdapUser2CoreMailUserAdapter;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.CharUtils;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.RenderUtils;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.SimpleEmail;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.coremail.CoreMailUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.IVmtMessageService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.service.thread.EmailSendJob;
import net.duckling.vmt.service.thread.JobThread;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.rest.ServiceException;

/**
 * @author lvly
 * @since 2013-5-20
 */
@Controller
@RequestMapping("/user/message")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class MessageController {
	private static final Logger LOG=Logger.getLogger(MessageController.class);
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private IUserService userService;
	@Autowired
	private UserService umtUserService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private ICoreMailService coreMailService;
	
	@Autowired
	private MQMessageSenderExt sender;
	@Autowired
	private IVmtMessageService msgService;
	
	@Autowired
	private IOrgService orgService;
	/**
	 * 显示消息
	 * @return
	 */
	@RequestMapping
	public String displsy(){
		return "/user/message";
	}
	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		String umtId=user.getUserInfo().getUmtId();
		List<LdapGroup> myGroup=groupService.getMyGroupsAll(umtId);
		List<LdapOrg> myOrg=orgService.getMyOrgsAll(umtId);
		String myGroupDN[]=LdapGroup.extractGroupId(myGroup);
		String myOrgDN[]=LdapOrg.extractOrgId(myOrg);
		String allDN[]=mergeArrays(myGroupDN,myOrgDN);
		
		List<LdapGroup> adminGroup=LdapGroup.extractAdminGroup(myGroup, umtId);
		List<LdapOrg> adminOrg=LdapOrg.extractAdminGroup(myOrg, umtId);
		
		String[] groupAdminsDN=LdapGroup.extractGroupId(adminGroup);
		String[] orgAdminsDN=LdapOrg.extractOrgId(adminOrg);
		String[] allAdminDN=mergeArrays(groupAdminsDN, orgAdminsDN);
		return new JsonResult(VmtMessage.groupBy(msgService.getMsgByICanSee(user.getUserInfo().getCstnetId(), allDN,allAdminDN)));
	}
	@SecurityMapping(level=SecurityLevel.ADMIN)
	@RequestMapping("/applyRegist")
	@ResponseBody
	public JsonResult applyRegist(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,@RequestParam("msgId")int msgId){
		return validateRegistRequest(msgId,true,user);
	}
	@SecurityMapping(level=SecurityLevel.ADMIN)
	@RequestMapping("/refuseRegist")
	@ResponseBody
	public JsonResult applyRegistX(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,@RequestParam("msgId")int msgId){
		return validateRegistRequest(msgId,false,user);
	}
	public JsonResult validateRegistRequest(int msgId,boolean apply,VmtSessionUser sUser){
		VmtMessage msg=msgService.getMsgById(msgId);
		if(msg==null){
			return new JsonResult("找不到消息");
		}
		if(!msg.getMsgType().equals(VmtMessage.MSG_TYPE_USER_REGIST_APPLY)){
			return new JsonResult("消息类型错误");
		}
		if(!msg.getMsgStatus().equals(VmtMessage.MSG_STATUS_NEED_REED)){
			return new JsonResult("消息状态错误");
		}
		if(CommonUtils.isNull(msg.getColumns())){
			return new JsonResult("必要的参数缺失");
		}
		String teamDN=msg.getTeamDN();
		LdapOrg org=orgService.getOrgByDN(teamDN);
		if(org==null){
			return new JsonResult("组织不存在");
		}
		if(!org.isAdmin(sUser.getUserInfo().getUmtId())){
			return new JsonResult("权限不足");
		}
		if(CommonUtils.isNull(org.getDomains())||!org.getIsCoreMail()){
			return new JsonResult("组织信息错误");
		}
		Map<String,String> map=msg.getKeyValue();
		String email=map.get("email");
		if(!org.isMainDomain(EmailUtils.getDomain(email))){
			return new JsonResult("域名错误");
		}
		//同意
		if(apply){
			if(userService.isGlobalLoginNameExists(email)){
				return new JsonResult("用户已存在");
			}
			msgService.updateMsgReaded(msgId);
			CoreMailUser user=new CoreMailUser();
			String password=CharUtils.random(8);
			user.setDomain(EmailUtils.getDomain(email));
			user.setEmail(EmailUtils.getNameFromEmail(email));
			user.setOrgId(org.getSymbol());
			user.setPassword(password);
			user.setVisible(true);
			user.setName(map.get("trueName"));
			coreMailService.createUser(user);
			try {
				String umtId=umtUserService.generateUmtId(new String[]{email})[0];
				LdapUser ldapUser=LdapUser2CoreMailUserAdapter.convert(user, org.getDn());
				ldapUser.setCustom1(map.get("custom1"));
				ldapUser.setCstnetId(email);
				ldapUser.setCustom2(map.get("custom2"));
				ldapUser.setTelephone(map.get("phone"));
				ldapUser.setUmtId(umtId);
				ldapUser.setVisible(true);
				ldapUser.setCurrentDisplay(org.getName());
				String userDN=userService.addUserToNode(org.getDn(), ldapUser,sUser.getUserInfo());
				sender.sendCreateUserMessage(new String[]{umtId}, userDN);
				indexService.addIndexByUser(org.getDn(),new boolean[]{true}, ldapUser);
				//发送邮件
				String title="电子邮件账号审核通过";
				Map<String,Object> emailP=new HashMap<String,Object>();
				emailP.put("${email}", email);
				emailP.put("${adminEmail}", sUser.getUserInfo().getCstnetId());
				String content=RenderUtils.getHTML(emailP, RenderUtils.APPLY_REGIST_OK);
				JobThread.addJobThread(new EmailSendJob(new SimpleEmail(map.get("contractEmail"),content,title)));
				//返回信息
				JsonResult result=new JsonResult();
				JSONObject obj=new JSONObject();
				obj.put("email", email);
				obj.put("password",password);
				result.setData(obj);
				return result;
			} catch (ServiceException e) {
				LOG.error(e.getMessage(),e);
				return new JsonResult("passport交互错误");
			}
		}else{
			msgService.deleteMsg(msgId);
			//sendEmail
			String title="电子邮件账号审核未通过";
			Map<String,Object> emailP=new HashMap<String,Object>();
			emailP.put("${email}", email);
			emailP.put("${adminEmail}", sUser.getUserInfo().getCstnetId());
			String content=RenderUtils.getHTML(emailP, RenderUtils.APPLY_REGIST_REFUSE);
			JobThread.addJobThread(new EmailSendJob(new SimpleEmail(map.get("contractEmail"),content,title)));
		}
		return new JsonResult();
	}
	/**
	 * 接受邀请
	 * @param dn
	 * @return
	 */
	@RequestMapping("/accept")
	@ResponseBody
	public boolean accept(@RequestParam("dn")String dn,@ModelAttribute("msgId")int msgId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		LdapUser localUser=updateUserStatus(user, dn,LdapUser.STATUS_ACTIVE,msgId);
		if(localUser==null){
			LOG.error("user not found ,dn=["+dn+"]");
			return true;
		}
		sender.sendCreateUserMessage(new String[]{localUser.getUmtId()}, localUser.getRoot());
		return true;
	}
	@RequestMapping("/acceptApply")
	@ResponseBody
	public boolean acceptApply(@RequestParam("dn")String dn,@ModelAttribute("msgId")int msgId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		LdapUser localUser=updateApplyUserStatus(user, dn,LdapUser.STATUS_ACTIVE,msgId);
		if(localUser==null){
			return false;  
		}
		sender.sendCreateUserMessage(new String[]{localUser.getUmtId()}, localUser.getRoot());
		return true;
	}
	@RequestMapping("/refuseApply")
	@ResponseBody
	public boolean refuseApply(@RequestParam("dn")String dn,@ModelAttribute("msgId")int msgId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		return updateApplyUserStatus(user, dn,LdapUser.STATUS_REFUSE,msgId)!=null;
	}
	/**
	 * 拒绝邀请
	 * @param dn
	 * @return
	 */
	@RequestMapping("/refuse")
	@ResponseBody
	public boolean refuse(@RequestParam("dn")String dn,@ModelAttribute("msgId")int msgId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		updateUserStatus(user, dn,LdapUser.STATUS_REFUSE,msgId);
		return true;
	}
	private LdapUser updateApplyUserStatus(VmtSessionUser user,String dn,String status,int msgId){
		String decodeDn=LdapUtils.decode(dn);
		String teamDn=LdapUtils.getTeamDN(decodeDn);
		LdapGroup group=groupService.getGroupByDN(teamDn);
		if(!CommonUtils.isEqualsContain(group.getAdmins(),user.getUserInfo().getUmtId())&&!user.getIsSuperAdmin()){
			return null;
		}
		LdapUser localUser=CommonUtils.first(userService.searchUsersByDN(decodeDn, ""));
		attrService.update(localUser.getDn(), "vmt-status", status);
		indexService.updateStatus(teamDn,localUser.getUmtId(),status);
		msgService.deleteMsg(msgId);
		return localUser;
	}
	private LdapUser updateUserStatus(VmtSessionUser user,String dn,String status,int msgId){
		String umtId=user.getUserInfo().getUmtId();
		String decodeDn=LdapUtils.decode(dn);
		LdapUser localUser=CommonUtils.first(userService.searchUsersByUmtId(decodeDn, new String[]{umtId}));
		if(localUser==null){
			msgService.deleteMsg(msgId);
			LOG.info("the user cant found,dn=["+umtId+"],maybe delete also");
			return null;
		}
		attrService.update(localUser.getDn(), "vmt-status", status);
		indexService.updateStatus(decodeDn,umtId,status);
		msgService.deleteMsg(msgId);
		return localUser;
	}
	@RequestMapping("getCount")
	@ResponseBody
	public JsonResult getMsgCount(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		String umtId=user.getUserInfo().getUmtId();
		List<LdapGroup> myGroup=groupService.getMyGroupsAll(umtId);
		List<LdapOrg> myOrg=orgService.getMyOrgsAll(umtId);
		String myGroupDN[]=LdapGroup.extractGroupId(myGroup);
		String myOrgDN[]=LdapOrg.extractOrgId(myOrg);
		String allDN[]=mergeArrays(myGroupDN,myOrgDN);
		
		List<LdapGroup> adminGroup=LdapGroup.extractAdminGroup(myGroup, umtId);
		List<LdapOrg> adminOrg=LdapOrg.extractAdminGroup(myOrg, umtId);
		
		String[] groupAdminsDN=LdapGroup.extractGroupId(adminGroup);
		String[] orgAdminsDN=LdapOrg.extractOrgId(adminOrg);
		String[] allAdminDN=mergeArrays(groupAdminsDN, orgAdminsDN);
		int count=msgService.getMsgByICanSeeCount(user.getUserInfo().getCstnetId(),allDN,allAdminDN);
		return new JsonResult(count);
	}
	private String[] mergeArrays(String[] a1,String[] a2){
		String[] all=new String[a1.length+a2.length];
		int index=0;
		for(String gDN:a1){
			all[index++]=gDN;
		}
		for(String oDN:a2){
			all[index++]=oDN;
		}
		return all;
	}
	
}
