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
package net.duckling.vmt.service.impl;

import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.dao.IEmailDAO;
import net.duckling.vmt.dao.IEmailFileDAO;
import net.duckling.vmt.dao.IEmailGetterDAO;
import net.duckling.vmt.dao.IEmailGroupDAO;
import net.duckling.vmt.dao.IEmailSendablePeopledDAO;
import net.duckling.vmt.domain.email.Email;
import net.duckling.vmt.domain.email.EmailFile;
import net.duckling.vmt.domain.email.EmailGetter;
import net.duckling.vmt.domain.email.EmailGroup;
import net.duckling.vmt.domain.email.EmailSendablePeople;
import net.duckling.vmt.service.ISendEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceImpl implements ISendEmailService{
	@Autowired
	private IEmailDAO emailDAO;
	@Autowired
	private IEmailGroupDAO emailGroupDAO;
	@Autowired
	private IEmailGetterDAO getterDAO;
	
	@Autowired
	private IEmailFileDAO emailFileDAO;
	
	@Autowired
	private IEmailSendablePeopledDAO senableDAO;
	
	@Override
	public void saveEmail(Email email) {
		email.setId(emailDAO.saveEmail(email));
	}
	@Override
	public void saveGroup(EmailGroup eg) {
		eg.setId(emailGroupDAO.saveGroup(eg));
	}
	@Override
	public void saveGetter(List<EmailGetter> egs) {
		getterDAO.saveGetter(egs);
	}
	@Override
	public void saveFiles(List<EmailFile> efs) {
		emailFileDAO.saveFiles(efs);
	}
	@Override
	public List<Email> getEmailByUmtId(String umtId) {
		return emailDAO.getEmailByUmtId(umtId);
	}
	@Override
	public Email getEmailById(int emailId) {
		return emailDAO.getEmailById(emailId);
	}
	@Override
	public List<EmailGetter> getGetterByEmailId(int emailId) {
		return getterDAO.getGetterByEmailId(emailId);
	}
	@Override
	public List<EmailFile> getFilesByEmailId(int emailId) {
		return emailFileDAO.getFilesByEmailId(emailId);
	}
	@Override
	public EmailFile getFileByClbId(int fileId) {
		return emailFileDAO.getFileByClbId(fileId);
	}
	@Override
	public List<EmailGroup> getGroupByUmtId(String umtId) {
		return emailGroupDAO.getGroupByUmtId(umtId);
	}
	@Override
	public EmailGroup getGroupById(int groupId) {
		return emailGroupDAO.getGroupById(groupId);
	}
	@Override
	public List<EmailGetter> getGetterByGroupId(int groupId) {
		return getterDAO.getGetterByGroupId(groupId);
	}
	@Override
	public void updateGroupName(int groupId, String name) {
		emailGroupDAO.updateGroupName(groupId,name);
	}
	@Override
	public void removeGroup(int groupId) {
		emailGroupDAO.removeGroup(groupId);
		getterDAO.removeGroupId(groupId);
	}
	@Override
	public void addEmailSendable(EmailSendablePeople sep) {
		senableDAO.add(sep);
	}
	@Override
	public void removeSendable(String dn, String umtId) {
		senableDAO.delete(dn, umtId);
	}
	@Override
	public List<EmailSendablePeople> getSenableByDN(String dn) {
		return senableDAO.getSenableByDN(dn);
	}
	@Override
	public boolean isMember(String domain, String umtId) {
		if(CommonUtils.isNull(domain)){
			return false;
		}
		return senableDAO.isMember(domain,umtId);
	}

}
