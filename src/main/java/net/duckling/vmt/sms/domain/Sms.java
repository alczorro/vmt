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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;
@TableMapping("vmt_sms")
public class Sms {
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping("send_time")
	private Date sendTime;
	@FieldMapping("sms_uuid")
	private String smsUuid;
	public String getSmsUuid() {
		return smsUuid;
	}
	public void setSmsUuid(String smsUuid) {
		this.smsUuid = smsUuid;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Sms(){}
	public static final Map<String,String> RT_CODE=new HashMap<String,String>();
	static{
		RT_CODE.put("r:xxx","成功发送");
		RT_CODE.put("r:000","成功提交");
		RT_CODE.put("r:001","黑名单号码");
		RT_CODE.put("r:002","非法手机号");
		RT_CODE.put("r:003","接口权限不足");
		RT_CODE.put("r:004","短信发送失败");
		RT_CODE.put("r:999","未知错误");
		RT_CODE.put("p:001","短信内容为空");
		RT_CODE.put("p:002","手机号码数组为空");
		RT_CODE.put("p:003","帐号或者密码不正确");
		RT_CODE.put("p:004","帐号余额不足");
		RT_CODE.put("p:005","超出每日限制发送量");
		RT_CODE.put("p:006","号码数量超过1000");
		RT_CODE.put("p:007","业务类型不正确");
		RT_CODE.put("p:008","ip鉴权失败");
	}
	public String getStatusDisplay(){
		return RT_CODE.get(this.subStatus);
	}
	public boolean isSuccess(){
		return "r:000".equals(this.subStatus);
	}
	private List<SmsGetter> getter;
	@FieldMapping("status")
	private String subStatus;
	@FieldMapping("sender_name")
	private String senderName;
	@FieldMapping("sender_cstnet_id")
	private String senderCstnetId;
	@FieldMapping("sender_umt_id")
	private String senderUmtId;
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
	public String getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	@FieldMapping("group_id")
	private int groupId;
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	@FieldMapping("content")
	private String content;
	public Sms(List<SmsGetter> getter, String content) {
		this.getter=getter;
		this.content=content;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isValid(){
		if(CommonUtils.isNull(content)||content.length()>70){
			return false;
		}
		if(CommonUtils.isNull(getter)||getter.size()>1000){
			return false;
		}
		return true;
	}
	public int getSuccessSendCount(){
		int index=0;
		for(SmsGetter g:this.getter){
			if(g.getStat().equals("r:xxx")){
				index++;
			}
		}
		return index;
	}
	public int getErrorCount(){
		int index=0;
		for(SmsGetter g:this.getter){
			if(!g.getStat().equals("r:000")&&!g.getStat().equals("r:xxx")){
				index++;
			}
		}
		return index;
	}
	public int getDelayCount(){
		int index=0;
		for(SmsGetter g:this.getter){
			if(g.getStat().equals("r:000")){
				index++;
			}
		}
		return index;
	}
	public List<SmsGetter> getGetter() {
		return getter;
	}
	public void setGetter(List<SmsGetter> getter) {
		this.getter = getter;
	}
	public String getSendTimeDisplay(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E",Locale.CHINA);
		return sdf.format(this.sendTime);
		
	}
	
	public String getSmsContent(){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		sb.append("<com.ctc.ema.server.jwsserver.sms.MtMessage>");
		sb.append("<content><![CDATA[").append(this.content).append("]]></content>");
		String smsId=null; 
		for(SmsGetter g:getter){
			sb.append("<phoneNumber><![CDATA[").append(g.getMobile()).append("]]></phoneNumber>");
			if(!CommonUtils.isNull(g.getSmsUuid())){
				smsId=g.getSmsUuid();
			}
		}
		if(!CommonUtils.isNull(smsId)){
			sb.append("<smsId>").append(smsId).append("</smsId>");
		}
		sb.append("</com.ctc.ema.server.jwsserver.sms.MtMessage>");
		return sb.toString();
	}
	
}
