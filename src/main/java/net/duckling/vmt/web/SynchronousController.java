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

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.ISynchronousCoreMailService;
import net.duckling.vmt.service.ISynchronousDDLService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 同步数据
 * @author lvly
 * @since 2013-5-29
 */
@Controller
@RequestMapping("/user/sync")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class SynchronousController {
	@Autowired
	private ISynchronousCoreMailService coreMailSynService;
	@Autowired
	private ISynchronousDDLService ddlSynService;
	@Autowired
	private IOrgService orgSrevice;
	@Autowired
	private IGroupService groupService;
	/**
	 * 全部同步coreMail的信息
	 * @param user
	 * @return
	 */
	@RequestMapping("/coreMail/all")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.SUPER_ADMIN)
	public boolean syncCoreMailAll(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		coreMailSynService.synchrounousAll(user.getUserInfo().getUmtId(),false);
		return true;
	}
	/**
	 * 全部更新，值更新root
	 * */
	@RequestMapping("/coreMail/allRootOnly")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.SUPER_ADMIN)
	public boolean syncCoreMailAllRootOnly(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		coreMailSynService.synchrounousAll(user.getUserInfo().getUmtId(),true);
		return true;
	}
	/**
	 * 单个同步coremail组织结构
	 * @param orgId
	 * @param user
	 * @return
	 */
	@RequestMapping("/coreMail/{orgId}")
	@ResponseBody
	public boolean syncCoreMail(@PathVariable("orgId")String orgId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(orgSrevice.isExists(orgId)){
			LdapOrg org=orgSrevice.getOrgBySymbol(orgId);
			if(user.getIsSuperAdmin()||CommonUtils.isEqualsContain(org.getAdmins(), user.getUserInfo().getUmtId())){
				coreMailSynService.synchrounousByOrgId(orgId, user.getUserInfo().getUmtId());
				return true;
			}
		}
		return false;
		
		
	}
	/**
	 * 同步全部的ddl的团队
	 * @param user
	 * @return
	 */
	@RequestMapping("/ddl/all")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.SUPER_ADMIN)
	public boolean syncDDLAll(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		ddlSynService.synchrounousAll(user.getUserInfo().getUmtId(),false);
		return true;
	}
	/**
	 * 同步全部的ddl的团队
	 * @param user
	 * @return
	 */
	@RequestMapping("/ddl/allRootOnly")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.SUPER_ADMIN)
	public boolean syncDDLAllRootOnly(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		ddlSynService.synchrounousAll(user.getUserInfo().getUmtId(),true);
		return true;
	}
	/**
	 * 同步单个的ddl团队
	 * @param teamCode
	 * @param user
	 * @return
	 */
	@RequestMapping("/ddl/{teamCode}")
	@ResponseBody
	public boolean syncDDLTeam(@PathVariable("teamCode")String teamCode,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(groupService.isSymbolUsed(teamCode)){
			LdapGroup group=groupService.getGroupBySymbol(teamCode, LdapNode.FROM_DDL);
			if(user.getIsSuperAdmin()||CommonUtils.isEqualsContain(group.getAdmins(), user.getUserInfo().getUmtId())){
				ddlSynService.synchrounousAGroup(teamCode, user.getUserInfo().getUmtId(),false);
				return true;
			}
		}
		return false;
	}
	
}
