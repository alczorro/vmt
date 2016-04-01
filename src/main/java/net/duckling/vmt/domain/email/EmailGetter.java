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
package net.duckling.vmt.domain.email;

import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;

@TableMapping("vmt_email_getter")
public class EmailGetter {
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping("getter_cstnet_id")
	private String getterCstnetId;
	@FieldMapping("getter_name")
	private String getterName;
	@FieldMapping("getter_umt_id")
	private String getterUmtId;
	@FieldMapping("email_id")
	private int emailId;
	@FieldMapping("group_id")
	private int groupId;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGetterCstnetId() {
		return getterCstnetId;
	}
	public void setGetterCstnetId(String getterCstnetId) {
		this.getterCstnetId = getterCstnetId;
	}
	public String getGetterName() {
		return getterName;
	}
	public void setGetterName(String getterName) {
		this.getterName = getterName;
	}
	public String getGetterUmtId() {
		return getterUmtId;
	}
	public void setGetterUmtId(String getterUmtId) {
		this.getterUmtId = getterUmtId;
	}
	public int getEmailId() {
		return emailId;
	}
	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}
	
}
