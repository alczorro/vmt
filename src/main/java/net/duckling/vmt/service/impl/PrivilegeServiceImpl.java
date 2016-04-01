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
package net.duckling.vmt.service.impl;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IPrivilegeService;
import net.duckling.vmt.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-5-13
 */
@Service
public class PrivilegeServiceImpl implements IPrivilegeService {
	@Autowired
	private IAttributeService attributeService;
	@Autowired
	private IUserService userService;
	
	@Override
	public boolean canLookUnConfirm(String dn, String umtId) {
		String[] unConfirmVisible = attributeService.get(dn, "vmt-unconfirm-visible");
		if (!CommonUtils.isNull(unConfirmVisible) && unConfirmVisible[0].equals("true")&&userService.isExistsSubTree(dn, umtId,true)) {
			return true;
		} 
		return isAdmin(dn,umtId);
	}

	@Override
	public boolean canLook(String dn, String umtId) {
		dn=LdapUtils.getTeamDN(dn);
		String[] memberVisible = attributeService.get(dn, "vmt-member-visible");
		if (!CommonUtils.isNull(memberVisible) && memberVisible[0].equals("true")&&userService.isExistsSubTree(dn, umtId,true)) {
			return true;
		} 
		return isAdmin(dn,umtId);
	}
	@Override
	public boolean canAdd(String dn,String umtId) {
		String[] previleges = attributeService.get(dn, "vmt-privilege");
		if(!CommonUtils.isNull(previleges)&&previleges[0].equals(LdapGroup.PRIVILEGE_PRIVATE_ALLOW_ADD)&&userService.isExistsSubTree(dn, umtId,true)){
			return true;
		}
		return isAdmin(dn,umtId);
	}
	@Override
	public  boolean isAdmin(String dn,String umtId){
		String teamDN=LdapUtils.getTeamDN(dn);
		String[] admins = attributeService.get(teamDN, "vmt-admin");
		return CommonUtils.isEqualsContain(admins, umtId);
	}
}
