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
package net.duckling.vmt.dao.impl;

import java.util.HashMap;
import java.util.Map;

import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IVmtAppSwitchDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppSwitchJDBCDAOImpl implements IVmtAppSwitchDAO{
	@Autowired
	private BaseDAO<?> baseDAO;

	private static final String TABLE_NAME="`vmt_app_switch`";
	@Override
	public boolean isExists(String teamDn) {
		String sql="select count(*) from "+TABLE_NAME+" where team_dn=:teamDN";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("teamDN", teamDn);
		return baseDAO.getTmpl().queryForInt(sql,map)>0;
	}

	@Override
	public void insert(String teamDn) {
		String sql="insert into "+TABLE_NAME+"(`team_dn`) values(:teamDN)";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("teamDN", teamDn);
		baseDAO.getTmpl().update(sql, map);
		
	}

	@Override
	public void delete(String teamDn) {
		String sql="delete from "+TABLE_NAME+" where team_dn=:teamDN";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("teamDN", teamDn);
		baseDAO.getTmpl().update(sql, map);
	}
	@Override
	public void deleteAll() {
		String sql="delete from "+TABLE_NAME;
		Map<String,Object> map=new HashMap<String,Object>();
		baseDAO.getTmpl().update(sql, map);
	}
}
