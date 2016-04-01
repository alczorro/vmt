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
import net.duckling.vmt.sms.domain.SmsGetter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SmsGetterDAOImpl implements ISmsGetterDAO{
	@Autowired
	private BaseDAO<SmsGetter> baseDAO;
	@Override
	public void insert(List<SmsGetter> getters) {
		baseDAO.batchAdd(getters);
	}
	@Override
	public List<SmsGetter> getSmsGetter(SmsGetter g) {
		return baseDAO.select(g);
	}
	@Override
	public void updateResStat(SmsGetter g) {
		String sql="update vmt_sms_getter set stat=:stat where sms_uuid=:uuid and mobile=:mobile";
		Map<String,Object> m=new HashMap<String,Object>();
		m.put("stat", g.getStat());
		m.put("uuid", g.getSmsUuid());
		m.put("mobile", g.getMobile());
		baseDAO.getTmpl().update(sql, m);
	}
	@Override
	public void updateResStatByMobile(String mobile,String status) {
		String sql="update vmt_sms_getter set stat=:stat where mobile=:mobile and stat='r:000' order by id limit 1";
		Map<String,Object> m=new HashMap<String,Object>();
		m.put("stat", status);
		m.put("mobile", mobile);
		baseDAO.getTmpl().update(sql, m);
	}
}
