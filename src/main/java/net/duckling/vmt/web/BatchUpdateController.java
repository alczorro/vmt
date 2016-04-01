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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.falcon.api.serialize.JSONMapper;
import net.duckling.vmt.common.adapter.LdapUser2CoreMailUserAdapter;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.readFile.BatchRow;
import net.duckling.vmt.common.readFile.impl.XlsExportBatchResolver;
import net.duckling.vmt.common.readFile.impl.XlsInsertImportBatchResolver;
import net.duckling.vmt.common.readFile.impl.XlsUpdateImportBatchResolver;
import net.duckling.vmt.common.util.BrowseUtils;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.BatchOperation;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.coremail.CoreMailUser;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.domain.message.VmtMsgColumns;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IDepartmentService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.IVmtMessageService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.web.helper.IsUpgradeHelper;
import net.duckling.vmt.web.helper.JSONHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.vlabs.duckling.api.umt.rmi.exception.UserExistException;
import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.rest.ServiceException;

/**
 * 批量删除用户
 * @author lvly
 * @since 2013-8-27
 */
@Controller
@RequestMapping("/user/batch")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
@SecurityMapping(level=SecurityLevel.ADMIN)
public class BatchUpdateController {
	private static final Logger LOGGER=Logger.getLogger(BatchUpdateController.class);
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IOrgService orgService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IDepartmentService deptService;
	
	@Autowired
	private MQMessageSenderExt sender;
	
	@Autowired
	private IVmtIndexService indexService;
	
	@Autowired
	private IsUpgradeHelper helper;
	@Autowired
	private UserService umtUserService;
	@Autowired
	private ICoreMailService coreMailService;
	@Autowired
	private IVmtMessageService msgService;
	
	
	private static final String FUNC_INSERT="insert";
	private static final String FUNC_UPDATE="update";
	/**
	 * 显示
	 **/
	@SecurityMapping(level=SecurityLevel.ADMIN)
	@RequestMapping(value="show")
	public ModelAndView display(@RequestParam("dn")String dn,@RequestParam("func")String func){
		
		ModelAndView mv;
		if(FUNC_INSERT.equals(func)){
			mv=new ModelAndView("/user/batchImport");
		}else if(FUNC_UPDATE.equals(func)){
			mv=new ModelAndView("/user/batchUpdate");
		}else{
			return null;
		}
		String decodeDN=LdapUtils.decode(dn);
		if(LdapUtils.isOrgDN(decodeDN)){
			mv.addObject("node",orgService.getOrgByDN(decodeDN));
		}else if(LdapUtils.isGroupDN(decodeDN)){
			mv.addObject("node",groupService.getGroupByDN(decodeDN));
		}
		
		return mv;
	}
	
