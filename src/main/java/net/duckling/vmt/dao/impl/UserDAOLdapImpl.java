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

import javax.naming.InvalidNameException;
import javax.naming.directory.SearchControls;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.ldap.LdapMappingResolver;
import net.duckling.vmt.common.ldap.LdapObjectClass;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.dao.IUserDAO;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.ldap.LdapUser;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2013-5-3
 */
@Component
@SuppressWarnings("unchecked")
public class UserDAOLdapImpl implements IUserDAO{
	private static final Logger LOGGER=Logger.getLogger(UserDAOLdapImpl.class);

	private LdapMappingResolver<LdapUser> resolver=new LdapMappingResolver<LdapUser>(LdapUser.class);
	@Autowired
	private LdapTemplate ldapTemplate;
	@Override
	public boolean[] addUserToNode(String dn, List<LdapUser> users){
		boolean results[]=new boolean[users.size()];
		int index=0;
		String pdn=LdapUtils.getTeamDN(dn);
		for(LdapUser user:users){
			if(user==null||isExistsSubTree(pdn,user.getUmtId(),true)){
				results[index++]=false;
			}else{
				ldapTemplate.bind(resolver.build(user,dn));
				results[index++]=true;
			}
		}
		return results;
	}
	@Override
	public boolean isExistsSubTree(String pdn,String umtId,boolean expectRefuse){
		return isExists(pdn, umtId, SearchControls.SUBTREE_SCOPE, expectRefuse);
	}
	@Override
	public boolean isUserUsedInOrg(String umtId) {
		return isExists(KEY.DB_ORG_BASE_DN, umtId, SearchControls.SUBTREE_SCOPE, true);
	}
	private boolean isExists(String pdn,String umtId,int searchScope, boolean expectRefuse){
		ldapTemplate.setIgnoreNameNotFoundException(true);
		AndFilter filter = getBaseAndFilter();
		if(!CommonUtils.isNull(umtId)){
			 filter.and(new EqualsFilter(KEY.DB_UMTID, umtId));
		}
	    if(expectRefuse){
	    	 filter.and(new NotFilter(new EqualsFilter(KEY.DB_STATUS, LdapUser.STATUS_REFUSE)));
	    }
		List<?> result=ldapTemplate.search(pdn, filter.encode(), searchScope, new ContextMapper() {
			@Override
			public Object mapFromContext(Object arg0) {
				return new Object();
			}
		});
		return !CommonUtils.isNull(result);
	}
	@Override
	public boolean isExistLevel(String dn, String umtId) {
		return isExists(dn, umtId, SearchControls.ONELEVEL_SCOPE,false);
	}
	@Override
	public List<LdapUser> searchUserAll() {
		AndFilter filter=getBaseAndFilter();
		return ldapTemplate.search(KEY.DB_EMPTY, filter.encode(),SearchControls.SUBTREE_SCOPE, resolver.getContextMapper());
	}

	@Override
	public List<LdapUser> searchUserByAll(String[] scopes,boolean[] canSee,boolean isAdmin) {
		AndFilter filter=getBaseAndFilter();
		OrFilter orFilter=new OrFilter();
		addToOr(orFilter,scopes,canSee);
		if(!isAdmin){
			filter.and(new NotFilter(new EqualsFilter("vmt-visible", "false")));
		}
		filter.and(orFilter);
		return ldapTemplate.search(getBaseDN(scopes), filter.encode(),SearchControls.SUBTREE_SCOPE, resolver.getContextMapper());
	}
	private void addToOr(OrFilter orFilter,String[] scopes,boolean[] canSee){
		int index=0;
		for(String scope:scopes){
			String teamDN=LdapUtils.getTeamDN(scope);
			if(canSee[index++]){
				orFilter.or(new EqualsFilter(KEY.DB_VMT_ROOT, teamDN));
			}else{
				AndFilter filter=new AndFilter();
				filter.and(new EqualsFilter(KEY.DB_STATUS, Boolean.TRUE.toString()));
				filter.and(new EqualsFilter(KEY.DB_VMT_ROOT, teamDN));
				orFilter.or(filter);
			}
		}
	}
	@Override
	public List<LdapUser> searchUserByDN(String dn,String expectUmtId) {
		AndFilter filter=getBaseAndFilter();
		if(!CommonUtils.isNull(expectUmtId)){
			filter.and(new NotFilter(new EqualsFilter(KEY.DB_UMTID, expectUmtId)));
		}
		return ldapTemplate.search(dn, filter.encode(), SearchControls.SUBTREE_SCOPE,resolver.getContextMapper());
	}

