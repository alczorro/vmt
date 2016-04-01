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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.util.Pair;
import net.duckling.vmt.dao.IAttributeDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2013-5-3
 */
@Component
public class AttributeDAOLdapImpl implements IAttributeDAO {
	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public void insert(String dn, String attributeName, String values[]) {
		modify(dn, attributeName, DirContext.ADD_ATTRIBUTE, values);
	}

	@Override
	public void delete(String dn, String attributeName, String[] values) {
		modify(dn, attributeName, DirContext.REMOVE_ATTRIBUTE, values);
	}
	private void modify(String dn, String attributeName, int operation,
			String[] values) {

		int index = 0;
		String killNull[] = killNull(values);
		if (CommonUtils.isNull(killNull)) {
			return;
		}
		ModificationItem[] items = new ModificationItem[killNull.length];
		for (String value : killNull(values)) {
			if (value == null) {
				continue;
			}
			items[index++] = new ModificationItem(operation,
					new BasicAttribute(attributeName, value));
		}
		ldapTemplate.modifyAttributes(dn, items);
	}
	private String[] killNull(String[] values) {
		Set<String> result = new HashSet<String>();
		for (String value : values) {
			if (value == null) {
				continue;
			}
			result.add(value);
		}
		return result.toArray(new String[result.size()]);

	}

	@Override
	public String[] get(String dn, String attributeName) {
		DirContextAdapter result = (DirContextAdapter) ldapTemplate.lookup(dn);
		return result.getStringAttributes(attributeName);
	}
	@Override
	public boolean isExists(String dn, String attributeName, String value) {
		String attr[] = get(dn, attributeName);
		if (CommonUtils.isNull(attr)) {
			return false;
		}
		for (String str : attr) {
			if (str.equals(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void update(String dn, String attributeName, String value) {
		modify(dn, attributeName, DirContext.REPLACE_ATTRIBUTE,
				new String[]{value});
	}

	@Override
	public Map<String, String> search(String dn, String attributeName) {
		final String realAttributeName = "vmt-" + attributeName;
		@SuppressWarnings("unchecked")
		List<Pair> pairs = (List<Pair>) ldapTemplate.search(dn,
				"(objectclass=vmt-user)", SearchControls.SUBTREE_SCOPE,
				new String[]{"vmt-id", realAttributeName}, new AttributesMapper() {
					@Override
					public Object mapFromAttributes(Attributes attributes)
							throws NamingException {
						Attribute umtId = attributes.get("vmt-id");
						Attribute requireAttribute = attributes
								.get(realAttributeName);
						if (umtId != null) {
							if (requireAttribute != null) {
								Object value = requireAttribute.get();
								if (value != null) {
									return new Pair(umtId.get().toString(),
											value.toString());
								}
							} else {
								return new Pair(umtId.get().toString(), "");
							}
						}
						return null;
					}
				});
		HashMap<String, String> map = new HashMap<String, String>();
		for (Pair pair : pairs) {
			if (pair != null) {
				map.put(pair.getFirst(), pair.getSecond());
			}
		}
		return map;
	}
}
