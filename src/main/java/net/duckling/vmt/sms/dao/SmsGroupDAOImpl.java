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

import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.sms.domain.SmsGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class SmsGroupDAOImpl implements ISmsGroupDAO{
	
	@Autowired
	private BaseDAO<SmsGroup> groupDAO;
	@Override
	public SmsGroup getGroupById(int gid) {
		SmsGroup smsG=new SmsGroup();
		smsG.setId(gid);
		return groupDAO.selectOne(smsG);
	}
	
	@Override
	public List<SmsGroup> findAll() {
		SmsGroup smsG=new SmsGroup();
		return groupDAO.select(smsG);
	}
	@Override
	public int create(SmsGroup smsGroup) {
		return groupDAO.insert(smsGroup);
	}
	@Override
	public void update(SmsGroup gupdate) {
		groupDAO.update(gupdate);
	}
	@Override
	public boolean isAccountUsed(String trim) {
		SmsGroup group=new SmsGroup();
		group.setAccount(trim);
		return groupDAO.getCount(group)>0;
	}
	@Override
	public void removeGropyById(int gid) {
		SmsGroup group=new SmsGroup();
		group.setId(gid);
		groupDAO.delete(group);
	}
	@Override
	public void plusSmsUsedByID(int groupId,int count) {
		String sql="update `vmt_sms_group` set sms_used=sms_used+:count where id=:id";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("count", count);
		map.put("id",groupId);
		groupDAO.getTmpl().update(sql, map);
		
	}
	
}