	@Override
	public List<LdapUser> searchUsersByLetter(String letter, String[] scopes,boolean[] canSee,boolean isAdmin) {
		AndFilter filter=getBaseAndFilter();
		OrFilter orFilter=new OrFilter();
		addToOr(orFilter,scopes,canSee);
		filter.and(orFilter);
		filter.and(new EqualsFilter("vmt-search-index",letter.equals("other")?"#":letter));
		if(!isAdmin){
			filter.and(new NotFilter(new EqualsFilter("vmt-visible", "false")));
		}
		return ldapTemplate.search(getBaseDN(scopes), filter.encode(),SearchControls.SUBTREE_SCOPE, resolver.getContextMapper());
		
	}
	@Override
	public List<LdapUser> searchUserByKeyword(String keyword, String[] scopes,String domains[],boolean[] canSee,boolean isAdmin){
		AndFilter filter=getBaseAndFilter();
		OrFilter scopeFilter=new OrFilter();
		addToOr(scopeFilter,scopes,canSee);
		filter.and(scopeFilter);
		if(!CommonUtils.isNull(keyword)){
			OrFilter likeFilter=new OrFilter();
			likeFilter.or(new LikeFilter(KEY.DB_CSTNET_ID,"*"+keyword+"*"));
			likeFilter.or(new LikeFilter(KEY.DB_NAME,"*"+keyword+"*"));
			likeFilter.or(new LikeFilter(KEY.DB_PINYIN,"*"+keyword+"*"));
			likeFilter.or(new LikeFilter
					("vmt-title","*"+keyword+"*"));
			filter.and(likeFilter);	
		}
		if(!CommonUtils.isNull(domains)){
			OrFilter likeFilter=new OrFilter();
			for(String domain:domains){
				likeFilter.or(new LikeFilter(KEY.DB_CSTNET_ID, "*@"+domain));
			}
			filter.and(likeFilter);
		}
		if(!isAdmin){
			filter.and(new NotFilter(new EqualsFilter("vmt-visible", "false")));
		}
		return ldapTemplate.search(getBaseDN(scopes), filter.encode(),SearchControls.SUBTREE_SCOPE, resolver.getContextMapper());
	}
	private String getBaseDN(String[] scopes){
		return scopes.length>1?KEY.DB_EMPTY:scopes[0];
		
	}
	@Override
	public void unbind(String[] dns) {
		for(String dn:dns){
			ldapTemplate.unbind(dn);
		}
	}
	@Override
	public boolean[] move(String[] users, String moveto) {
		boolean[] result=new boolean[users.length];
		int index=0;
		for(String userDN:users){
			DistinguishedName userNewDN=new  DistinguishedName(moveto);
			DistinguishedName userOldDN=new DistinguishedName(userDN);
			if(userOldDN.getPrefix(userOldDN.size()-1).toString().equals(moveto)){
				continue;
			}
			try {
				userNewDN.addAll(userOldDN.getSuffix(userOldDN.size()-1));
			} catch (InvalidNameException e) {
				LOGGER.error(e.getMessage(),e);
				return null;
			}
			if(isExistLevel(moveto, LdapUtils.getValue(userOldDN.getSuffix(userOldDN.size()-1).toString()))){
				result[index++]=false;
				continue;
			}
			ldapTemplate.rename(userOldDN, userNewDN);
			result[index++]=true;
		}
		return result;
	}
	@Override
	public LdapUser checkRandomOK(String teamDn,String random, String umtId) {
		AndFilter filter=getBaseAndFilter().and(new EqualsFilter("vmt-random", random))
		.and(new EqualsFilter(KEY.DB_UMTID,umtId));
		List<LdapUser> users=ldapTemplate.search(teamDn, filter.encode(), resolver.getContextMapper());
		return CommonUtils.first(users);
		
	}
	@Override
	public int getSubUserCount(String baseDN) {
		List<?> list=ldapTemplate.search(baseDN, getBaseAndFilter().encode(), resolver.getContextMapper());
		return list.size();
	}
	@Override
	public int getSubUserCount(String decodeDN, boolean canLookUnConfirm, boolean admin) {
		AndFilter filter=getBaseAndFilter();
		if(!admin){
			filter.and(new NotFilter(new  EqualsFilter("vmt-visible","false")));
		}
		if(!canLookUnConfirm){
			filter.and(new EqualsFilter("vmt-status",LdapUser.STATUS_ACTIVE));
		}
		return ldapTemplate.search(decodeDN, filter.encode(),SearchControls.SUBTREE_SCOPE, resolver.getContextMapper()).size();
		
	}
	@Override
	public LdapUser getUser(String dn) {
		return resolver.convert((DirContextAdapter) ldapTemplate.lookup(dn));
	}
	private AndFilter getBaseAndFilter(){
		AndFilter filter=new AndFilter();
		filter.and(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_USER));
		return filter;
	}
	@Override
	public List<LdapUser> searchUsersByCstnetId(String decodeDN, String cstnetIds[]) {
		return searchUsersByProperty(decodeDN, cstnetIds, KEY.DB_CSTNET_ID);
	}
	private List<LdapUser> searchUsersByProperty(String decodeDN,String[] values,String key){
		AndFilter filter=getBaseAndFilter();
		OrFilter orFilter=new OrFilter();
		if(!CommonUtils.isNull(values)){
			for(String value: values){
				orFilter.or(new EqualsFilter(key, value));
			}
		}else{
			return null;
		}
		filter.and(orFilter);
		return ldapTemplate.search(decodeDN, filter.encode(), SearchControls.SUBTREE_SCOPE,resolver.getContextMapper());
	}
	@Override
	public List<LdapUser> searchUsersByUmtId(String pdn, String[] umtIds) {
		AndFilter filter=getBaseAndFilter();
		OrFilter orFilter=new OrFilter();
		for(String umtId:umtIds){
			orFilter.or(new EqualsFilter(KEY.DB_UMTID, umtId));
		}
		filter.and(orFilter);
		return ldapTemplate.search(pdn, filter.encode(),SearchControls.SUBTREE_SCOPE,resolver.getContextMapper());
	}
	@Override
	public List<LdapUser> searchCoreMailUser(String pdn) {
		AndFilter filter=getBaseAndFilter();
		filter.and(new EqualsFilter("vmt-user-from", LdapUser.USER_FROM_CORE_MAIL));
		return ldapTemplate.search(pdn, filter.encode(),SearchControls.SUBTREE_SCOPE,resolver.getContextMapper());
	}
}