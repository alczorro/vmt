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
package net.duckling.vmt.dao;

import java.util.List;

import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.domain.message.VmtMsgColumns;

public interface IVmtMessageDAO {

	int insertMsg(VmtMessage vmtMessage);

	void insertMsgColumns(List<VmtMsgColumns> columns);

	List<VmtMessage> getMsgByEntryId(String dn);

	List<VmtMsgColumns> getMsgColumnsByEntryId(List<Integer> extractId);

	void updateMsgStatus(int msgId,String status);

	void deleteMsg(int msgId);
	

	VmtMessage getMsgByProperty(VmtMessage msg);

	boolean isEntryIdUsed(String entryId);

	List<VmtMessage> getMsgByToAdmin(String[] imAdminDNs);

	List<VmtMessage> getMsgByToMe(String[] allDN,String myCstnetId);

	int getMsgByToMeCount(String[] allDN, String myCstnetId);

	int getMsgByToAdminCount(String[] imAdminDNs);

	VmtMessage getMsgById(int msgId);

}
