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
package net.duckling.vmt.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.dao.IVmtAppDAO;
import net.duckling.vmt.domain.VmtApp;
import net.duckling.vmt.service.IVmtAppService;
import net.duckling.vmt.service.IVmtAppSwitchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class VmtAppServiceImpl implements IVmtAppService{
	@Autowired
	private IVmtAppDAO vmtAppDAO;
	@Autowired
	private IVmtAppSwitchService vmtAppSwitchService;

	@Override
	public void addAppBatch(List<VmtApp> apps) {
		vmtAppDAO.addAppBatch(apps);
	}
	@Override
	public void addApp(VmtApp app) {
		vmtAppDAO.addApp(app);
		vmtAppSwitchService.ifNotExitsThenInsert(app.getTeamDn());
	}

	@Override
	public void deleteApp(VmtApp app) {
		vmtAppDAO.deleteApp(app);
	}

	@Override
	public List<VmtApp> searchAppByTeamSymbol(String symbol) {
		return vmtAppDAO.searchAppByTeamSymbol(symbol);
	}
	@Override
	public List<VmtApp> searchAppByTeamDn(String teamDN) {
		return vmtAppDAO.searchAppByTeamDn(teamDN);
	}
	@Override
	public boolean isOauthAppAdded(String clientId, String dn) {
		return vmtAppDAO.isOauthAppAdded(clientId,dn);
	}
	@Override
	public VmtApp getAppById(int appId) {
		return vmtAppDAO.getAppById(appId);
	}
	@Override
	public void updateApp(VmtApp app) {
		vmtAppDAO.updateApp(app);
	}
	
	@Override
	public boolean checkAppIdAndDn(String dn,int appId) {
		return vmtAppDAO.checkAppIdAndDn(dn,appId);
	}
	@Override
	public List<VmtApp> searchAppByTeamDnsNoDistinct(List<String> orgDNS) {
		if(CommonUtils.isNull(orgDNS)){
			return new ArrayList<VmtApp>();
		}
		List<VmtApp> apps=vmtAppDAO.searchAppByTeamDns(orgDNS);
		if(CommonUtils.isNull(apps)){
			return new ArrayList<VmtApp>();
		}
		return apps;
	}
	@Override
	public List<VmtApp> distinctApp(List<VmtApp> apps){
		Set<String> clientIds=new HashSet<String>();
		for(Iterator<VmtApp> it=apps.iterator();it.hasNext();){
			VmtApp app=it.next();
			if(app.getAppType().equals(VmtApp.APP_TYPE_OAUTH)){
				if(clientIds.contains(app.getAppClientId())){
					it.remove();
				}else{
					clientIds.add(app.getAppClientId());
				}
			}
		}
		return apps;
	}

}
