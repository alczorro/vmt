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
package net.duckling.vmt.service.mq;

import java.util.ArrayList;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.api.domain.message.MQMoveDepartMessage;
import net.duckling.vmt.api.domain.message.MQSwitchPhoneGroupMessage;
import net.duckling.vmt.api.domain.message.MQSwitchPhoneOrgMessage;
import net.duckling.vmt.common.adapter.VmtGroup2LdapGroupAdapter;
import net.duckling.vmt.common.adapter.VmtOrg2LdapOrgAdapter;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IDepartmentService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-8-5
 */
@Service
public class MQMessageSenderExt extends MQMessageSender{
	
	private static final Logger LOGGER=Logger.getLogger(MQMessageSenderExt.class);
	
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IDepartmentService deptService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGroupService groupService;

	public void sendMoveUserMessage(String[] dns, String destDn) {
		String teamDn = LdapUtils.getTeamDN(destDn);
		LdapDepartment dept = null;
		LdapOrg org = orgService.getOrgByDN(teamDn);
		// 目标是部门
		if (new DistinguishedName(destDn).size() > 2) {
			dept = deptService.getDepartByDN(destDn);
		}
		super.sendMoveUserMessage(
				userService.searchUsersByUmtId(teamDn, extractUmtId(dns)),
				org,dept);
	}
	public MQMoveDepartMessage getMoveDeptMessage(String decodeOrgDn, String decodeDestDn, boolean containSelf){
		LdapOrg org = orgService.getOrgByDN(LdapUtils.getTeamDN(decodeOrgDn));
		LdapDepartment orgDept = deptService.getDepartByDN(decodeOrgDn);
		LdapDepartment destDept = null;
		if (new DistinguishedName(decodeDestDn).size() > 2) {
			destDept = deptService.getDepartByDN(decodeDestDn);
		}
		return getMoveDeptMessage(orgDept, containSelf, destDept, org);
	}

	public String[] extractUmtId(String dns[]) {
		String[] umtIds = new String[dns.length];
		int index = 0;
		for (String dn : dns) {
			umtIds[index++] = LdapUtils.getLastValue(dn);
		}
		return umtIds;
	}


	/**
	 * @param umtId
	 * @param dn
	 */
	public void sendCreateUserMessage(String[] umtIds, String destDn) {
		LdapOrg org=null;
		LdapGroup group=null;
		LdapDepartment dept=null;
		if(LdapUtils.isOrgDN(destDn)){
			org=orgService.getOrgByDN(destDn);
		}else if(LdapUtils.isGroupDN(destDn)){
			group=groupService.getGroupByDN(destDn);
		}else if(LdapUtils.isDeptDN(destDn)){
			org=orgService.getOrgByDN(LdapUtils.getTeamDN(destDn));
			dept=deptService.getDepartByDN(destDn);
		}
		super.sendCreateUserMessage(userService.searchUsersByUmtId(destDn, umtIds), org,dept,group);
	}
	public void sendCreateUserMessage(String[] cstnetIds,boolean[] flags,String destDn){
		List<String> result=new ArrayList<String>();
		int index=0;
		for(boolean flag:flags){
			if(flag){
				result.add(cstnetIds[index].split(KEY.GLOBAL_DATA_SPLIT)[0]);
			}
			index++;
		}
		if(CommonUtils.isNull(result)){
			return;
		}
		List<LdapUser> users=userService.searchUsersByCstnetId(destDn, result.toArray(new String[result.size()]));
		LdapOrg org=null;
		LdapGroup group=null;
		LdapDepartment dept=null;
		if(LdapUtils.isOrgDN(destDn)){
			org=orgService.getOrgByDN(destDn);
		}else if(LdapUtils.isGroupDN(destDn)){
			group=groupService.getGroupByDN(destDn);
		}else if(LdapUtils.isDeptDN(destDn)){
			org=orgService.getOrgByDN(LdapUtils.getTeamDN(destDn));
			dept=deptService.getDepartByDN(destDn);
		}
		super.sendCreateUserMessage(users,org,dept,group);
	}


	/**
	 * 删除dn下的所有东西
	 * @param decodeDN
	 */
	public void sendUnbindMessage(String dn) {
		if(LdapUtils.isGroupDN(dn)){
			LdapGroup group=groupService.getGroupByDN(dn);
			super.sendDeleteGroupMessage(group);
		}else if(LdapUtils.isOrgDN(dn)){
			LdapOrg org=orgService.getOrgByDN(dn);
			super.sendDeleteOrgMessage(org);
		}else if(LdapUtils.isDeptDN(dn)){
			LdapOrg org=orgService.getOrgByDN(LdapUtils.getTeamDN(dn));
			LdapDepartment dept=deptService.getDepartByDN(dn);
			super.sendDeleteDepartMessage(org,dept);
		}else if(LdapUtils.isUserDN(dn)){
			LdapOrg org=null;
			LdapGroup group=null;
			LdapDepartment dept=null;
			DistinguishedName name=new DistinguishedName(dn);
			String pdn=name.getPrefix(name.size()-1).toString();
			LdapUser user=userService.getUserByDN(dn);
			if(!LdapUser.STATUS_ACTIVE.equals(user.getStatus())){
				LOGGER.info("this user["+user.getCstnetId()+"] is unactive");
				return;
			}
			if(LdapUtils.isOrgDN(pdn)){
				org=orgService.getOrgByDN(pdn);
			}else if(LdapUtils.isGroupDN(pdn)){
				group=groupService.getGroupByDN(pdn);
			}else if(LdapUtils.isDeptDN(pdn)){
				org=orgService.getOrgByDN(LdapUtils.getTeamDN(pdn));
				dept=deptService.getDepartByDN(pdn);
			}
			super.sendDeleteUserMessage(user,org,dept,group);
		}
	}
	/**
	 * @param dns
	 */
	public void sendUnbindMessage(String[] dns) {
		for(String dn:dns){
			sendUnbindMessage(dn);
		}
	}

