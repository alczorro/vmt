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
import net.duckling.vmt.dao.IDepartmentDAO;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.exception.DBErrorCode;
import net.duckling.vmt.exception.LdapOpeException;
import net.duckling.vmt.exception.LdapParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2013-5-2
 */
@SuppressWarnings("unchecked")
@Component
public class DepartmentDAOLdapImpl implements IDepartmentDAO{
	private static final Logger LOGGER=Logger.getLogger(IDepartmentDAO.class);
	@Autowired
	private LdapTemplate ldapTemplate;
	
	private LdapMappingResolver<LdapDepartment> resolver=new LdapMappingResolver<LdapDepartment>(LdapDepartment.class);
	
	@Override
	public LdapDepartment getDepartByName(String pdn, String name) {
		AndFilter filter=getBaseFilter();
		filter.and(new EqualsFilter(KEY.DB_NAME, name));
		List <LdapDepartment> list=ldapTemplate.search(pdn, filter.encode(),SearchControls.ONELEVEL_SCOPE, resolver.getContextMapper());
		return CommonUtils.first(list);
	}
	@Override
	public boolean insert(String pdn, LdapDepartment depart) {
		try {
			if(!isExists(pdn,depart.getName())){
				ldapTemplate.bind(resolver.build(depart, pdn));
				return true;
			}else{
				throw new LdapOpeException(DBErrorCode.NODE_EXISTS,pdn+depart.getName());
			}
		} catch (LdapParseException e) {
			LOGGER.error(e.getMessage(),e);
			return false;
		}
	}
	@Override
	public String getDn(String pdn, String departName) {
		ldapTemplate.setIgnoreNameNotFoundException(true);
		AndFilter filter=getBaseFilter();
		filter.and(new EqualsFilter("vmt-name", departName));
		List<?> result=ldapTemplate.search(pdn, filter.encode(), SearchControls.ONELEVEL_SCOPE, new ContextMapper() {
			@Override
			public Object mapFromContext(Object arg) {
				DirContextAdapter adp=(DirContextAdapter)arg;
				return adp.getDn().toString();
			}
		});
		return result.isEmpty()?"":result.get(0).toString();
	}
	@Override
	public LdapDepartment getDepartByDN(String dn) {
		ldapTemplate.setIgnoreNameNotFoundException(true);
		return (LdapDepartment) ldapTemplate.lookup(dn, resolver.getContextMapper());
	}
	private AndFilter getBaseFilter(){
		return new AndFilter().and(new EqualsFilter(LdapObjectClass.OBJECTCLASS, LdapObjectClass.CLASS_VMT_DEPART));
	}
	@Override
	public List<LdapDepartment> getDepartByAll(String baseDn) {
		AndFilter filter=getBaseFilter();
		return ldapTemplate.search(baseDn, filter.encode(), SearchControls.SUBTREE_SCOPE,resolver.getContextMapper());
	}
	@Override
	public boolean isExists(String pdn,String departName) {
		return !CommonUtils.isNull(getDn(pdn, departName));
	}
	@Override
	public LdapDepartment getDepartBySymbol(String orgDN, String symbol) {
		AndFilter filter=getBaseFilter();
		filter.and(new EqualsFilter(KEY.DB_VMT_SYMBOL,symbol));
		List<LdapDepartment> result=(List<LdapDepartment>)ldapTemplate.search(orgDN, filter.encode(),SearchControls.SUBTREE_SCOPE,resolver.getContextMapper());
		return CommonUtils.first(result);
	}

}
