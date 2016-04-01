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
package net.duckling.vmt.service;

import java.util.List;

import net.duckling.vmt.domain.email.Email;
import net.duckling.vmt.domain.email.EmailFile;
import net.duckling.vmt.domain.email.EmailGetter;
import net.duckling.vmt.domain.email.EmailGroup;
import net.duckling.vmt.domain.email.EmailSendablePeople;

public interface ISendEmailService {
	void saveEmail(Email email);

	void saveGroup(EmailGroup eg);

	void saveGetter(List<EmailGetter> egs);

	void saveFiles(List<EmailFile> efs);

	List<Email> getEmailByUmtId(String umtId);

	Email getEmailById(int emailId);

	List<EmailGetter> getGetterByEmailId(int emailId);

	List<EmailFile> getFilesByEmailId(int emailId);

	EmailFile getFileByClbId(int fileId);

	List<EmailGroup> getGroupByUmtId(String umtId);

	EmailGroup getGroupById(int groupId);

	List<EmailGetter> getGetterByGroupId(int groupId);

	void updateGroupName(int groupId, String name);

	void removeGroup(int groupId);

	void addEmailSendable(EmailSendablePeople sep);

	List<EmailSendablePeople> getSenableByDN(String dn);

	void removeSendable(String dn, String umtId);

	boolean isMember(String domain, String umtId);
}
