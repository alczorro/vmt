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
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.exception.LdapOpeException;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 针对组织（Organization）的controller
 * @author lvly
 * @since 2013-4-25
 */
@Controller
@RequestMapping("/user/org")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class OrgController {
	private static final Logger LOGGER=Logger.getLogger(OrgController.class);
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private MQMessageSenderExt sender;
	
	/**
	 * 创建组织
	 * @param orgName 组织机构名称
	 * @param teamSynbol 组织机构标识
	 * @param privilege 默认权限
	 * @param user 登陆用户
	 * @return 跳转到层级视图
	 * */
	@RequestMapping("/create")
	public RedirectView create(
			@RequestParam("teamName")String orgName,
			@RequestParam("teamSymbol")String teamSymbol,
			@RequestParam("privilege")String privilege,
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,
			HttpServletRequest request){
		LdapOrg org=new LdapOrg();
		org.setCreator(user.getUserInfo().getUmtId());
		org.setPrivilege(privilege);
		org.setSymbol(CommonUtils.trim(teamSymbol).toLowerCase());
		org.setAdmins(new String[]{user.getUserInfo().getUmtId()});
		org.setName(CommonUtils.trim((orgName)));
		org.setOpenDchat(true);
		try {
			orgService.createOrg(org,true);
		} catch (LdapOpeException e) {
			LOGGER.error(e.getMessage(),e);
		}
		indexService.buildAIndexJob(org.getDn());
		sender.sendCreateOrgMessage(org);
		String encodeDN=LdapUtils.encode(org.getDn());
		if(PhoneUtils.isMobile(request)){
			return new RedirectView("../mobile/detail?dn="+encodeDN);
		}
		return new RedirectView("../index2?dn="+encodeDN);
	}
	@ResponseBody
	@RequestMapping("getDetail")
	@SecurityMapping(level=SecurityLevel.ADMIN)
	public JsonResult getDetail(@RequestParam("dn")String dn){
		return new JsonResult(orgService.getOrgByDN(LdapUtils.decode(dn)));
	}
	
}
