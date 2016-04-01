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
package net.duckling.vmt.dao.impl;

import java.util.List;

import javax.naming.directory.SearchControls;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.ldap.LdapMappingResolver;
import net.duckling.vmt.common.ldap.LdapObjectClass;
import net.duckling.vmt.dao.IOrgDAO;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.SearchOrgDomainMappingCondition;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.exception.DBErrorCode;
import net.duckling.vmt.exception.LdapOpeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2013-4-26
 */
@Component
@SuppressWarnings("unchecked")
public class OrgDAOLdapImpl implements IOrgDAO{
	private static final String BASE_DN="ou=org";
	@Autowired
	private LdapTemplate ldapTemplate;
	private LdapMappingResolver<LdapOrg> resolver=new LdapMappingResolver<LdapOrg>(LdapOrg.class);
	
	@Override
	public boolean insert(LdapOrg org){
			if(!isExists(org.getSymbol())){
				ldapTemplate.bind(resolver.build(org,BASE_DN));
				return true;
			}else{
				throw new LdapOpeException(DBErrorCode.NODE_EXISTS, org.getSymbol());
			}
	}
	@Override
	public boolean isExists(String orgSymbol) {
		ldapTemplate.setIgnoreNameNotFoundException(true);
		List<?> result=ldapTemplate.search(resolver.getDN(orgSymbol, BASE_DN), getBaseFilter().encode(), SearchControls.OBJECT_SCOPE, new ContextMapper() {
			@Override
			public Object mapFromContext(Object arg0) {
				return null;
			}
		});
		return !CommonUtils.isNull(result);
	}
	
	@Override
	public List<LdapOrg> getMyOrgs(String umtId) {
		AndFilter filter = new AndFilter();
 		filter.and(new EqualsFilter(LdapObjectClass.OBJECTCLASS, LdapObjectClass.CLASS_VMT_USER));
		filter.and(new EqualsFilter("vmt-umtId",umtId));
		filter.and(new NotFilter(new EqualsFilter("vmt-status", LdapUser.STATUS_REFUSE)));
		List<LdapOrg> result= ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.SUBTREE_SCOPE,new ContextMapper(){
			@Override
			public LdapOrg mapFromContext(Object arg0) {
				DirContextAdapter adap=(DirContextAdapter)arg0;
				return getOrgByDN(adap.getDn().getPrefix(2).toString());
			}
		});
		return result;
	}
	@Override
	public List<LdapOrg> getOrgsByOrgDomainMapping(
			SearchOrgDomainMappingCondition od) {
		if(od==null||od.isDefaulCondition()){
			return getThirdPartyOrgs(null);
		}else{
			AndFilter and=getBaseFilter();
			if(!CommonUtils.isNull(od.getCas())){
				if("true".equals(od.getCas())){
					and.and(new EqualsFilter("vmt-is-cas","true"));
				}else if("false".equals(od.getCas())){
					and.and(new NotFilter(new EqualsFilter("vmt-is-cas","true")));
				}
				
			}
			if(!CommonUtils.isNull(od.getCoreMail())){
				if("true".equals(od.getCoreMail())){
					and.and(new EqualsFilter("vmt-is-coremail","true"));
				}else if("false".equals(od.getCoreMail())){
					and.and(new NotFilter(new EqualsFilter("vmt-is-coremail","true")));
				}
			}
			if(!CommonUtils.isNull(od.getDomain())){
				and.and(new LikeFilter("vmt-domain", "*"+CommonUtils.trim(od.getDomain())+"*"));
			}
			if(!CommonUtils.isNull(od.getName())){
				and.and(new LikeFilter("vmt-name", "*"+CommonUtils.trim(od.getName())+"*"));
			}
			return ldapTemplate.search(BASE_DN,and.encode(),SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
		}
		
	}
	@Override
	public LdapOrg getOrgByDN(String dn) {
		return resolver.convert((DirContextAdapter) ldapTemplate.lookup(dn));
	}
	@Override
	public List<LdapOrg> getAdminOrgs(String umtId) {
		AndFilter filter = getBaseFilter();
		filter.and(new EqualsFilter("vmt-admin",umtId));
		return ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	@Override
	public String getThirdPartyOrg(String orgId,String from) {
		LdapOrg org=getOrgBySymboleAndFrom(orgId,from);
		if(org==null){
			return null;
		}
		return org.getDn();
	}
	private AndFilter getBaseFilter(){
		 return new AndFilter().and(new EqualsFilter(LdapObjectClass.OBJECTCLASS, LdapObjectClass.CLASS_VMT_ORG));
	}
	@Override
	public List<LdapOrg> getAllOrgs() {
		return ldapTemplate.search(BASE_DN, getBaseFilter().encode(),SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	private LdapOrg getOrgBySymboleAndFrom(String orgSymbol,String from){
		AndFilter filter = getBaseFilter();
		if(!CommonUtils.isNull(from)){
			filter.and(new EqualsFilter(KEY.DB_VMT_FROM,from));
		}
		filter.and(new EqualsFilter("vmt-symbol",orgSymbol));
		List<LdapOrg> dns= ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
		return CommonUtils.first(dns);
	}
	@Override
	public LdapOrg getOrgBySymbol(String orgSymbol) {
		return getOrgBySymboleAndFrom(orgSymbol,null);
	}
	@Override
	public List<LdapOrg> getThirdPartyOrgs(String from) {
		AndFilter filter = getBaseFilter();
		if(!CommonUtils.isNull(from)){
			filter.and(new EqualsFilter(KEY.DB_VMT_FROM,from));
		}
		return ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	@Override
	public List<LdapOrg> getOrgBySymbol(List<VmtIndex> orgIndex) {
		OrFilter or=new OrFilter();
		for(VmtIndex index:orgIndex){
			or.or(new EqualsFilter("vmt-symbol",index.getSymbol()));
		}
		return ldapTemplate.search(BASE_DN, or.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	@Override
	public List<LdapOrg> getOrgsByMemberVisible() {
		AndFilter filter=getBaseFilter();
		filter.and(new EqualsFilter("vmt-member-visible", "true"));
		
		return ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	@Override
	public LdapOrg getOrgByDomain(String domain) {
		AndFilter filter=getBaseFilter();
		filter.and(new EqualsFilter("vmt-domain", domain));
		List<LdapOrg> orgs=ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
		return CommonUtils.first(orgs);
	}
	@Override
	public int getOrgCount() {
		AndFilter filter=getBaseFilter();
		List<Boolean> orgs = ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,new ContextMapper(){
			public Object mapFromContext(Object arg0) {
				return Boolean.TRUE;
			}
			
		});
		return orgs.size();
	}
	
	@Override
	public int getOrgCountWithDChat() {
		AndFilter filter=getBaseFilter();
		filter.and(new EqualsFilter("vmt-open-dchat","true"));
		List<Boolean> orgs = ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,new ContextMapper(){
			public Object mapFromContext(Object arg0) {
				return Boolean.TRUE;
			}
			
		});
		return orgs.size();
	}
	
}
