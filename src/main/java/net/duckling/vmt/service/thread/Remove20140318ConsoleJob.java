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

import java.util.Arrays;
import java.util.List;

import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.dao.IOrgDAO;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.web.ConsoleController;

public class Remove20140318ConsoleJob implements Jobable{
	private IOrgDAO orgDAO;
	private IAttributeService attrService;
	public Remove20140318ConsoleJob(){
		this.orgDAO=BeanFactory.getBean(IOrgDAO.class);
		this.attrService=BeanFactory.getBean(IAttributeService.class);
	}
	@Override
	public void doJob() {
		List<LdapOrg> orgs=orgDAO.getAllOrgs();
		int size=orgs.size();
		int index=0;
		for(LdapOrg org:orgs){
			try{
				ConsoleController.write("["+(++index)+"/"+size+"]"+org.getDn()+"=>删除域名,是否CoreMail,是否院内用户字段");
				attrService.update(org.getDn(),"vmt-open-dchat",true);
				if(org.getDomains()!=null){
					ConsoleController.write("删除域名脏数据:"+Arrays.toString(org.getDomains()));
					attrService.delete(org.getDn(), "vmt-domain", org.getDomains());
				}
				ConsoleController.write("删除是否CoreMail:"+false);
				attrService.update(org.getDn(), "vmt-is-coremail", false);
				ConsoleController.write("删除是否 院内用户:"+false);
				attrService.update(org.getDn(), "vmt-is-cas", false);
			}catch(Exception e){
				ConsoleController.write(e);
			}
		}
		ConsoleController.writeOver();
		
	}

	@Override
	public String getJobId() {
		return "remove-domain-data-20140318";
	}

	@Override
	public boolean isJobEquals(Jobable job) {
		return this.getJobId().equals(job.getJobId());
	}
	

}
