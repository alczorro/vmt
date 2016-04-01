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
import net.duckling.vmt.dao.IEmailSendablePeopledDAO;
import net.duckling.vmt.domain.email.EmailSendablePeople;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailSendablePeopleDAOImpl implements IEmailSendablePeopledDAO{

	@Autowired
	private BaseDAO<EmailSendablePeople> baseDAO;
	@Override
	public void add(EmailSendablePeople sep) {
		baseDAO.insert(sep);
	}
	
	@Override
	public void delete(String teamDn, String umtId) {
		EmailSendablePeople sep=new EmailSendablePeople();
		sep.setOrgDn(teamDn);
		sep.setUmtId(umtId);
		baseDAO.delete(sep);
	}
	@Override
	public List<EmailSendablePeople> getSenableByDN(String dn) {
		EmailSendablePeople esp=new EmailSendablePeople();
		esp.setOrgDn(dn);
		return baseDAO.select(esp);
	}
	@Override
	public boolean isMember(String domain, String umtId) {
		EmailSendablePeople esp=new EmailSendablePeople();
		esp.setDomain(domain);
		esp.setUmtId(umtId);
		return baseDAO.getCount(esp)>0;
	}
	
}
