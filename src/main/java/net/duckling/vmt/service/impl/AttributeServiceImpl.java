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

import java.util.Map;

import net.duckling.vmt.dao.IAttributeDAO;
import net.duckling.vmt.service.IAttributeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-5-13
 */
@Service
public class AttributeServiceImpl implements IAttributeService {
	@Autowired
	private IAttributeDAO attributeDAO;
	
	@Override
	public void insert(String dn, String attrubuteName, String[] values) {
		attributeDAO.insert(dn, attrubuteName, values);
	}

	@Override
	public void delete(String dn, String attrubuteName, String[] values) {
		attributeDAO.delete(dn, attrubuteName, values);
	}

	@Override
	public String[] get(String dn, String attributeName) {
		return attributeDAO.get(dn, attributeName);
	}

	@Override
	public boolean isExists(String dn, String attributeName, String value) {
		return attributeDAO.isExists(dn, attributeName, value);
	}

	@Override
	public void update(String dn, String attributeName, Object value) {
		attributeDAO.update(dn, attributeName, value==null?null:value.toString());
	}

	@Override
	public Map<String, String> search(String dn, String attributeName) {
		return attributeDAO.search(dn, attributeName);
	}

}
