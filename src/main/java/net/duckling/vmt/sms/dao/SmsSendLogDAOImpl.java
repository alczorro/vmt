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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.sms.domain.SmsSendLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SmsSendLogDAOImpl implements ISmsSendLogDAO {
	@Autowired
	private BaseDAO<SmsSendLog> baseDAO;

	@Override
	public void insert(SmsSendLog log) {
		baseDAO.insert(log);
	}
	@Override
	public List<SmsSendLog> getLogGroupByuserName(int groupId,String umtId) {
		String sql="select sender_name n,sender_cstnet_id c,sum(send_success_count) s from vmt_sms_send_log where group_id=:gid";
		if(!CommonUtils.isNull(umtId)){
			sql+=" and sender_umt_id=:umtId ";
		}
		sql+=" group by sender_cstnet_id having s>0";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("gid",groupId);
		map.put("umtId", umtId);
		return baseDAO.getTmpl().query(sql, map, new RowMapper<SmsSendLog>() {
			@Override
			public SmsSendLog mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				SmsSendLog ssl=new SmsSendLog(); 
				ssl.setSenderName(rs.getString("n"));
				ssl.setSenderCstnetId(rs.getString("c"));
				ssl.setSendCount(rs.getInt("s"));
				return ssl;
			}
		});
	}
}
