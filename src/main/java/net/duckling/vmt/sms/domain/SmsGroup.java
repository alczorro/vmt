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

import java.util.ArrayList;
import java.util.List;

import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;

@TableMapping("vmt_sms_group")
public class SmsGroup {
	@FieldMapping(type=Type.ID)
	private int id;
	
	@FieldMapping("group_name")
	private String groupName;
	
	@FieldMapping("account")
	private String account;
	
	@FieldMapping("sms_count")
	private int smsCount;
	
	@FieldMapping("sms_used")
	private int smsUsed;
	
	private List<SmsGroupMember> members=new ArrayList<SmsGroupMember>();
	
	public List<SmsGroupMember> getMembers(){
		return this.members;
	}
	public void addMember(SmsGroupMember m){
		this.members.add(m);
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(int smsCount) {
		this.smsCount = smsCount;
	}

	public int getSmsUsed() {
		return smsUsed;
	}

	public void setSmsUsed(int smsUsed) {
		this.smsUsed = smsUsed;
	}
	

	

}
