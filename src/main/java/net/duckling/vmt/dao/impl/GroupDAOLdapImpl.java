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

import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.SearchControls;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.ldap.LdapMappingResolver;
import net.duckling.vmt.common.ldap.LdapObjectClass;
import net.duckling.vmt.dao.IGroupDAO;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapGroup;
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
public class GroupDAOLdapImpl implements IGroupDAO{
	private static final String BASE_DN="ou=group";
	@Autowired
	private LdapTemplate ldapTemplate;
	private LdapMappingResolver<LdapGroup> resolver=new LdapMappingResolver<LdapGroup>(LdapGroup.class);
	
	
	@Override
	public boolean insert(LdapGroup group){
		if(!isSymbolUsed(group.getSymbol())){
			ldapTemplate.bind(resolver.build(group, BASE_DN));
			return true;
		}else{
			throw new LdapOpeException(DBErrorCode.NODE_EXISTS, group.getName());
		}
	}
	@Override
	public boolean isSymbolUsed(String groupSymbol) {
		ldapTemplate.setIgnoreNameNotFoundException(true);
		AndFilter filter=getBaseFilter();
		filter.and(new EqualsFilter("vmt-symbol",groupSymbol));
		List<?> result=ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE, new ContextMapper() {
			@Override
			public Object mapFromContext(Object arg0) {
				return null;
			}
		});
		return !CommonUtils.isNull(result);
	}
	@Override
	public boolean isNameUsed(String groupName) {
		ldapTemplate.setIgnoreNameNotFoundException(true);
		AndFilter filter=getBaseFilter();
		filter.and(new EqualsFilter("vmt-name",groupName));
		List<?> result=ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE, new ContextMapper() {
			@Override
			public Object mapFromContext(Object arg0) {
				return null;
			}
		});
		return !CommonUtils.isNull(result);
	}
	
	@Override
	public List<LdapGroup> getMyGroups(String umtId) {
		AndFilter filter = new AndFilter();
 		filter.and(new EqualsFilter(LdapObjectClass.OBJECTCLASS, LdapObjectClass.CLASS_VMT_USER));
		filter.and(new EqualsFilter("vmt-umtId",umtId));
		filter.and(new NotFilter(new EqualsFilter("vmt-status", LdapUser.STATUS_REFUSE)));
		return ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.SUBTREE_SCOPE,new ContextMapper(){
			@Override
			public LdapGroup mapFromContext(Object arg0) {
				DirContextAdapter adap=(DirContextAdapter)arg0;
				return getGroupByDN(adap.getDn().getPrefix(2).toString());
			}
		});
	}
	@Override
	public List<LdapGroup> searchGroupByKeyword(String keyword) {
		if(CommonUtils.isNull(keyword)){
			return new ArrayList<LdapGroup>();
		}
		AndFilter filter=getBaseFilter();
		OrFilter privFilter=new OrFilter();
		privFilter.or(new EqualsFilter("vmt-privilege", LdapGroup.PRIVILEGE_ALL_OPEN));
		privFilter.or(new EqualsFilter("vmt-privilege", LdapGroup.PRIVILEGE_OPEN_REQUIRED));
		filter.and(privFilter);
		OrFilter keywordFilter=new OrFilter();
		keywordFilter.or(new LikeFilter("vmt-name","*"+keyword+"*"));
		filter.and(keywordFilter);
		return ldapTemplate.search(BASE_DN, filter.encode(),SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	@Override
	public List<LdapGroup> getGroupBySymbol(List<VmtIndex> indexs) {
		OrFilter or=new OrFilter();
		for(VmtIndex index:indexs){
			or.or(new EqualsFilter("vmt-symbol", index.getSymbol()));
		}
		return ldapTemplate.search(BASE_DN, or.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	@Override
	public LdapGroup getGroupByDN(String groupDN) {
		return resolver.convert((DirContextAdapter) ldapTemplate.lookup(groupDN));
	}
	@Override
	public List<LdapGroup> getAdminGroups(String umtId) {
		return getAdminGroups(umtId,null);
	}
	@Override
	public List<LdapGroup> getGroupsByMemberVisible() {
		AndFilter filter=getBaseFilter();
		filter.and(new EqualsFilter("vmt-member-visible", "true"));
		return ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	private List<LdapGroup> getAdminGroups(String umtId,String from){
		AndFilter filter=getBaseFilter();
 		filter.and(new EqualsFilter("vmt-admin", umtId));
 		if(!CommonUtils.isNull(from)){
 			filter.and(new EqualsFilter(KEY.DB_VMT_FROM, from));
 		}
		return ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	@Override
	public List<LdapGroup> getThirdPartyGroupByUmtId(String umtId, String from) {
		return getAdminGroups(umtId, from);
	}
	private AndFilter getBaseFilter(){
		return new AndFilter().and(new EqualsFilter(LdapObjectClass.OBJECTCLASS, LdapObjectClass.CLASS_VMT_GROUP));
	}
	@Override
	public LdapGroup getGroupBySymbol(String symbol, String from) {
		AndFilter filter=getBaseFilter();
		if(!CommonUtils.isNull(from)){
			filter.and(new EqualsFilter(KEY.DB_VMT_FROM,from));
		}
		filter.and(new EqualsFilter("vmt-symbol",symbol));
		List<LdapGroup> dns= ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
		return CommonUtils.first(dns);
	}
	@Override
	public List<LdapGroup> getAllGroups() {
		return ldapTemplate.search(BASE_DN, getBaseFilter().encode(),SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	@Override
	public List<LdapGroup> getThirdPartyGroups(String from) {
		AndFilter filter=getBaseFilter();
		if(!CommonUtils.isNull(from)){
			filter.and(new EqualsFilter(KEY.DB_VMT_FROM,from));
		}
		return ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,resolver.getContextMapper());
	}
	@Override
	public int getGroupCount() {
		AndFilter filter=getBaseFilter();
		List<Boolean> orgs = ldapTemplate.search(BASE_DN, filter.encode(), SearchControls.ONELEVEL_SCOPE,new ContextMapper(){
			public Object mapFromContext(Object arg0) {
				return Boolean.TRUE;
			}
		});
		return orgs.size();
	}
}
