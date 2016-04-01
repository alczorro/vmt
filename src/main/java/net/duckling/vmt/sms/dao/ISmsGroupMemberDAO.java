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
package net.duckling.vmt.sms.dao;

import java.util.List;

import net.duckling.vmt.sms.domain.SmsGroupMember;

public interface ISmsGroupMemberDAO {
	List<SmsGroupMember> getMemberByGroupId(List<Integer> groupIds);
	List<SmsGroupMember> getMemberAll();
	int addMember(SmsGroupMember m);
	int getAdminCount(int groupId);
	void removeMember(int mid);
	SmsGroupMember getGroupMemberByGidAndUmtId(int groupId, String umtId);
	void update(SmsGroupMember m);
	List<SmsGroupMember> getGroupMemberByUmtId(String umtId);
	List<SmsGroupMember> getgroupMemberByGroupId(int groupId);
	void changeMemberRole(int memberId, boolean isAdmin);
	SmsGroupMember getgroupMemberByGidAndId(int gid, int memberId);
}
