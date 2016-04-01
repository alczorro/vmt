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
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;

@TableMapping("vmt_sms_getter")
public class SmsGetter {
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping("umt_id")
	private String umtId;
	@FieldMapping("cstnet_id")
	private String cstnetId;
	@FieldMapping("mobile")
	private String mobile;
	@FieldMapping("true_name")
	private String trueName;
	@FieldMapping("stat")
	private String stat;
	public int getSmsId() {
		return smsId;
	}
	public void setSmsId(int smsId) {
		this.smsId = smsId;
	}
	public String getSmsUuid() {
		return smsUuid;
	}
	public void setSmsUuid(String smsUuid) {
		this.smsUuid = smsUuid;
	}
	@FieldMapping("sms_id")
	private int smsId;
	@FieldMapping("sms_uuid")
	private String smsUuid;
	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getStat() {
		return stat;
	}
	public String getStatDesc(){
		return Sms.RT_CODE.get(this.stat);
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public SmsGetter(){}
	public SmsGetter(String umtId,String cstnetId,String trueName,String mobile){
		this.umtId=umtId;
		this.cstnetId=cstnetId;
		this.trueName=trueName;
		this.mobile=mobile;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
}
