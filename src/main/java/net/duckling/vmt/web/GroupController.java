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

import javax.servlet.http.HttpServletRequest;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.common.PhoneUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.exception.LdapOpeException;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author lvly
 * @since 2013-5-6
 */
@Controller
@RequestMapping("/user/group")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class GroupController {
	private static final Logger LOGGER=Logger.getLogger(GroupController.class);
	@Autowired
	private IGroupService groupService;
	@Autowired
	private MQMessageSenderExt mqsender;
	@Autowired
	private IVmtIndexService indexService;
	
	/**
	 * 创建群组
	 * @param groupName
	 * @param privilege
	 * @param teamSymbol
	 * @param user
	 * @return
	 */
	@RequestMapping("/create")
	public RedirectView create(
			@RequestParam("teamName")String groupName,
			@RequestParam("privilege")String privilege,
			@RequestParam("teamSymbol")String teamSymbol,
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,
			HttpServletRequest request) {
		LdapGroup group=new LdapGroup();
		group.setCreator(user.getUserInfo().getUmtId());
		group.setPrivilege(privilege);
		group.setOpenDchat(true);
		group.setAdmins(new String[]{user.getUserInfo().getUmtId()});
		group.setName(CommonUtils.trim(groupName));
		group.setSymbol(CommonUtils.trim(teamSymbol.toLowerCase()));
		group.setCurrentDisplay(CommonUtils.trim(groupName));
		try {
			groupService.createGroup(group,true);
		} catch (LdapOpeException e){
			LOGGER.error(e.getMessage(),e);
		}
		indexService.buildAIndexJob(group.getDn());
		mqsender.sendCreateGroupMessage(group);
		String encodeDN=LdapUtils.encode(group.getDn());
		if(PhoneUtils.isMobile(request)){
			return new RedirectView("../mobile/detail?dn="+encodeDN);
		}
		return new RedirectView("../index?dn="+encodeDN);
	}
}
