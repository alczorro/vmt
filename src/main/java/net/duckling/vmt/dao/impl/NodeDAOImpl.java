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
package net.duckling.vmt.dao.impl;

import java.util.List;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.ldap.LdapMappingResolver;
import net.duckling.vmt.common.ldap.LdapObjectClass;
import net.duckling.vmt.dao.INodeDAO;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.service.IAttributeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Component;

/**
 * 节点的Ldap实现类
 * @author lvly
 * @since 2013-6-21
 */
@Component
public class NodeDAOImpl  implements INodeDAO{
	/**
	 * 属性的服务类
	 */
	@Autowired
	private IAttributeService attrService;
	/**
	 * spring 对ldap持久化的实现
	 */
	@Autowired
	private LdapTemplate ldapTemplate;
	/**
	 * LdapNode解释器
	 */
	private LdapMappingResolver<LdapNode> resolver=new LdapMappingResolver<LdapNode>(LdapNode.class);

	@Override
	public LdapNode getNode(String dn) {
		return resolver.convert((DirContextAdapter) ldapTemplate.lookup(dn));
	}
	@Override
	public void updateSonsAndMyDisplayName(String pdn, final int size, final String newName) {
		AndFilter andFilter=new AndFilter();
		andFilter.and(new EqualsFilter(LdapObjectClass.OBJECTCLASS,LdapObjectClass.CLASS_VMT_NODE));
		ldapTemplate.search(pdn, andFilter.encode(), new ContextMapper() {
			@Override
			public Object mapFromContext(Object arg0) {
				DirContextAdapter adp=(DirContextAdapter)arg0;
				String currDisplay=adp.getStringAttribute("vmt-current-display");
				attrService.update(adp.getDn().toString(), "vmt-current-display", update(currDisplay,size-1,newName));
				return null;
			}
		});
		
	}
	private String update(String displayName,int index,String newValue){
		String[] values=displayName.split(",");
		StringBuilder result=new StringBuilder();
		for(int i=0;i<values.length;i++){
			if(i!=0){
				result.append(",");
			}
			if(i==(index-1)){
				result.append(newValue);
			}else{
				result.append(values[i]);
			}
		}
		return result.toString();
	}
	@Override
	public boolean isSymbolUsed(String pdn, String symbol) {
		AndFilter filter=new AndFilter();
		filter.and(new EqualsFilter(LdapObjectClass.OBJECTCLASS, LdapObjectClass.CLASS_VMT_NODE));
		filter.and(new EqualsFilter("vmt-symbol",symbol));
		List<?> result=ldapTemplate.search(pdn, filter.encode(),new ContextMapper() {
			@Override
			public Object mapFromContext(Object arg0) {
				return null;
			}
		});
		return !CommonUtils.isNull(result);
	}
}
