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
import net.duckling.vmt.dao.IOrgDAO;
import net.duckling.vmt.domain.SearchOrgDomainMappingCondition;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ICommonService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.ISearchService;
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
public class OrgServiceImpl implements IOrgService{
	private static final int PASS_LENGTH=20;
	@Autowired
	private IOrgDAO orgDAO;
	@Autowired
	private IUserService userService;
	@Autowired
	private ISearchService searchService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private IVmtAppSwitchService switchService;
	
	@Override
	public List<LdapOrg> getThirdPartyOrgs(String from) {
		return orgDAO.getThirdPartyOrgs(from);
	}

	@Override
	public boolean createOrg(LdapOrg org,boolean createSelf){
		org.setCurrentDisplay(org.getName());
		org.setPassword(CharUtils.random(PASS_LENGTH));
		org.setAppsOpen(true);
		if(createSelf){
			org.setAdmins(new String[]{org.getCreator()});
		}
		boolean flag=orgDAO.insert(org);
		if(flag){
			switchService.ifNotExitsThenInsert(org.getDn());
		}
		if(flag&&createSelf){
			userService.addUserToNodeUsed(org.getDn().toString(), new String[]{org.getCreator()},LdapUser.STATUS_ACTIVE,true);
		}
		return flag;
	}
	@Override
	public boolean isExists(String orgName){
		return orgDAO.isExists(orgName);
	}
	@Override
	public List<LdapOrg> getMyOrgs(String umtId) {
		if(CommonUtils.isNull(umtId)){
			return orgDAO.getAllOrgs();
		}
		List<VmtIndex> orgIndex=indexService.selectIndexByType(umtId,VmtIndex.TYPE_ORG);
		if(CommonUtils.isNull(orgIndex)){
			return null;
		}
		List<LdapOrg> orgs=orgDAO.getOrgBySymbol(orgIndex);
		if(CommonUtils.isNull(orgs)){
			return orgs;
		}
		for(Iterator<LdapOrg> it=orgs.iterator();it.hasNext();){
			LdapOrg org=it.next();
			if(!org.canLook(umtId)){
				it.remove();
			}
		}
		return  orgs;
	}
	@Override
	public List<LdapOrg> getMyOrgsAll(String umtId) {
		if(CommonUtils.isNull(umtId)){
			return orgDAO.getAllOrgs();
		}
		List<VmtIndex> orgIndex=indexService.selectIndexByTypeAll(umtId,VmtIndex.TYPE_ORG);
		if(CommonUtils.isNull(orgIndex)){
			return null;
		}
		List<LdapOrg> orgs=orgDAO.getOrgBySymbol(orgIndex);
		if(CommonUtils.isNull(orgs)){
			return orgs;
		}
		return  orgs;
	}
	@Override
	public List<LdapOrg> getAdminOrgs(String umtId) {
		if(CommonUtils.isNull(umtId)){
			return orgDAO.getAllOrgs();
		}
		return orgDAO.getAdminOrgs(umtId);
	}
	@Override
	public String getThirdPartyOrg(String orgId,String from) {
		return orgDAO.getThirdPartyOrg(orgId,from);
	}
	
	@Override
	public void deleteAllMember(String orgDN) {
		List<?> result=searchService.searchByListLocalData(orgDN);
		for(Object view:result){
			if(view instanceof LdapDepartment){
				LdapDepartment dept=(LdapDepartment)view;
				commonService.unbind(dept.getDn());
			}else if(view instanceof LdapUser){
				LdapUser user=(LdapUser)view;
				commonService.unbind(user.getDn());
			}
		}
		attrService.update(orgDN, "vmt-count", "0");
	}
	@Override
	public LdapOrg getOrgBySymbol(String orgSymbol){
		return orgDAO.getOrgBySymbol(orgSymbol);
	}
	
	@Override
	public LdapOrg getOrgByDN(String decodeDN) {
		return orgDAO.getOrgByDN(decodeDN);
	}
	@Override
	public List<LdapOrg> getOrgsByMemberVisible() {
		return orgDAO.getOrgsByMemberVisible();
	}
	@Override
	public List<LdapOrg> getOrgsByOrgDomainMapping(
			SearchOrgDomainMappingCondition od) {
		return orgDAO.getOrgsByOrgDomainMapping(od);
	}
	@Override
	public LdapOrg getOrgByDomain(String domain) {
		return orgDAO.getOrgByDomain(domain);
	}

	@Override
	public int getOrgCount() {
		return orgDAO.getOrgCount();
	}

	@Override
	public int getOrgCountWithDChat() {
		return orgDAO.getOrgCountWithDChat();
	}
}
