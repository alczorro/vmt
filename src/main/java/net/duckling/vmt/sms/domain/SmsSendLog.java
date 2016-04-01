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
package net.duckling.vmt.sms.domain;

import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;

@TableMapping("vmt_sms_send_log")
public class SmsSendLog {
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping("sender_name")
	private String senderName;
	
	@FieldMapping("sender_cstnet_id")
	private String senderCstnetId;
	@FieldMapping("sender_umt_id")
	private String umtId;
	@FieldMapping("send_success_count")
	private int sendCount;
	@FieldMapping("sms_id")
	private int smsId;
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	@FieldMapping("group_id")
	private int groupId;
	
	
	public int getSmsId() {
		return smsId;
	}
	public void setSmsId(int smsId) {
		this.smsId = smsId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderCstnetId() {
		return senderCstnetId;
	}
	public void setSenderCstnetId(String senderCstnetId) {
		this.senderCstnetId = senderCstnetId;
	}
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	
}
