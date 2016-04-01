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
package net.duckling.vmt.domain;

import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;
@TableMapping("vmt_app_profile")
public class VmtAppProfile {
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping
	private String fkId;
	@FieldMapping
	private String appType;
	@FieldMapping
	private String umtId;
	@FieldMapping
	private String cstnetId;
	@FieldMapping
	private String value;
	
	public static final String VALUE_SHOW="show";
	public static final String VALUE_HIDE="hide";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFkId() {
		return fkId;
	}
	public void setFkId(String fkId) {
		this.fkId = fkId;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	public String getCstnetId() {
		return cstnetId;
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
