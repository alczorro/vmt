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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.RenderUtils;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.SimpleEmail;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.domain.message.VmtMsgColumns;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.IVmtMessageService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.service.thread.EmailSendJob;
import net.duckling.vmt.service.thread.JobThread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2013-9-22
 */
@Controller
@RequestMapping("/user/search/group")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class SearchGroupController {
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IUserService userService;
	@Autowired
	private VmtConfig config;
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private MQMessageSenderExt msgSender;
	@Autowired
	private IVmtMessageService msgService;
	/**
	 * 查询群组
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(){
		ModelAndView mv=new ModelAndView("user/searchGroup");
		return mv;
	}
	
	/**
	 * 查询群组，私密无法查到，完全公开能直接加入，完全需审核需要审核加入
	 * @param keyword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params={"keyword"}) 
	public List<LdapGroup> searchGroup(@RequestParam("keyword")String keyword){
		if(CommonUtils.isNull(keyword)){
			return null;
		}
		return groupService.searchGroupByKeyword(keyword.trim());
	}
	@ResponseBody
	@RequestMapping(params="func=add")
	public JsonResult addToGroup(
			@RequestParam("dn")String dn,
			@RequestParam("privilege")String privilege,
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser sessionUser){
		String decodeDN=LdapUtils.decode(dn);
		//不是团队的dn
		if(!LdapUtils.isGroupDN(decodeDN)){
			return new JsonResult("团队未找到");
		}
		LdapGroup group=groupService.getGroupByDN(decodeDN);
		//权限有变更，或者找不到该权限
		if(!privilege.equals(group.getPrivilege())){
			return new JsonResult("权限未找到");
		}
		boolean[] flag=null;
		LdapUser user=userService.searchUserByUmtId(group.getDn(), sessionUser.getUserInfo().getUmtId());
		if(user!=null){
			return new JsonResult("您已经提交申请或者已经加入此团队,请耐心等待");
		}
		JsonResult result=new JsonResult();
		if(LdapGroup.PRIVILEGE_ALL_OPEN.equals(privilege)){
			flag=userService.addUserToNodeUsed(dn, new String[]{sessionUser.getUserInfo().getUmtId()}, LdapUser.STATUS_ACTIVE, false);
			user=userService.searchUserByUmtId(group.getDn(), sessionUser.getUserInfo().getUmtId());
			msgSender.sendCreateUserMessage(new String[]{sessionUser.getUserInfo().getUmtId()}, dn);
			result.setDesc("加入群组成功!");
		}else if(LdapGroup.PRIVILEGE_OPEN_REQUIRED.equals(privilege)){
			flag=userService.addUserToNodeUsed(dn, new String[]{sessionUser.getUserInfo().getUmtId()}, LdapUser.STATUS_APPLY, false);
			user=userService.searchUserByUmtId(group.getDn(), sessionUser.getUserInfo().getUmtId());
			send2Admins(group,user);
			result.setDesc("群组加入申请提交成功，请等待管理员审核。");
		}
		indexService.addIndexByUser(group.getDn(), flag, user);
		return result;
	}
	private void send2Admins(LdapGroup group,LdapUser user){
		//发送消息
		VmtMessage vmtMsg=new VmtMessage();
		
		vmtMsg.setMsgTo(VmtMessage.MSG_TO_ADMIN);
		vmtMsg.setEntryId(group.getDn());
		vmtMsg.setTeamDN(group.getDn());
		vmtMsg.setMsgStatus(VmtMessage.MSG_STATUS_NEED_REED);
		vmtMsg.setMsgType(VmtMessage.MSG_TYPE_USER_APPLY_TEMP);
		if(!msgService.isEquals(vmtMsg)){
			vmtMsg.setTeamName(group.getName());
			msgService.insertMsg(vmtMsg);
			List<VmtMsgColumns> columns=new ArrayList<VmtMsgColumns>();
			
			columns.add(new VmtMsgColumns("applyName", user.getName(), vmtMsg.getId()));
			columns.add(new VmtMsgColumns("applyDN",user.getDn(),vmtMsg.getId()));
			columns.add(new VmtMsgColumns("applyCstnetId",user.getCstnetId(),vmtMsg.getId()));
			msgService.insertMsgColumns(columns);
		}
		//发送邮件
		List<LdapUser> users=userService.searchUsersByUmtId(group.getDn(), group.getAdmins());
		if(CommonUtils.isNull(users)){
			return; 
		}
		for(LdapUser admin:users){
			Map<String,Object> param=new HashMap<String,Object>();
			param.put("${userName}",user.getName());
			param.put("${email}",user.getCstnetId());
			param.put("${teamName}",group.getName());
			param.put("${loginName}",admin.getCstnetId());
			param.put("${baseUrl}",config.getMyBaseUrl());
			param.put("${activeUrl}",config.getMyBaseUrl()+"/user/active/apply/"+user.getRandom()+"/"+user.getUmtId()+"?teamDn="+LdapUtils.encode(group.getDn()));
			EmailSendJob job=new EmailSendJob(new SimpleEmail(admin.getCstnetId(), RenderUtils.getHTML(param, RenderUtils.APPLY_ADD), "有新的用户需要审核"));
			JobThread.addJobThread(job);
		}
	}
}
