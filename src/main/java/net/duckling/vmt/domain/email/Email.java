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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;

@TableMapping("vmt_email")
public class Email {
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping("sender_name")
	private String senderName;
	@FieldMapping("sender_cstnet_id")
	private String senderCstnetId;
	@FieldMapping("sender_umt_id")
	private String senderUmtId;
	@FieldMapping("title")
	private String title;
	@FieldMapping("content")
	private String content;
	@FieldMapping("send_time")
	private Date sendTime;
	private boolean success;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
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
	public String getSenderUmtId() {
		return senderUmtId;
	}
	public void setSenderUmtId(String senderUmtId) {
		this.senderUmtId = senderUmtId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getSendTimeDisplay(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E",Locale.CHINA);
		return sdf.format(this.sendTime);
		
	}
	
	

}
