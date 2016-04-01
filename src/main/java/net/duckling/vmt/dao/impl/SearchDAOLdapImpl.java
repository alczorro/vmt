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
import net.duckling.vmt.dao.ISearchDAO;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.view.ListView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2013-5-10
 */
@SuppressWarnings("unchecked")
@Component
public class SearchDAOLdapImpl implements ISearchDAO{
	@Autowired
	private LdapTemplate ldapTemplate;
	private LdapMappingResolver<LdapUser> userResolver=new LdapMappingResolver<LdapUser>(LdapUser.class);
	private LdapMappingResolver<LdapDepartment> deptResolver=new LdapMappingResolver<LdapDepartment>(LdapDepartment.class);
	private ContextMapper getListViewContextMapper(final boolean canSearch){
		return new ContextMapper() {
			@Override
			public ListView mapFromContext(Object arg0) {
				DirContextAdapter context=(DirContextAdapter)arg0;
				ListView view=new ListView();
				view.setDn(context.getDn().toString());
				view.setName(context.getStringAttribute("vmt-name"));
				view.setEmail(context.getStringAttribute("vmt-id"));
				switch(context.getStringAttribute(LdapObjectClass.OBJECTCLASS)){
					case LdapObjectClass.CLASS_VMT_DEPART:{
						view.setType(ListView.TYPE_DEPART);
						String visible=context.getStringAttribute("vmt-visible");
						view.setVisible(CommonUtils.isNull(visible)?true:Boolean.parseBoolean(visible));
						break;
					}
					case LdapObjectClass.CLASS_VMT_USER:{
						if(!canSearch&&context.getStringAttribute("vmt-status").equals("false")){
							return null;
						}
						view.setType(ListView.TYPE_USER);
						view.setMobile(context.getStringAttribute("vmt-telephone"));
						view.setVisible(Boolean.parseBoolean(context.getStringAttribute("vmt-visible")));
						view.setStatus(context.getStringAttribute("vmt-status"));
						view.setUmtId(context.getStringAttribute("vmt-umtId"));
					}break;
					default:break;
				}
				String rank=context.getStringAttribute("vmt-list-rank");
				view.setListRank(Integer.parseInt(rank==null?"0":rank));
				view.setCurrentDisplay(context.getStringAttribute("vmt-current-display"));
				return view;
			}
		};
	}
	@Override
	public List<ListView> searchDepartByAll(String dn) {
		OrFilter orFilter=new OrFilter();
		orFilter.or(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_DEPART));
		orFilter.or(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_USER));
		return CommonUtils.removeNull(ldapTemplate.search(dn,orFilter.encode(),SearchControls.SUBTREE_SCOPE, getListViewContextMapper(true)));
	}
	
	
	@Override
	public List<ListView> searchByList(final String dn,final boolean canSearch,final boolean isAdmin) {
		AndFilter filter=new AndFilter();
		OrFilter orFilter=new OrFilter();
		filter.and(orFilter);
		orFilter.or(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_DEPART));
		orFilter.or(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_USER));
		if(!isAdmin){
			filter.and(new NotFilter(new EqualsFilter("vmt-visible","false")));
		}
		
		return CommonUtils.removeNull(ldapTemplate.search(dn,filter.encode(),SearchControls.ONELEVEL_SCOPE, getListViewContextMapper(canSearch) ));
	}
	@Override
	public List<?> searchByListLocalData(String dn){
		AndFilter filter=new AndFilter();
		OrFilter orFilter=new OrFilter();
		orFilter.or(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_DEPART));
		orFilter.or(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_USER));
		filter.and(orFilter);
		return ldapTemplate.search(dn, filter.encode(),SearchControls.ONELEVEL_SCOPE,new ContextMapper() {
			@Override
			public Object mapFromContext(Object dir) {
				DirContextAdapter context=(DirContextAdapter)dir;
				String[] objects=context.getStringAttributes(LdapObjectClass.OBJECTCLASS);
				if(CommonUtils.isEqualsContain(objects, LdapObjectClass.CLASS_VMT_USER)){
					return userResolver.convert(context);
					
				}else if(CommonUtils.isEqualsContain(objects, LdapObjectClass.CLASS_VMT_DEPART)){
					return deptResolver.convert(context);
				}
				return null;
			}
		});
	}
	@Override
	public List<ListView> searchDepartByList(final String decodeDN) {
		AndFilter filter=new AndFilter();
		filter.and(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_DEPART));
		return ldapTemplate.search(decodeDN, filter.encode(), SearchControls.ONELEVEL_SCOPE,getListViewContextMapper(true));
	}
	@Override
	public int searchActiveUserCount(String dn) {
		AndFilter filter=new AndFilter();
		filter.and(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_USER));
		filter.and(new EqualsFilter("vmt-status", LdapUser.STATUS_ACTIVE));
		List<?> result=ldapTemplate.search(dn, filter.encode(), SearchControls.SUBTREE_SCOPE, new ContextMapper(){
			@Override
			public Object mapFromContext(Object arg0) {
				return null;
			}
		});
		return result.size();
	}

}
