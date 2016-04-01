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

import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.dao.IGroupDAO;
import net.duckling.vmt.dao.IOrgDAO;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IVmtAppSwitchService;
import net.duckling.vmt.web.ConsoleController;

public class SetAllAppOpenJob implements Jobable{

	@Override
	public void doJob() {
		IOrgDAO orgDAO=BeanFactory.getBean(IOrgDAO.class);
		IGroupDAO groupDAO=BeanFactory.getBean(IGroupDAO.class);
		IAttributeService attrService=BeanFactory.getBean(IAttributeService.class);
		IVmtAppSwitchService switchService=BeanFactory.getBean(IVmtAppSwitchService.class);
		switchService.deleteAll();
		List<LdapOrg> orgs=orgDAO.getAllOrgs();
		
		for(LdapOrg org:orgs){
			ConsoleController.write("组织结构:"+org.getName()+","+org.getDn());
			attrService.update(org.getDn(),"vmt-is-apps-open",true);
			switchService.insert(org.getDn());
		}
		List<LdapGroup> groups=groupDAO.getAllGroups();
		for(LdapGroup group:groups){
			ConsoleController.write("团队:"+group.getName()+","+group.getDn());
			attrService.update(group.getDn(),"vmt-is-apps-open",true);
			switchService.insert(group.getDn());
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
