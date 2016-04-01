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
/**
 * 
 */
package net.duckling.vmt.service.impl;

import java.util.List;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.service.IDDLService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.ISynchronousDDLService;
import net.duckling.vmt.service.thread.JobThread;
import net.duckling.vmt.service.thread.SynchronizeDDLJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-5-30
 */
@Service
public class SynchronousDDLServiceImpl implements ISynchronousDDLService {
	@Autowired
	private IDDLService ddlService;
	@Autowired
	private IGroupService groupService;
	@Override
	public void synchrounousAGroup(String teamCode,String umtId,boolean rootOnly) {
		JobThread.addJobThread(new SynchronizeDDLJob(teamCode, umtId,rootOnly));
	}

	@Override
	public void synchrounousAll(String umtId,boolean rootOnly) {
		List<String> groupTeamCodes = ddlService.getAllTeamCode();
		for (String teamCode : groupTeamCodes) {
			synchrounousAGroup(teamCode,umtId,rootOnly);
		}
	}
	@Override
	public void synchrounousIfExists(String uid,String umtId) {
		List<String> groupTeamCodes = ddlService.getMyTemCode(uid);
		if(CommonUtils.isNull(groupTeamCodes)){
			return;
		}
		for (String teamCode : groupTeamCodes) {
			LdapGroup destGroup=groupService.getGroupBySymbol(teamCode, LdapNode.FROM_DDL);
			if(destGroup==null){
				synchrounousAGroup(teamCode,umtId,false);
			}
		}
		
	}
}
