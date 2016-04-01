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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IEmailGetterDAO;
import net.duckling.vmt.domain.email.EmailGetter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailGetterDAOImpl implements IEmailGetterDAO {

	@Autowired
	private BaseDAO<EmailGetter> baseDAO;
	@Override
	public void saveGetter(List<EmailGetter> getter) {
		baseDAO.batchAdd(getter);
	}
	@Override
	public List<EmailGetter> getGetterByEmailId(int emailId) {
		EmailGetter eg=new EmailGetter();
		eg.setEmailId(emailId);
		return baseDAO.select(eg);
	}
	@Override
	public List<EmailGetter> getGetterByGroupId(int groupId) {
		EmailGetter eg=new EmailGetter();
		eg.setGroupId(groupId);
		return baseDAO.select(eg);
	}
	@Override
	public void removeGroupId(int groupId) {
		String sql="update vmt_email_getter set group_id=-1 where group_id=:groupId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("groupId", groupId);
		baseDAO.getTmpl().update(sql, map);
	}

}