	public void sendUpdateMessage(String dn){
		sendUpdateMessage(dn,null);
	}
	public void setTelephoneNone(String userDn,LdapUser user){
		if(LdapUtils.isOrgSub(userDn)){
			LdapOrg org=orgService.getOrgByDN(LdapUtils.getTeamDN(userDn));
			if(org.isHideMobile()){
				user.setTelephone(null);
			}
		}else{
			LdapGroup group=groupService.getGroupByDN(LdapUtils.getTeamDN(userDn));
			if(group.isHideMobile()){
				user.setTelephone(null);
			}
		}
	}
	/**
	 * @param decodeDn
	 */
	public void sendUpdateMessage(String dn,String beforeName) {
		if(LdapUtils.isGroupDN(dn)){
			LdapGroup group=groupService.getGroupByDN(dn);
			List<LdapUser> users=userService.searchUsersByUmtId(dn, group.getAdmins());
			super.sendUpdateGroup(group,users,beforeName);
		}else if(LdapUtils.isOrgDN(dn)){
			LdapOrg org=orgService.getOrgByDN(dn);
			List<LdapUser> users=userService.searchUsersByUmtId(dn, org.getAdmins());
			super.sendUpdateOrg(org,users,beforeName);
		}else if(LdapUtils.isDeptDN(dn)){
			LdapDepartment dept=deptService.getDepartByDN(dn);
			super.sendUpdateDept(dept,beforeName);
		}else if(LdapUtils.isUserDN(dn)){
			LdapUser user=userService.getUserByDN(dn);
			if(!LdapUser.STATUS_ACTIVE.equals(user.getStatus())){
				LOGGER.info("this user["+user.getCstnetId()+"] is unactive");
				return;
			}
			setTelephoneNone(dn,user);
			super.sendUpdateUser(user);
		}
	}
	public void sendCreateGroupMessage(LdapGroup group){
		LdapUser user=CommonUtils.first(userService.searchUsersByUmtId(group.getDn(),new String[]{ group.getCreator()}));
		super.sendCreateGroupMessage(group, user);
	}

	/**
	 * @param org
	 */
	public void sendCreateOrgMessage(LdapOrg org) {
		LdapUser user=CommonUtils.first(userService.searchUsersByUmtId(org.getDn(),new String[]{ org.getCreator()}));
		super.sendCreateOrgMessage(org, user);
		
	}

	/**
	 * @param orgDN
	 */
	public void sendRefreshMessage(String dn) {
		if(LdapUtils.isGroupDN(dn)){
			LdapGroup group=groupService.getGroupByDN(dn);
			super.sendRefreshGroupMessage(group);
		}else if(LdapUtils.isOrgDN(dn)){
			LdapOrg org=orgService.getOrgByDN(dn);
			super.sendRefreshOrgMessage(org);
		}
		
	}
	public void sendOpenDchatMessage(String dn,boolean isOpen){
		if(LdapUtils.isOrgDN(dn)){
			super.sendOpenOrgDchatMessage(orgService.getOrgByDN(dn),isOpen);
		}else if(LdapUtils.isGroupDN(dn)){
			super.sendOpenGroupDchatMessage(groupService.getGroupByDN(dn),isOpen);
		}
	}
	
	public void sendToggleMobileMessage(String dn, boolean visible) {
		if(LdapUtils.isGroupDN(dn)){
			LdapGroup group=groupService.getGroupByDN(dn);
			if (group!=null){
				VmtGroup vmtGroup = VmtGroup2LdapGroupAdapter.convert(group);
				MQSwitchPhoneGroupMessage message = new MQSwitchPhoneGroupMessage(vmtGroup, visible);
				send(message);
			}
		}else if(LdapUtils.isOrgDN(dn)){
			LdapOrg org=orgService.getOrgByDN(dn);
			if (org!=null){
				VmtOrg vmtOrg = VmtOrg2LdapOrgAdapter.convert(org);
				MQSwitchPhoneOrgMessage message = new MQSwitchPhoneOrgMessage(vmtOrg, visible);
				send(message);
			}
		}
	}

	

}
