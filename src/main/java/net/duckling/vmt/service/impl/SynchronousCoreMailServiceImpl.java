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

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.ISynchronousCoreMailService;
import net.duckling.vmt.service.thread.JobThread;
import net.duckling.vmt.service.thread.SynchronizeCoreMailCreateJob;
import net.duckling.vmt.service.thread.SynchronizeCoreMailUpdateJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-5-29
 */
@Service
public class SynchronousCoreMailServiceImpl implements ISynchronousCoreMailService{
	@Autowired
	private ICoreMailService coreMailService;
	@Autowired
	private IOrgService orgService;
	

	@Override
	public void synchrounousByDomain(String domain,String umtId) {
		if(CommonUtils.isNull(domain)){
			return;
		}
		String[] orgIds=coreMailService.getOrgIdFromDomain(domain);
		if(!CommonUtils.isNull(orgIds)){
			doSync(orgIds,umtId,false);
		}
	}
	@Override
	public void synchrounousByOrgId(String orgId, String umtId) {
		doSync(new String[]{orgId},umtId,false);
		
	}
	private void doSync(String[] orgIds,String umtId,boolean rootOnly){
		for(String orgId:orgIds){
			String orgDN=orgService.getThirdPartyOrg(orgId,LdapNode.FROM_CORE_MAIL);
			if(CommonUtils.isNull(orgDN)){
				JobThread.addJobThread((new SynchronizeCoreMailCreateJob(orgId, umtId,rootOnly)));
			}else {
				JobThread.addJobThread(new SynchronizeCoreMailUpdateJob(orgId, orgDN,umtId,rootOnly));
			}
		}
	}
	@Override
	public void synchrounouseByDomainIfNotExists(String domain, String umtId) {
		String[] orgIds=coreMailService.getOrgIdFromDomain(domain);
		if(CommonUtils.isNull(orgIds)){
			return;
		}
		for(String orgId:orgIds){
			String orgDN=orgService.getThirdPartyOrg(orgId,LdapNode.FROM_CORE_MAIL);
			if(CommonUtils.isNull(orgDN)){
				JobThread.addJobThread((new SynchronizeCoreMailCreateJob(orgId, umtId,false)));
			}
		}
	}
	@Override
	public synchronized void synchrounousAll(String umtId,boolean rootOnly) {
		String[] orgIds=coreMailService.getAllOrgId();
		if(!CommonUtils.isNull(orgIds)){
			doSync(orgIds,umtId,rootOnly);
		}
		
	}

}
