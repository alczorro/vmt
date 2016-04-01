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
package net.duckling.vmt.sms;

import java.util.List;

import net.duckling.vmt.sms.domain.Sms;
import net.duckling.vmt.sms.domain.SmsGetter;
import net.duckling.vmt.sms.domain.SmsGroup;
import net.duckling.vmt.sms.domain.SmsGroupMember;
import net.duckling.vmt.sms.domain.SmsRechargeLog;
import net.duckling.vmt.sms.domain.SmsSendLog;


/**
 * 发送短信服务类
 * **/
public interface ISmsService {
	//发送短信
	void sendSms(Sms sms);
	
	List<SmsGroup> getAllSmsGroup();

	boolean createSmsGroup(SmsGroup smsGroup);
	
	int getAdminCount(int groupId);

	void removeMember(int mid);
	
	void addMember(SmsGroupMember m);

	SmsGroupMember getGroupMemberByGidAndUmtId(int groupId, String umtId);
	List<SmsGroupMember> getGroupMemberByGroupId(int groupId);
	
	void update(SmsGroupMember m);

	SmsGroup getGroupById(int gid);

	void recharge(SmsGroup g, int plus, String email);

	boolean isAccountUsed(String trim);
	List<SmsRechargeLog> findChargeLogByGid(int gid);

	void removeGroupById(int gid);

	SmsGroup getLastGroup(String umtId);
	
	List<Sms> getSmsByGroupId(int groupId);
	
	List<Sms> getSmsByGroupIdAndUmtId(int groupId,String umtId);

	Sms getSmsById(int smsId);

	Sms getSmsByIdAndUmtId(int smsId, String umtId);

	List<SmsGetter> getSmsGetterBySmsId(int id);

	SmsGetter getSmsGetterBySmsGetterId(int smsLogId);

	void resendSms(Sms sms);

	List<SmsGetter> getSmsGetterByFail(int gid);

	void changeMemberRole(int memberId, boolean isAdmin);
	List<SmsSendLog> getLogGroupByuserName(int gid);
	List<SmsSendLog> getLogGroupByuserName(int gid,String umtId);

	SmsGroupMember getGroupMemberByGidAndId(int gid, int memberId);
	
	void ifLessThan20PercentSendEmail(SmsGroup smsGroup);
	
}
