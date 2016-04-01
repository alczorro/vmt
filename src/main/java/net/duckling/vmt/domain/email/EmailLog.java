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

import java.util.Date;

import net.duckling.cloudy.common.DateUtils;
import net.duckling.falcon.api.serialize.JSONMapper;

import org.apache.log4j.Logger;

public class EmailLog {
	private static final Logger LOG=Logger.getLogger(EmailLog.class);
	private String sender;
	private String[] getter;
	private String occurTime;
	private String emailTitle;
	private String logType;
	private boolean success;
	private String desc;
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public EmailLog(){
		this.logType="email";
		occurTime=DateUtils.getDateStr(new Date());
	}
	public void log(){
		LOG.info(JSONMapper.getJSONString(this));
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String[] getGetter() {
		return getter;
	}
	public void setGetter(String[] getter) {
		this.getter = getter;
	}
	public String getOccurTime() {
		return occurTime;
	}
	public void setOccurTime(String occurTime) {
		this.occurTime = occurTime;
	}
	public String getEmailTitle() {
		return emailTitle;
	}
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
}
