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
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.web.ConsoleController;

public class ReomveAllAppsConsoleJob implements Jobable{

	@Override
	public void doJob() {
		IOrgDAO orgDAO=BeanFactory.getBean(IOrgDAO.class);
		IGroupDAO groupDAO=BeanFactory.getBean(IGroupDAO.class);
		IAttributeService attrService=BeanFactory.getBean(IAttributeService.class);
		List<LdapOrg> orgs=orgDAO.getAllOrgs();
		for(LdapOrg org:orgs){
			if(!CommonUtils.isNull(org.getVmtAppCopy())){
				ConsoleController.write("删除组织["+LdapUtils.removeDangerous(org.getName())+"]的应用信息...");
				attrService.delete(org.getDn(),"vmt-apps-copy",new String[]{org.getVmtAppCopy()});
				ConsoleController.write("已删除冗余:"+org.getVmtAppCopy());
			}
			if(org.isAppsOpen()){
				attrService.update(org.getDn(),"vmt-is-apps-open", false);
			}
		}
		List<LdapGroup> groups=groupDAO.getAllGroups();
		for(LdapGroup group:groups){
			if(!CommonUtils.isNull(group.getVmtAppCopy())){
				ConsoleController.write("删除群组["+LdapUtils.removeDangerous(group.getName())+"]的应用信息...");
				attrService.delete(group.getDn(),"vmt-apps-copy",new String[]{group.getVmtAppCopy()});
				ConsoleController.write("已删除冗余:"+group.getVmtAppCopy());
			}
			if(group.isAppsOpen()){
				attrService.update(group.getDn(),"vmt-is-apps-open", false);
			}
		}
		ConsoleController.writeOver();
	}
	
	@Override
	public String getJobId() {
		return "console.remove.all.apps.job";
	}

	@Override
	public boolean isJobEquals(Jobable job) {
		return this.getJobId().equals(job.getJobId());
	}

}
