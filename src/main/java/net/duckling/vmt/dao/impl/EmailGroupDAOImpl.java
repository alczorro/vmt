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

import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IEmailGroupDAO;
import net.duckling.vmt.domain.email.EmailGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailGroupDAOImpl implements IEmailGroupDAO {

	@Autowired
	private BaseDAO<EmailGroup> baseDAO;
	
	@Override
	public int saveGroup(EmailGroup eg) {
		return baseDAO.insert(eg);
	}
	@Override
	public List<EmailGroup> getGroupByUmtId(String umtId) {
		EmailGroup eg=new EmailGroup();
		eg.setGroupMasterUmtId(umtId);
		return baseDAO.select(eg,"order by id desc");
	}
	@Override
	public EmailGroup getGroupById(int groupId) {
		EmailGroup eg=new EmailGroup();
		eg.setId(groupId);
		return baseDAO.selectOne(eg);
	}
	@Override
	public void updateGroupName(int groupId, String name) {
		EmailGroup eg=new EmailGroup();
		eg.setId(groupId);
		eg.setGroupName(name);
		baseDAO.update(eg);
	}
	@Override
	public void removeGroup(int groupId) {
		EmailGroup eg=new EmailGroup();
		eg.setId(groupId);
		baseDAO.delete(eg);
	}

}