	/***
	 * FireFox上传文件
	 * @throws ServiceException
	 * */
	@RequestMapping(value="/import",method = RequestMethod.POST,params={"func"},headers = { "X-File-Name" })
	@SecurityMapping(level=SecurityLevel.ADMIN)
	@ResponseBody
	public BatchUpdateJob importXls(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader("X-File-Name") String fileName,
			@RequestParam("dn")String dn,
			@RequestParam("func")String func) throws IOException, ServiceException {
		String decodeDN=LdapUtils.decode(dn);
		List<LdapUser> users=userService.searchUsersByAll(new String[]{decodeDN}, null);
		List<BatchRow> rows=null;
		if(FUNC_INSERT.equals(func)){
			rows=new XlsInsertImportBatchResolver(fileName, request.getInputStream(),decodeDN).resolve();
		}else if(FUNC_UPDATE.equals(func)){
			rows=new XlsUpdateImportBatchResolver(users, fileName, request.getInputStream(),decodeDN).resolve();

		}
		return new BatchUpdateJob(rows);
	}
	/***
	 * IE上传文件
	 * */
	@RequestMapping(value="/import",method = RequestMethod.POST,params="func")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.ADMIN)
	public void importXls(
			@RequestParam("qqfile") MultipartFile uplFile,
			HttpServletResponse response,
			@RequestParam("dn")String dn,
			@RequestParam("func")String func) throws IOException,ServiceException  {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		String decodeDN=LdapUtils.decode(dn);
		List<LdapUser> users=userService.searchUsersByAll(new String[]{decodeDN}, null);
		List<BatchRow> rows=null;
		
		if(FUNC_INSERT.equals(func)){
			rows=new XlsInsertImportBatchResolver(uplFile.getOriginalFilename(), uplFile.getInputStream(),decodeDN).resolve();
		}else if(FUNC_UPDATE.equals(func)){
			rows=new XlsUpdateImportBatchResolver(users, uplFile.getOriginalFilename(), uplFile.getInputStream(),decodeDN).resolve();

		}
		//IE兼容，不这么整，会下载json
		JSONHelper.writeJSONObject(response, new BatchUpdateJob(rows).toJSON());
	}
	private String[] getPath(String currentDisplay){
		if(CommonUtils.isNull(currentDisplay)||!currentDisplay.contains("/")){
			return new String[]{};
		}
		return currentDisplay.split("/");
	} 
	@RequestMapping(value="/doTask",method=RequestMethod.POST,params="func=delete")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.ADMIN)
	public JsonResult doTaskDelete(@RequestParam("cstnetId")String cstnetId,@RequestParam("dn")String dn){
		String decodeDn=LdapUtils.decode(dn);
		LdapOrg org=orgService.getOrgByDN(decodeDn);
		String errorMsg="无法找到此域名";
		if(org==null){
			return new JsonResult(errorMsg);
		}
		if(CommonUtils.isNull(org.getDomains())){
			return new JsonResult(errorMsg);
		}
		if(!org.getIsCoreMail()){
			return new JsonResult(errorMsg);
		}
		String email=CommonUtils.trim(cstnetId);
		if(!EmailUtils.isEmail(email)){
			return new JsonResult("邮件格式错误"); 
		}
		String domain=EmailUtils.getDomain(email);
		if(!CommonUtils.isEqualsContain(org.getDomains(), domain)){
			return new JsonResult(errorMsg);
		}
		
		if(!coreMailService.isUserExists(email)){
			return new JsonResult(errorMsg);
		}
		List<LdapUser> users=userService.searchUsersByCstnetId(decodeDn, new String[]{email});
		coreMailService.deleteUser(email);
		if(!CommonUtils.isNull(users)){
			LdapUser user=users.get(0);
			sender.sendUnbindMessage(user.getDn());
			userService.unbind(new String[]{user.getDn()});
			indexService.deleteUser(new String[]{user.getDn()});
		}
		return new JsonResult();
	}
	
	@RequestMapping(value="/doTask",method=RequestMethod.POST,params="func=insert")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.ADMIN,dnParam="teamDn")
	public JsonResult doTaskInsert(BatchOperation operation,LdapUser user,@RequestParam("password")String password,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser sessionUser){
		boolean isGroup=LdapUtils.isGroupDN(operation.getTeamDn());
		String destDn=isGroup?operation.getTeamDn():deptService.getDepartByPath(operation.getTeamDn(),getPath(user.getCurrentDisplay())).getDn();
		validateTask(user);
		user.setStatus(LdapUser.STATUS_ACTIVE);
		String[] cstnetId=new String[]{user.getCstnetId()};
		boolean isTemp=false;
		switch(operation.getType()){
			case BatchOperation.TYPE_IMPORT:{
				if(LdapUtils.isOrgDN(operation.getTeamDn())){
					LdapOrg org=orgService.getOrgByDN(operation.getTeamDn());
					
					if(sessionUser.getIsSuperAdmin()||
							(org.getIsCoreMail()&&CommonUtils.isEqualsContain(org.getDomains(),EmailUtils.getDomain(user.getCstnetId())))){
						user.setStatus(LdapUser.STATUS_ACTIVE);
					}else{
						user.setStatus(LdapUser.STATUS_TEMP);
						isTemp=true;
					}
				}else{
					user.setStatus(LdapUser.STATUS_TEMP);
				}
				//don't need do sth;
				break;
			}
			case BatchOperation.TYPE_REGIST_COREMAIL:{
				String deptSymbol=null;
				if(!CommonUtils.isNull(destDn)){
					if(new DistinguishedName(destDn).size()>2){
						deptSymbol=LdapUtils.getLastValue(destDn);
					}
				}
				CoreMailUser u=LdapUser2CoreMailUserAdapter.convert(user,LdapUtils.getLastValue(operation.getTeamDn()), deptSymbol,password);
				LdapOrg org=orgService.getOrgByDN(operation.getTeamDn());
				if(!org.isMainDomain(u.getDomain())||!org.isAdmin(sessionUser.getUserInfo().getUmtId())){
					return new JsonResult("域名错误");
				}
				user.setUserFrom(LdapUser.USER_FROM_CORE_MAIL);
				coreMailService.createUser(u);
				break;
			}
			case BatchOperation.TYPE_REGIST_UMT:{
				UMTUser u=new UMTUser();
				u.setPassword(password);
				u.setTruename(user.getName());
				u.setCstnetId(user.getCstnetId());
				try {
					umtUserService.createUser(u);
				} catch (UserExistException e) {
					LOGGER.error(e.getMessage(),e);
					return new JsonResult();
				}
				break;
			}default:{
				return new JsonResult();
			}
			
		}
		String umtId=null;
		try {
			umtId=umtUserService.generateUmtId(cstnetId)[0];
			user.setUmtId(umtId);
		} catch (ServiceException e) {
			LOGGER.error(e.getMessage(),e);
		}
		userService.addUserToNode(destDn, user,sessionUser.getUserInfo());
		if(!isTemp){
			sender.sendCreateUserMessage(new String[]{umtId}, destDn);
		}else{
			saveMsg(user.getCstnetId(),user.getUmtId(),operation.getTeamDn(),sessionUser);
		}
		indexService.addIndexByUser(operation.getTeamDn(),new boolean[]{true}, user);
		return new JsonResult();	
	}
	private void saveMsg(String cstnetId,String umtId,String teamDN,VmtSessionUser sessionUser){
		VmtMessage msg=new VmtMessage();
		msg.setEntryId(cstnetId);
		msg.setMsgType(VmtMessage.MSG_TYPE_USER_ADD_TEMP);
		msg.setMsgStatus(VmtMessage.MSG_STATUS_NEED_REED);
		msg.setMsgTo(VmtMessage.MSG_TO_USER);
		msg.setTeamDN(LdapUtils.getTeamDN(teamDN));
		List<VmtMsgColumns> columns=new ArrayList<VmtMsgColumns>();
		if(!msgService.isEquals(msg)){
			if(LdapUtils.isOrgDN(teamDN)){
				msg.setTeamName(orgService.getOrgByDN(teamDN).getName());
			}else{
				msg.setTeamName(groupService.getGroupByDN(teamDN).getName());
			}
			msgService.insertMsg(msg);
			columns.add(new VmtMsgColumns("whoaddCstnetId",sessionUser.getUserInfo().getCstnetId(),msg.getId()));
			columns.add(new VmtMsgColumns("whoaddUmtId",sessionUser.getUserInfo().getUmtId(),msg.getId()));
			columns.add(new VmtMsgColumns("whoaddTrueName",sessionUser.getUserInfo().getTrueName(),msg.getId()));
	}
	msgService.insertMsgColumns(columns);
	}
	@RequestMapping(value="/doTask",method=RequestMethod.POST,params="func=update")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.ADMIN,dnParam="teamDn")
	public JsonResult doTaskUpdate(BatchOperation operation,LdapUser user){
		boolean isGroup=LdapUtils.isGroupDN(operation.getTeamDn());
		String destDn=isGroup?operation.getTeamDn():deptService.getDepartByPath(operation.getTeamDn(),getPath(user.getCurrentDisplay())).getDn();
		String[] cstnetIds=new String[]{user.getCstnetId()};
		validateTask(user);
		//更新数据
		if(BatchOperation.TYPE_UPDATE.equals(operation.getType())){
			//会发生移动
			if(!operation.getBeforecurrentDisplay().equals(user.getCurrentDisplay())&&!isGroup&&!destDn.equals(operation.getTeamDn())){
				String[] userDn=new String[]{user.getDn()};
				userService.move(userDn,destDn,true);
				LdapUser createUser=CommonUtils.first(userService.searchUsersByCstnetId(destDn, cstnetIds));
				user.setDn(createUser.getDn());
				sender.sendMoveUserMessage(userDn, destDn);
			}
			userService.updateUser(user);
			LdapUser orgUser=userService.getUserByDN(user.getDn());
			if(LdapUser.STATUS_ACTIVE.equals(orgUser.getStatus())){
				sender.sendUpdateUser(orgUser);
			}
		}else{
			return new JsonResult("未知的操作");
		}
		if(operation.getIndex()==operation.getAll()){
			indexService.buildAIndexJob(operation.getTeamDn());
		}
 		return new JsonResult();
	}
	private void validateTask(LdapUser user){
		if("男".equals(user.getSex())){
			user.setSex(LdapUser.SEX_MALE);
		}else if("女".equals(user.getSex())){
			user.setSex(LdapUser.SEX_FEMALE);
		}else{
			user.setSex("");
		}
		if("正常".equals(user.getAccountStatus())){
			user.setAccountStatus(LdapUser.ACCOUNT_STATUS_NORMAL);
		}else if("锁定".equals(user.getAccountStatus())){
			user.setAccountStatus(LdapUser.ACCOUNT_STATUS_LOCKED);
		}else if("停用".equals(user.getAccountStatus())){
			user.setAccountStatus(LdapUser.ACCOUNT_STATUS_STOP);
		}else{
			user.setAccountStatus("");
		}
	}
	
	class BatchUpdateJob{
		private List<BatchRow> rows;
		private boolean success;
		public BatchUpdateJob(List<BatchRow> rows){
			if(!CommonUtils.isNull(rows)){
				this.success=true;
				this.rows=rows;
			}
		}
		public List<BatchRow> getRows() {
			return rows;
		}
		public void setRows(List<BatchRow> rows) {
			this.rows = rows;
		}
		public boolean isSuccess() {
			return success;
		}
		public void setSuccess(boolean success) {
			this.success = success;
		}
		public String toJSON(){
			return JSONMapper.getJSONString(this);
		}
	}
	@RequestMapping(value="/export",params="func=update")
	public void exportXls(@RequestParam("dn")String dn,@RequestParam("name")String name,
			HttpServletRequest request,HttpServletResponse response)throws IOException{
		String decodeDN=LdapUtils.decode(dn);
		String decodeName=LdapUtils.decode(name);
		List<LdapUser> users=userService.searchUsersByAll(new String[]{decodeDN}, null);
		response.setHeader("Content-Disposition", BrowseUtils.encodeFileName(request.getHeader("User-Agent"),decodeName+".xls"));
		new XlsExportBatchResolver(users,decodeName ).export(response.getOutputStream());
	}
}
