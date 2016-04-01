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

import java.util.Date;

import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;

@TableMapping("vmt_sms_recharge_log")
public class SmsRechargeLog {
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping("group_id")
	private int groupId;
	@FieldMapping("plus")
	private int plus;
	@FieldMapping("last")
	private int last;
	@FieldMapping("who_charged")
	private String whoCharged;
	@FieldMapping("charged_time")
	private Date chargedTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getPlus() {
		return plus;
	}
	public void setPlus(int plus) {
		this.plus = plus;
	}
	public int getLast() {
		return last;
	}
	public void setLast(int last) {
		this.last = last;
	}
	public String getWhoCharged() {
		return whoCharged;
	}
	public void setWhoCharged(String whoCharged) {
		this.whoCharged = whoCharged;
	}
	public Date getChargedTime() {
		return chargedTime;
	}
	public void setChargedTime(Date chargedTime) {
		this.chargedTime = chargedTime;
	}
	
	

}
