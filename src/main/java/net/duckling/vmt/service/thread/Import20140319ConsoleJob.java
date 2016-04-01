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

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.domain.index.OrgDetail;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.INodeService;
import net.duckling.vmt.service.IOrgDomainMappingService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.web.ConsoleController;

public class Import20140319ConsoleJob implements Jobable{
	private IOrgDomainMappingService domainService;
	private IOrgService orgService;
	private IAttributeService attrService;
	private MQMessageSenderExt sender;
	private INodeService nodeService;
	private IVmtIndexService indexService;
	public Import20140319ConsoleJob(){
		this.domainService=BeanFactory.getBean(IOrgDomainMappingService.class);
		this.orgService=BeanFactory.getBean(IOrgService.class);
		this.attrService=BeanFactory.getBean(IAttributeService.class);
		this.sender=BeanFactory.getBean(MQMessageSenderExt.class);
		this.nodeService=BeanFactory.getBean(INodeService.class);
		this.indexService=BeanFactory.getBean(IVmtIndexService.class);
	}
	@Override
	public void doJob() {
		List<OrgDetail> result=domainService.findAllOrgDetails();
		int size=result.size();
		for(int i=0;i<size;i++){
			try{
				OrgDetail od=result.get(i);
				LdapOrg org=orgService.getOrgBySymbol(od.getSymbol());
				if(org==null){
					ConsoleController.write("["+(i+1)+"/"+size+"]未找到 id 为"+od.getSymbol()+"的组织");
				}else{
					ConsoleController.write("["+(i+1)+"/"+size+"]找到id为["+od.getSymbol()+"]组织["+od.getName()+"] ,开始更新....");
					if(org.getDomains()!=null){
						ConsoleController.write("删除脏数据域名:"+Arrays.toString(org.getDomains()));
						attrService.delete(org.getDn(), "vmt-domain", org.getDomains());
					}
					if(!org.getName().equals(od.getName())){
						ConsoleController.write("发现团队名称已变更 原名称:"+org.getName()+",更名后:"+od.getName());
						String beforeName=org.getCurrentDisplay();
						attrService.update(org.getDn(), "vmt-name", od.getName());
						nodeService.updateSonAndSelfDisplayName(org.getDn(), od.getName());
						indexService.updateTeamName(org.getDn(), od.getName());
						sender.sendUpdateMessage(org.getDn(),beforeName);
					}
					ConsoleController.write("插入新域名:"+Arrays.toString(od.getDomainsByArray()));
					if(!CommonUtils.isNull(od.getDomainsByArray())){
						for(String domain:od.getDomainsByArray()){
							attrService.insert(org.getDn(), "vmt-domain", new String[]{domain});
						}
					}
					
					ConsoleController.write("更新是否为coreMail:"+od.isCoreMail());
					attrService.update(org.getDn(), "vmt-is-coremail", od.isCoreMail());
					ConsoleController.write("更新是否院内用户:"+od.isCas());
					attrService.update(org.getDn(), "vmt-is-cas", od.isCas());
				}
			}catch(Exception e){
				ConsoleController.write(e);
			}
		}
		ConsoleController.writeOver();
	}

	@Override
	public String getJobId() {
		return "import-2014-20140318-data";
	}

	@Override
	public boolean isJobEquals(Jobable job) {
		return this.getJobId().equals(job.getJobId());
	}

}
