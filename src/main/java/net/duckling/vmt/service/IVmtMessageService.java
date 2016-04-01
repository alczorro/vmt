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

import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.domain.message.VmtMsgColumns;

public interface IVmtMessageService {
	
	int insertMsg(VmtMessage vmtMessage);
	
	void insertMsgColumns(VmtMsgColumns columns);
	
	void insertMsgColumns(List<VmtMsgColumns> columns);
	
	List<VmtMessage> getMsgByEntryId(String dn);
	
	void updateMsgReaded(int msgId);
	
	void deleteMsg(int msgId);

	List<VmtMessage> getMsgByICanSee(String cstnetId, String[] allDN, String[] allAdminDN);

	int getMsgByICanSeeCount(String cstnetId, String[] allDN, String[] allAdminDN);

	void updateMsgUnReaded(int msgId);

	VmtMessage getMsgByProperty(VmtMessage msg);
	
	VmtMessage getMsgById(int msgId);
	
	boolean isEquals(VmtMessage msg);
	
	boolean isLoginNameUsed(String entryId);

	void updateMsgReaded(VmtMessage msg);
	
	
}
