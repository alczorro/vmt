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
package net.duckling.vmt.dao;

import java.util.List;

import net.duckling.vmt.domain.VmtApp;

public interface IVmtAppDAO {
	
	void addApp(VmtApp app);
	
	void deleteApp(VmtApp app);
	
	List<VmtApp> searchAppByTeamSymbol(String symbol);

	List<VmtApp> searchAppByTeamDn(String teamDN);

	void addAppBatch(List<VmtApp> apps);

	boolean isOauthAppAdded(String clientId, String dn);

	VmtApp getAppById(int appId);

	void updateApp(VmtApp app);

	boolean checkAppIdAndDn(String dn, int appId);

	List<VmtApp> searchAppByTeamDns(List<String> orgDNS);
}
