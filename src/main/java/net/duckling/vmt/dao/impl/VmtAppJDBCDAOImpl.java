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
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IVmtAppDAO;
import net.duckling.vmt.domain.VmtApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VmtAppJDBCDAOImpl implements IVmtAppDAO{
	@Autowired
	private BaseDAO<VmtApp> baseDAO;

	@Override
	public void addApp(VmtApp app) {
		baseDAO.insert(app);
	}

	@Override
	public void deleteApp(VmtApp app) {
		app.setAppType(null);
		baseDAO.delete(app);
	}

	@Override
	public List<VmtApp> searchAppByTeamSymbol(String symbol) {
		VmtApp vmtApp=new VmtApp();
		vmtApp.setAppType(null);
		vmtApp.setTeamSymbol(symbol);
		return baseDAO.select(vmtApp,"order by `id`");
	}
	@Override
	public List<VmtApp> searchAppByTeamDn(String teamDN) {
		VmtApp vmtApp=new VmtApp();
		vmtApp.setAppType(null);
		vmtApp.setTeamDn(teamDN);
		return baseDAO.select(vmtApp,"order by `id`");
	}
	@Override
	public void addAppBatch(List<VmtApp> apps) {
	    baseDAO.batchAdd(apps);
	}
	@Override
	public boolean isOauthAppAdded(String clientId, String dn) {
		VmtApp vmtApp=new VmtApp();
		vmtApp.setAppType(VmtApp.APP_TYPE_OAUTH);
		vmtApp.setTeamDn(dn);
		vmtApp.setAppClientId(clientId);
		return baseDAO.getCount(vmtApp)>0;
	}
	@Override
	public VmtApp getAppById(int appId) {
		VmtApp vmtApp=new VmtApp();
		vmtApp.setAppType(null);
		vmtApp.setId(appId);
		return baseDAO.selectOne(vmtApp);
	}
	@Override
	public void updateApp(VmtApp app) {
		app.setAppType(null);
		baseDAO.update(app);
	}
	@Override
	public boolean checkAppIdAndDn(String dn, int appId) {
		VmtApp vmtApp=new VmtApp();
		vmtApp.setAppType(null);
		vmtApp.setId(appId);
		vmtApp.setTeamDn(dn);
		return baseDAO.getCount(vmtApp)>0;
	}
	@Override
	public List<VmtApp> searchAppByTeamDns(List<String> orgDNS) {
		String sql="select * from vmt_app a,vmt_app_switch s where a.team_dn=s.team_dn and a.team_dn in(";
		Map<String,Object> params=new HashMap<String,Object>();
		int index=0;
		for(String dn:orgDNS){
			if(index!=0){
				sql+=",";
			}
			sql+=":dns"+index;
			params.put("dns"+index, dn);
			index++;
		}
		sql+=")";
		return baseDAO.getTmpl().query(sql, params, baseDAO.getORMParser(VmtApp.class).getRowMapper());
	}
}
