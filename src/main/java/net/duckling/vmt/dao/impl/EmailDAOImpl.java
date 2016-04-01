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

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IEmailDAO;
import net.duckling.vmt.domain.email.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailDAOImpl implements IEmailDAO {

	@Autowired
	private BaseDAO<Email> baseDAO;
	@Override
	public int saveEmail(Email email) {
		return baseDAO.insert(email);
	}
	@Override
	public List<Email> getEmailByUmtId(String umtId) {
		if(CommonUtils.isNull(umtId)){
			return null;
		}
		Email e=new Email();
		e.setSenderUmtId(umtId);
		return baseDAO.select(e,"order by id desc");
	}
	@Override
	public Email getEmailById(int emailId) {
		Email e=new Email();
		e.setId(emailId);
		return baseDAO.selectOne(e);
	}

}
