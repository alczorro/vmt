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

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.IVmtMessageService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
/**
 * 暴露给外部的激活接口，比如邮件
 * @author lvly
 * @since 2013-5-27
 */
@Controller
@RequestMapping("/user/active")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class AcceptInviteController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IAttributeService attributeService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private MQMessageSenderExt sender;
	@Autowired
	private IVmtMessageService msgService;
	/**
	 * 激活用户
	 * @param ramdom 激活码
	 * @param umtId 用户id，如果跟当钱登录用户不匹配，会激活不成功
	 * @return msg.jsp视图
	 * */
	@RequestMapping("/add/{ramdom}/{umtKey}")
	public ModelAndView active(
			@PathVariable("ramdom") String random,
			@PathVariable("umtKey") String umtKey,
			@RequestParam("teamDn") String teamDn,
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		ModelAndView mv=new ModelAndView("user/msg");
		if(!umtKey.equals(user.getUserInfo().getUmtId())){
			mv.addObject("msg","请核实该账户是否正确，该账户并非接受此团队邀请的账户");
			return mv;
		}
		String decodeTeamDN=LdapUtils.decode(teamDn);
		LdapUser ldapUser=userService.checkRandomOK(decodeTeamDN,random,umtKey);
		if(ldapUser==null){
			mv.addObject("msg","未找到用户");
			return mv;
		}
		if(ldapUser.getStatus().equals(LdapUser.STATUS_ACTIVE)){
			mv.addObject("msg","该用户已验证");
			return mv;
		}
		attributeService.update(ldapUser.getDn(), "vmt-status", LdapUser.STATUS_ACTIVE);
		indexService.updateUserStatus(ldapUser.getDn(),LdapUser.STATUS_ACTIVE);
		sender.sendCreateUserMessage(new String[]{ldapUser.getUmtId()}, ldapUser.getRoot());
		VmtMessage msg=new VmtMessage();
		msg.setEntryId(ldapUser.getCstnetId());
		msg.setMsgType(VmtMessage.MSG_TYPE_USER_ADD_TEMP);
		msg.setTeamDN(LdapUtils.getTeamDN(ldapUser.getDn()));
		msgService.updateMsgReaded(msg);
		mv.addObject("msg","验证成功! ");
		return mv;
	}
	@RequestMapping("/apply/{ramdom}/{umtKey}")
	public ModelAndView activeApply(
			@PathVariable("ramdom") String random,
			@PathVariable("umtKey") String umtKey,
			@RequestParam("teamDn") String teamDn,
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		String decodeTeamDN=LdapUtils.decode(teamDn);
		LdapGroup group=groupService.getGroupByDN(decodeTeamDN);
		ModelAndView mv=new ModelAndView("user/msg");
		if(group==null){
			mv.addObject("msg","团队不存在");
			return mv;
		}
		if(!CommonUtils.isEqualsContain(group.getAdmins(),user.getUserInfo().getUmtId())&&!user.getIsSuperAdmin()){
			mv.addObject("msg","您不是该团队的管理员，无权审核");
			return mv;
		}
		LdapUser ldapUser=userService.checkRandomOK(decodeTeamDN,random,umtKey);
		if(ldapUser==null||ldapUser.getStatus().equals(LdapUser.STATUS_ACTIVE)){
			mv.addObject("msg","邀请链接有误，验证失败");
			return mv;
		}
		attributeService.update(ldapUser.getDn(), "vmt-status", LdapUser.STATUS_ACTIVE);
		indexService.updateUserStatus(ldapUser.getDn(),LdapUser.STATUS_ACTIVE);
		sender.sendCreateUserMessage(new String[]{ldapUser.getUmtId()}, ldapUser.getRoot());
		mv.addObject("msg","验证成功!");
		return mv;
	}

}
