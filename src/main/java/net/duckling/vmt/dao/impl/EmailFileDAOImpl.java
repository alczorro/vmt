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
import net.duckling.vmt.dao.IEmailFileDAO;
import net.duckling.vmt.domain.email.EmailFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmailFileDAOImpl implements IEmailFileDAO {

	@Autowired
	private BaseDAO<EmailFile> baseDAO;
	@Override
	public void saveFiles(List<EmailFile> efs) {
		baseDAO.batchAdd(efs);
	}
	@Override
	public List<EmailFile> getFilesByEmailId(int emailId) {
		EmailFile ef=new EmailFile();
		ef.setEmailId(emailId);
		return baseDAO.select(ef);
	}
	@Override
	public EmailFile getFileByClbId(int fileId) {
		EmailFile ef=new EmailFile();
		ef.setClbId(fileId);
		return baseDAO.selectOne(ef);
	}

}
