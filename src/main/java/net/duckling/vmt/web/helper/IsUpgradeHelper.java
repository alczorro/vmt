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
package net.duckling.vmt.web.helper;

import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-8-30
 */
@Service
public class IsUpgradeHelper {
	
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	
	public boolean isUpgraded(String dn){
		long before=0;
		long after=0;
		if(LdapUtils.isGroupDN(dn)){
			LdapGroup group=groupService.getGroupByDN(dn);
			before=group.getFromDate();
			after=group.getToDate();
		}else if(LdapUtils.isOrgDN(dn)){
			LdapOrg org=orgService.getOrgByDN(dn);
			before=org.getFromDate();
			after=org.getToDate();
		}
		return isUpgraded(before,after);
	}
	
	public boolean isUpgraded(long before,long after){
		return true;
//		long now=System.currentTimeMillis();
//		if(before==0&&after==0){
//			return false;
//		}else if(now>before&&now<after){
//			return true;
//		}else if(now>before&&after==0){
//			return true;
//		}else{
//			return false;
//		}
	}
}
