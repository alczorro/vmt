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
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.dao.IGroupDAO;
import net.duckling.vmt.dao.IOrgDAO;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.web.ConsoleController;

public class RemoveLogoConsoleJob implements Jobable {
	private IOrgDAO orgDAO;
	private IGroupDAO groupDAO;
	private IAttributeService attrService;
	public RemoveLogoConsoleJob(){
		orgDAO=BeanFactory.getBean(IOrgDAO.class);
		groupDAO=BeanFactory.getBean(IGroupDAO.class);
		attrService=BeanFactory.getBean(IAttributeService.class);
	}
	@Override
	public void doJob() {
		List<LdapOrg> orgs=orgDAO.getAllOrgs();
		for(LdapOrg org:orgs){
			ConsoleController.write("删除组织["+LdapUtils.removeDangerous(org.getName())+"]的Logo...");
			if(org.getLogoId()>0){
				ConsoleController.write("----通用Logo:"+org.getLogoId()+"");
				attrService.delete(org.getDn(),"vmt-logo",new String[]{ org.getLogoId()+""});
			}
			if(org.getMobileLogo()>0){
				ConsoleController.write("----移动Logo:"+org.getMobileLogo()+"");
				attrService.delete(org.getDn(),"vmt-mobile-logo",new String[]{ org.getMobileLogo()+""});
			}
			if(org.getPcLogo()>0){
				ConsoleController.write("----pc的Logo:"+org.getPcLogo()+"");
				attrService.delete(org.getDn(),"vmt-pc-logo",new String[]{ org.getPcLogo()+""});

			}
		}
		List<LdapGroup> groups=groupDAO.getAllGroups();
		for(LdapGroup group:groups){
			ConsoleController.write("删除群组["+LdapUtils.removeDangerous(group.getName())+"]的Logo...");
			if(group.getLogoId()>0){
				ConsoleController.write("----通用Logo:"+group.getLogoId()+"");
				attrService.delete(group.getDn(),"vmt-logo",new String[]{ group.getLogoId()+""});
			}
			if(group.getMobileLogo()>0){
				ConsoleController.write("----移动Logo:"+group.getMobileLogo()+"");
				attrService.delete(group.getDn(),"vmt-mobile-logo",new String[]{ group.getMobileLogo()+""});
			}
			if(group.getPcLogo()>0){
				ConsoleController.write("----pc的Logo:"+group.getPcLogo()+"");
				attrService.delete(group.getDn(),"vmt-pc-logo",new String[]{ group.getPcLogo()+""});
			}
		}
		ConsoleController.writeOver();
	}

	@Override
	public String getJobId() {
		return "remove.job.console";
	}

	@Override
	public boolean isJobEquals(Jobable job) {
		return this.getJobId().equals(job.getJobId());
	}

}
