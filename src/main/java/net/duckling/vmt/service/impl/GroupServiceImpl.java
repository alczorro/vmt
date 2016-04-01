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


import java.util.Iterator;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.util.CharUtils;
import net.duckling.vmt.dao.IGroupDAO;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtAppSwitchService;
import net.duckling.vmt.service.IVmtIndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-5-2
 */
@Service
public class GroupServiceImpl implements IGroupService{
	@Autowired
	private IGroupDAO groupDAO;
	@Autowired
	private IUserService userService;
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private IVmtAppSwitchService switchService;
	
	private static final int PASS_LENGTH=20;

	@Override
	public boolean createGroup(LdapGroup group,boolean addSelf){
		group.setCurrentDisplay(group.getName());
		group.setPassword(CharUtils.random(PASS_LENGTH));
		boolean flag= groupDAO.insert(group);
		if(flag){
			switchService.ifNotExitsThenInsert(group.getDn());
		}
		if(flag&&addSelf){
			userService.addUserToNodeUsed(group.getDn().toString(), new String[]{group.getCreator()},LdapUser.STATUS_ACTIVE,true);
		}
		return flag;
	}
	@Override
	public List<LdapGroup> getThirdPartyGroups(String from) {
		return groupDAO.getThirdPartyGroups(from);
	}
	@Override
	public boolean isSymbolUsed(String groupName){
		return groupDAO.isSymbolUsed(groupName);
	}
	@Override
	public List<LdapGroup> getMyGroupsAll(String umtId) {
		if(CommonUtils.isNull(umtId)){
			return groupDAO.getAllGroups();
		}
		List<VmtIndex> groupIndex=indexService.selectIndexByTypeAll(umtId,VmtIndex.TYPE_GROUP);
		if(CommonUtils.isNull(groupIndex)){
			return null;
		}
		return groupDAO.getGroupBySymbol(groupIndex);
	}
	@Override
	public List<LdapGroup> getMyGroups(String umtId) {
		if(CommonUtils.isNull(umtId)){
			return groupDAO.getAllGroups();
		}
		List<VmtIndex> groupIndex=indexService.selectIndexByType(umtId,VmtIndex.TYPE_GROUP);
		if(CommonUtils.isNull(groupIndex)){
			return null;
		}
		List<LdapGroup> groups=groupDAO.getGroupBySymbol(groupIndex);
		for(Iterator<LdapGroup> it=groups.iterator();it.hasNext();){
			LdapGroup group=it.next();
			if(!group.canLook(umtId)){
				it.remove();
			}
		}
		return  groups;
	}
	@Override
	public List<LdapGroup> getAdminGroups(String umtId) {
		if(CommonUtils.isNull(umtId)){
			return groupDAO.getAllGroups();
		}
		return groupDAO.getAdminGroups(umtId);
	}
	@Override
	public List<LdapGroup> getThirdPartyGroupByUmtId(String from, String umtId) {
		return groupDAO.getThirdPartyGroupByUmtId(from,umtId);
	}
	@Override
	public boolean isNameUsed(String groupName) {
		return groupDAO.isNameUsed(groupName);
	}
	@Override
	public LdapGroup getGroupBySymbol(String symbol,String from) {
		return groupDAO.getGroupBySymbol(symbol,from);
	}
	@Override
	public LdapGroup getGroupByDN(String decodeDN) {
		return groupDAO.getGroupByDN(decodeDN);
	}
	@Override
	public void deleteAllMember(String dn) {
		List<LdapUser> users=userService.searchUsersByDN(dn, null);
		if(!CommonUtils.isNull(users)){
			userService.unbind(getUserDN(users));
		}
		attrService.update(dn, "vmt-count", "0");
	}
	private String[] getUserDN(List<LdapUser> users){
		String[] usersDN=new String[users.size()];
		int index=0;
		if(CommonUtils.isNull(users)){
			return usersDN;
		}
		for(LdapUser user:users){
			usersDN[index++]=user.getDn();
		}
		return usersDN;
	}
	@Override
	public List<LdapGroup> getGroupsByMemberVisible() {
		return groupDAO.getGroupsByMemberVisible();
	}
	@Override
	public List<LdapGroup> searchGroupByKeyword(String keyword) {
		return groupDAO.searchGroupByKeyword(keyword);
	}
	@Override
	public int getGroupCount() {
		return groupDAO.getGroupCount();
	}
}
