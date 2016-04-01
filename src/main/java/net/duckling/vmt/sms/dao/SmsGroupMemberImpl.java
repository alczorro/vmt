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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.sms.domain.SmsGroupMember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class SmsGroupMemberImpl implements ISmsGroupMemberDAO {
	@Autowired
	private BaseDAO<SmsGroupMember> baseDAO;

	@Override
	public List<SmsGroupMember> getMemberByGroupId(List<Integer> groupIds) {
		if(CommonUtils.isNull(groupIds)){
			return null;
		}
		StringBuffer sb=new StringBuffer();
		sb.append("select * from `vmt_sms_group_member` where sms_group_id in(");
		int index=0;
		for(int id:groupIds){
			if((index++)!=0){
				sb.append(",");
			}
			sb.append(id);
		}
		sb.append(")");
		return baseDAO.getTmpl().query(sb.toString(),new HashMap<String,Object>() , baseDAO.getORMParser(SmsGroupMember.class).getRowMapper());
	}

	@Override
	public List<SmsGroupMember> getMemberAll() {
		return baseDAO.select(new SmsGroupMember());
	}
	@Override
	public int addMember(SmsGroupMember m) {
		return baseDAO.insert(m);
	}
	@Override
	public int getAdminCount(int groupId) {
		SmsGroupMember m=new SmsGroupMember();
		m.setGroupId(groupId);
		m.setAdmin(true);
		return baseDAO.getCount(m);
	}
	@Override
	public void removeMember(int mid) {
		SmsGroupMember m=new SmsGroupMember();
		m.setId(mid);
		baseDAO.delete(m);
	}
	@Override
	public SmsGroupMember getGroupMemberByGidAndUmtId(int groupId, String umtId) {
		SmsGroupMember m=new SmsGroupMember();
		m.setGroupId(groupId);
		m.setUmtId(umtId);
		return baseDAO.selectOne(m);
	}
	@Override
	public void update(SmsGroupMember m){
		baseDAO.update(m);
	}
	@Override
	public List<SmsGroupMember> getGroupMemberByUmtId(String umtId) {
		SmsGroupMember m=new SmsGroupMember();
		m.setUmtId(umtId);
		return baseDAO.select(m,"order by id desc");
	}
	@Override
	public List<SmsGroupMember> getgroupMemberByGroupId(int groupId) {
		SmsGroupMember m=new SmsGroupMember();
		m.setGroupId(groupId);
		return baseDAO.select(m);
	}
	@Override
	public void changeMemberRole(int memberId, boolean isAdmin) {
		String sql="update vmt_sms_group_member set m_is_admin=:isAdmin where id=:id";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", memberId);
		map.put("isAdmin", isAdmin);
		baseDAO.getTmpl().update(sql,map);
	}
	@Override
	public SmsGroupMember getgroupMemberByGidAndId(int gid, int memberId) {
		SmsGroupMember m=new SmsGroupMember();
		m.setGroupId(gid);
		m.setId(memberId);
		return CommonUtils.first(baseDAO.select(m));
	}
}
