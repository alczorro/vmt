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

import java.util.ArrayList;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.coremail.CoreMailUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.domain.message.VmtMsgColumns;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IPrivilegeService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.IVmtMessageService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.web.helper.CoreMailHelper;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 针对用户的一些操作
 * 
 * @author lvly
 * @since 2013-6-21
 */
@Controller
@SessionAttributes(KEY.SESSION_LOGIN_USER)
@RequestMapping("/user")
public class UserController {
	private static final Logger LOG=Logger.getLogger(UserController.class);
	/**
	 * 用户服务类
	 */
	@Autowired
	private IUserService userService;
	/**
	 * 节点属性服务类
	 */
	@Autowired
	private IAttributeService attributeService;
	/**
	 * 权限服务类
	 */
	@Autowired
	private IPrivilegeService privService;

	@Autowired
	private IVmtIndexService indexService;

	@Autowired
	private MQMessageSenderExt sender;

	@Autowired
	private CoreMailHelper coreMailHelper;
	@Autowired
	private ICoreMailService coreMailService;

	@Autowired
	private IVmtMessageService vmtMsgService;
	@Autowired
	private IGroupService groupService;
	
	@Autowired
	private IOrgService orgService;

	/**
	 * 判断用户是否存在于其他的组织机构
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/isExists")
	@ResponseBody
	public boolean isExistsInOrg(
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		return false;
	}

	@SecurityMapping(level = SecurityLevel.ADMIN)
	@RequestMapping("changePassword")
	@ResponseBody
	public boolean changePassword(@RequestParam("dn")String dn,@RequestParam("cstnetId") String cstnetId,
			@RequestParam("password") String password,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user) {
		String decodeUserDN=LdapUtils.decode(dn);
		String decodeOrgDN=LdapUtils.getTeamDN(decodeUserDN);
		if(!LdapUtils.isOrgDN(decodeOrgDN)){
			return false;
		}
		LdapOrg org=orgService.getOrgByDN(decodeOrgDN);
		if(org==null||CommonUtils.isNull(org.getDomains())||!org.getIsCoreMail()){
			return false;
		}
		if(!org.isAdmin(user.getUserInfo().getUmtId())){
			return false;
		}
		if(!org.isMainDomain(EmailUtils.getDomain(user.getUserInfo().getCstnetId()))){
			return false;
		}
		String domain=EmailUtils.getDomain(cstnetId);
		if(!org.getDomains()[0].equals(domain)){
			return false;
		}
		LOG.info("changePassword:"+cstnetId);
		return coreMailService.changePassword(cstnetId, password);
	}

	/**
	 * 添加人员
	 * 
	 * @param dn
	 * @param peoples
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	@SecurityMapping(level = SecurityLevel.ADMIN)
	public boolean[] add(@RequestParam("dn") String dn,
			@RequestParam("peoples") String[] peoples,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		String decodeDN = LdapUtils.decode(dn);
		boolean isOrg = LdapUtils.isOrgSub(decodeDN);
		LdapOrg org=orgService.getOrgByDN(LdapUtils.getTeamDN(decodeDN));
		//这时候是未确认,加入群组,目标组织不是coreMail,或者域名列表是空
		if(!isOrg||org==null||!org.getIsCoreMail()||CommonUtils.isNull(org.getDomains())){
			return saveUser(decodeDN,peoples,LdapUser.STATUS_TEMP,true,user);
		}else{
			boolean flag[]=new boolean[peoples.length];
			int index=0;
			for(String people:peoples){
				String cstnetId=getCstnetId(people);
				String domain=EmailUtils.getDomain(cstnetId);
				if(CommonUtils.isEqualsContain(org.getDomains(), domain)){
					flag[index++]=saveUser(decodeDN,new String[]{people},LdapUser.STATUS_ACTIVE,false,user)[0];
				}else{
					flag[index++]=saveUser(decodeDN,new String[]{people},LdapUser.STATUS_TEMP,true,user)[0];
				}
			}
			return flag;
		}
	}
	private boolean[] saveUser(String decodeDN,String[]peoples,String userStatus,boolean sendMail,VmtSessionUser user){
		boolean[] flag = userService.addUserToNodeUnUsed(decodeDN, peoples,
				user.getUserInfo(), userStatus, sendMail, false);
		indexService.addIndexByCstnetId(decodeDN, flag, peoples);
		saveVmtMsg(flag, decodeDN, user,peoples);
		if(LdapUser.STATUS_ACTIVE.equals(userStatus)){
			sender.sendCreateUserMessage(peoples, flag, decodeDN);
		}
		return flag;
		
	}
	
	private String getCstnetId(String cstnetIdAndName){
		return cstnetIdAndName.split(KEY.GLOBAL_DATA_SPLIT)[0].toLowerCase();
	}
	private void saveVmtMsg(boolean[] flag,String decodeDN,VmtSessionUser user,String[] peoples){
		LdapGroup group=groupService.getGroupByDN(decodeDN);
		List<VmtMsgColumns> columns=new ArrayList<VmtMsgColumns>();
		for(int i=0;i<flag.length;i++){
			if(!flag[i]){
				continue;
			}
			VmtMessage msg=new VmtMessage();
			msg.setEntryId(peoples[i].split(KEY.GLOBAL_DATA_SPLIT)[0]);
			msg.setMsgType(VmtMessage.MSG_TYPE_USER_ADD_TEMP);
			msg.setMsgStatus(VmtMessage.MSG_STATUS_NEED_REED);
			msg.setMsgTo(VmtMessage.MSG_TO_USER);
 			msg.setTeamDN(LdapUtils.getTeamDN(decodeDN));
			if(!vmtMsgService.isEquals(msg)){
				msg.setTeamName(group.getName());
				vmtMsgService.insertMsg(msg);
				columns.add(new VmtMsgColumns("whoaddCstnetId",user.getUserInfo().getCstnetId(),msg.getId()));
				columns.add(new VmtMsgColumns("whoaddUmtId",user.getUserInfo().getUmtId(),msg.getId()));
				columns.add(new VmtMsgColumns("whoaddTrueName",user.getUserInfo().getTrueName(),msg.getId()));
			}
		}
		vmtMsgService.insertMsgColumns(columns);
	}

	@RequestMapping("addCoreMail")
	@SecurityMapping(level = SecurityLevel.ADMIN)
	@ResponseBody
	public JsonResult addCoremail(@RequestParam("dn") String dn, CoreMailUser user) {
		if(CommonUtils.isNull(user.getEmail())
				||CommonUtils.isNull(user.getName())
				||CommonUtils.isNull(user.getPassword())
				||CommonUtils.isNull(user.getDomain())){
			return new JsonResult("缺乏必要的参数");
		}
		String email=user.getEmail() + "@" + user.getDomain();
		if(!EmailUtils.isEmail(email)){
			return new JsonResult("不是有效的邮件地址");
		}
		if(userService.isGlobalLoginNameExists(email)){
			return new JsonResult("用户已存在");
		}
		if (CommonUtils.isNull(dn)) {
			return new JsonResult("找不到团队");
		}
		String decode = LdapUtils.decode(dn);
		if (!LdapUtils.isOrgSub(decode)) {
			return new JsonResult("找不到团队");
		}
		LdapOrg org=orgService.getOrgByDN(LdapUtils.getTeamDN(decode));
		if(!org.getIsCoreMail()||CommonUtils.isNull(org.getDomains())){
			return new JsonResult("域名无效");
		}
		if(!org.isMainDomain(user.getDomain())){
			return new JsonResult("域名无效");
		}
		
		
		user.setOrgId(LdapUtils.getLastValue(LdapUtils.getTeamDN(decode)));
		if (!LdapUtils.isOrgDN(decode)) {
			user.setOuId(LdapUtils.getLastValue(decode));
		}
		user.setVisible(true);
		coreMailService.createUser(user);
		user.setEmail(user.getEmail()+"@"+user.getDomain());
		List<CoreMailUser> list = new ArrayList<CoreMailUser>();
		list.add(user);
		boolean[] flag = userService.addUserToNodeCoreMail(decode, list);
		indexService.addIndexByCstnetId(decode, flag,new String[] { user.getEmail() });
		sender.sendCreateUserMessage(new String[] { user.getEmail() }, flag,decode);
		return new JsonResult();
	}

	/**
	 * 删除人
	 * 
	 * @param dns
	 * @param user
	 * @return
	 */
	@RequestMapping("/unbind")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.ADMIN)
	public boolean unbind(@RequestParam("dns") String[] dns,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		String[] decodes = LdapUtils.decode(dns);
		String rootNode = LdapUtils.getTeamDN(decodes[0]);
		if (!user.getIsSuperAdmin()&& !privService.isAdmin(rootNode, user.getUserInfo().getUmtId())) {
			return false;
		}
		if (CommonUtils.isStartWith(decodes, "vmt-umtid="
				+ user.getUserInfo().getUmtId())) {
			LOG.error("dont remove self"+user.getUserInfo().getCstnetId());
			return false;
		}
		sender.sendUnbindMessage(decodes);
		userService.unbind(decodes);
		indexService.buildAIndexJob(LdapUtils.getTeamDN(CommonUtils
				.first(decodes)));
		return true;
	}

	/**
	 * 删除邮箱账号
	 * */
	@RequestMapping("/removeCoreMail")
	@ResponseBody
	@SecurityMapping(level = SecurityLevel.ADMIN)
	public boolean removeCoreMail(@RequestParam("dn") String dn,
			@RequestParam("cstnetId") String cstnetId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user) {
		String decodeUserDN=LdapUtils.decode(dn);
		String decodeOrgDN=LdapUtils.getTeamDN(decodeUserDN);
		if(!LdapUtils.isOrgDN(decodeOrgDN)){
			return false;
		}
		
		LdapOrg org=orgService.getOrgByDN(decodeOrgDN);
		if(org==null||CommonUtils.isNull(org.getDomains())||!org.getIsCoreMail()){
			return false;
		}
		if(!org.isAdmin(user.getUserInfo().getUmtId())){
			return false;
		}
		String adminEmail=user.getUserInfo().getCstnetId();
		if(!org.isMainDomain(EmailUtils.getDomain(adminEmail))){
			return false;
		}
		String domain=EmailUtils.getDomain(cstnetId);
		if(!org.getDomains()[0].equals(domain)){
			return false;
		}
		LOG.info("remove coremail:"+org.getName()+","+cstnetId);
		String decode[] = new String[] {decodeUserDN};
		sender.sendUnbindMessage(decode);
		userService.unbind(decode);
		coreMailService.deleteUser(cstnetId);
		indexService.deleteUser(decode);
		return true;
	}

	/**
	 * 移动人
	 * 
	 * @param userDns
	 * @param destDn
	 * @return
	 */
	@RequestMapping("/move")
	@ResponseBody
	@SecurityMapping(level = SecurityLevel.ADMIN, dnParam = "destDn")
	public boolean move(@RequestParam("userDns") String[] userDns,
			@RequestParam("destDn") String destDn) {
		String decodeUserDns[] = LdapUtils.decode(userDns);
		String decodeDestDn = LdapUtils.decode(destDn);
		coreMailHelper.checkDepartment(decodeDestDn);
		coreMailHelper.batchMoveUser(decodeUserDns, decodeDestDn);
		userService.move(decodeUserDns, decodeDestDn, true);
		sender.sendMoveUserMessage(decodeUserDns, decodeDestDn);

		return true;
	}

	/**
	 * 重命名用户的真实姓名
	 * 
	 * @param dn
	 * @param name
	 * @return
	 */
	@RequestMapping("/rename")
	@ResponseBody
	@SecurityMapping(level = SecurityLevel.SELF_OR_ADMIN)
	public boolean rename(@RequestParam("dn") String dn,
			@RequestParam("name") String name) {
		String decodeDn = LdapUtils.decode(dn);
		attributeService.update(decodeDn, "vmt-name", name);
		indexService.updateUserName(decodeDn, name);
		sender.sendUpdateMessage(decodeDn);
		return true;
	}

	/**
	 * 重新发送邀请链接
	 * 
	 * @param dn
	 * @param user
	 * @return
	 */
	@RequestMapping("/resend")
	@SecurityMapping(level = SecurityLevel.ADMIN)
	@ResponseBody
	public boolean resend(@RequestParam("dn") String dn,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		String decodeDN=LdapUtils.decode(dn);
		LdapUser oUser=userService.getUserByDN(decodeDN);
		LdapGroup group=groupService.getGroupByDN(LdapUtils.getTeamDN(decodeDN));
		userService.resend(oUser, user.getUserInfo());
		List<VmtMsgColumns> columns=new ArrayList<VmtMsgColumns>();
		VmtMessage msg=new VmtMessage();
		msg.setEntryId(oUser.getCstnetId());
		msg.setMsgType(VmtMessage.MSG_TYPE_USER_ADD_TEMP);
		msg.setMsgStatus(VmtMessage.MSG_STATUS_NEED_REED);
		msg.setMsgTo(VmtMessage.MSG_TO_USER);
		msg.setTeamDN(LdapUtils.getTeamDN(decodeDN));
		if(!vmtMsgService.isEquals(msg)){
			msg.setTeamName(group.getName());
			vmtMsgService.insertMsg(msg);
			columns.add(new VmtMsgColumns("whoaddCstnetId",user.getUserInfo().getCstnetId(),msg.getId()));
			columns.add(new VmtMsgColumns("whoaddUmtId",user.getUserInfo().getUmtId(),msg.getId()));
			columns.add(new VmtMsgColumns("whoaddTrueName",user.getUserInfo().getTrueName(),msg.getId()));
			vmtMsgService.insertMsgColumns(columns);
		}
		return true;
	}

	/**
	 * 获得用户详细信息
	 * */
	@RequestMapping("/show")
	@SecurityMapping(level = SecurityLevel.VIEW)
	@ResponseBody
	public LdapUser show(@RequestParam("dn") String dn,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		String decodeDn=LdapUtils.decode(dn);
		String teamDn=LdapUtils.getTeamDN(dn);
		LdapUser lu= userService.getUserByDN(decodeDn);
		if(LdapUtils.isOrgDN(teamDn)){
			LdapOrg lo=orgService.getOrgByDN(teamDn);
			if(user.getIsSuperAdmin()||lo.isAdmin(user.getUserInfo().getUmtId())){
				return lu;
			}
			if(lo.isHideMobile()){
				lu.setTelephone(null);
				return lu;
			}
		}else{
			LdapGroup lg=groupService.getGroupByDN(teamDn);
			if(user.getIsSuperAdmin()||lg.isAdmin(user.getUserInfo().getUmtId())){
				return lu;
			}
			if(lg.isHideMobile()){
				lu.setTelephone(null);
				return lu;
			}
		}
		return lu;
	}

	@RequestMapping("/update")
	@SecurityMapping(level = SecurityLevel.ADMIN)
	@ResponseBody
	public boolean update(@RequestParam("orgUserName") String orgUserName,
			LdapUser user,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser sessionUser) {
		userService.updateUser(user);
		indexService.updateUserVisible(user.getDn(), user.isVisible());
		sender.sendUpdateMessage(user.getDn());
		return true;
	}

	@RequestMapping("getDomain")
	@SecurityMapping(level = SecurityLevel.ADMIN)
	@ResponseBody
	public String getFirstDomain(@RequestParam("dn") String dn) {
		String decode = LdapUtils.decode(dn);
		if (!LdapUtils.isOrgSub(decode)) {
			return "";
		}
		LdapOrg org=orgService.getOrgByDN(LdapUtils.getTeamDN(decode));
		if(org==null){
			return "";
		}
		if(!org.getIsCoreMail()||CommonUtils.isNull(org.getDomains())){
			return "";
		}
		return CommonUtils.first(org.getDomains());
	}
	
	@RequestMapping("batchUpdateUserValidate")
	@SecurityMapping(level=SecurityLevel.ADMIN)
	@ResponseBody
	public JsonResult validate(@RequestParam("dn") String dn,@RequestParam("userDns[]") String[] userDns){
		String decodeDN=LdapUtils.decode(dn);
		String[] userDecodeDNs=LdapUtils.decode(userDns);
		int allCount=userDecodeDNs.length;
		int coreMailCount=0;
		if(LdapUtils.isOrgDN(decodeDN)){
			LdapOrg org=orgService.getOrgByDN(decodeDN);
			if(org.getIsCoreMail()){
				for(String decodeUserDN:userDecodeDNs){
					LdapUser user=userService.getUserByDN(decodeUserDN);
					if(CommonUtils.isEqualsContain(org.getDomains(), EmailUtils.getDomain(user.getCstnetId()))){
						coreMailCount++;
					}
				}
			}
		}
		JSONObject obj=new JSONObject();
		obj.put("coreMailCount",coreMailCount);
		obj.put("allCount", allCount);
		return new JsonResult(obj);
	}
	@RequestMapping("batchUpdateSubmit")
	@ResponseBody
	public JsonResult batchUpdateSubmit(LdapUser user,@RequestParam("dn") String dn,@RequestParam("userDns[]") String[] userDns,@RequestParam("needUpdate[]")String[] needUpdate){
		String[] decodeUserDNs=LdapUtils.decode(userDns);
		for(String decodeUserDN:decodeUserDNs){
			user.setDn(decodeUserDN);
			userService.batchUpdateUser(user,needUpdate);
			if(CommonUtils.isEqualsContain(needUpdate, "visible")){
				indexService.updateUserVisible(user.getDn(), user.isVisible());
			}
			sender.sendUpdateMessage(user.getDn());
		}
		return new JsonResult();
	}

}
