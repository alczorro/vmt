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
package net.duckling.vmt.service.thread;

import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.dao.IGroupDAO;
import net.duckling.vmt.dao.IOrgDAO;
import net.duckling.vmt.domain.VmtApp;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IVmtAppService;
import net.duckling.vmt.service.IVmtAppSwitchService;
import net.duckling.vmt.web.ConsoleController;

public class ImportAllAppsConsoleJob implements Jobable{

	@Override
	public void doJob() {
		IOrgDAO orgDAO=BeanFactory.getBean(IOrgDAO.class);
		IGroupDAO groupDAO=BeanFactory.getBean(IGroupDAO.class);
		IAttributeService attrService=BeanFactory.getBean(IAttributeService.class);
		IVmtAppService appService=BeanFactory.getBean(IVmtAppService.class);
		IVmtAppSwitchService switchService=BeanFactory.getBean(IVmtAppSwitchService.class);
		List<LdapOrg> orgs=orgDAO.getAllOrgs();
		for(LdapOrg org:orgs){
			List<VmtApp> apps=appService.searchAppByTeamDn(org.getDn());
			if(!CommonUtils.isNull(apps)){
				ConsoleController.write("添加组织["+LdapUtils.removeDangerous(org.getName())+"]的应用信息...");
				String jsonStr=VmtApp.toJson(apps);
				ConsoleController.write("应用信息:"+jsonStr);
				attrService.update(org.getDn(),"vmt-apps-copy",jsonStr);
			}
			attrService.update(org.getDn(),"vmt-is-apps-open",switchService.isExits(org.getDn()));
		}
		List<LdapGroup> groups=groupDAO.getAllGroups();
		for(LdapGroup group:groups){
			List<VmtApp> apps=appService.searchAppByTeamDn(group.getDn());
			if(!CommonUtils.isNull(apps)){
				ConsoleController.write("添加群组["+LdapUtils.removeDangerous(group.getName())+"]的应用信息...");
				String jsonStr=VmtApp.toJson(apps);
				ConsoleController.write("应用信息:"+jsonStr);
				attrService.update(group.getDn(),"vmt-apps-copy",jsonStr);
			}
			attrService.update(group.getDn(),"vmt-is-apps-open",switchService.isExits(group.getDn()));
		}
		ConsoleController.writeOver();
	}
	
	@Override
	public String getJobId() {
		return "console.import.all.apps.job";
	}

	@Override
	public boolean isJobEquals(Jobable job) {
		return this.getJobId().equals(job.getJobId());
	}

}
