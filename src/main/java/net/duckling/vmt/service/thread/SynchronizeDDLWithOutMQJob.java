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
package net.duckling.vmt.service.thread;

import java.util.List;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.domain.ddl.DDLGroup;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IDDLService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.web.ConsoleController;

/**
 * @author lvly
 * @since 2013-5-31
 */
public class SynchronizeDDLWithOutMQJob implements Jobable{
	private String umtId;
	private IGroupService groupService;
	private IDDLService ddlService;
	private IUserService userService;
	private IVmtIndexService indexService;
	private IAttributeService attrService;
	
	/**
	 * @param teamCode 团队编号 ddl提供
	 * @param umtId 触发者的id
	 */
	public SynchronizeDDLWithOutMQJob(String umtId){
		this.groupService=BeanFactory.getBean(IGroupService.class);
		this.ddlService=BeanFactory.getBean(IDDLService.class);
		this.userService=BeanFactory.getBean(IUserService.class);
		this.indexService=BeanFactory.getBean(IVmtIndexService.class);
		this.attrService=BeanFactory.getBean(IAttributeService.class);
		this.umtId=umtId;
	}
	private void syncATeam(String teamCode){
		DDLGroup group=ddlService.getTeamInfo(teamCode);
		LdapGroup destGroup = groupService.getGroupBySymbol(group.getTeamCode(),null);
		if (destGroup==null){
			ConsoleController.write("start team【"+group.getTeamName()+"】 create");
			destGroup=new LdapGroup();
			destGroup.setCreator(umtId);
			destGroup.setAdmins(group.getAdmin());
			destGroup.setName(group.getTeamName());
			destGroup.setSymbol(group.getTeamCode());
			destGroup.setFrom(LdapNode.FROM_DDL);
			groupService.createGroup(destGroup, false);
		} else{
			ConsoleController.write("start team【"+group.getTeamName()+"】 update");
			destGroup = groupService.getGroupBySymbol(group.getTeamCode(),null);
			if(!LdapNode.FROM_DDL.equals(destGroup.getFrom())){
				ConsoleController.write("it is from["+destGroup.getFrom()+"] ,not a ddl group");
				return;
			}
			if(!CommonUtils.isNull(group.getTeamName())&&!group.getTeamName().equals(destGroup.getName())){
				attrService.update(destGroup.getDn(), "vmt-name", group.getTeamName());
			}else if(CommonUtils.isNull(group.getTeamName())){
				ConsoleController.write("why name is null?:"+group.getTeamCode());
			}else if(group.getTeamName().equals(destGroup.getName())){
				ConsoleController.write(" name is not changed");
			}
			if(!CommonUtils.isNull(group.getAdmin())){
				if(!CommonUtils.isNull(destGroup.getAdmins())){
					attrService.delete(destGroup.getDn(), "vmt-admin", destGroup.getAdmins());
				}
				attrService.insert(destGroup.getDn(), "vmt-admin", group.getAdmin());
			}else{
				ConsoleController.write("why admin is Null?:"+group.getTeamCode());
			}
		}
		if(!CommonUtils.isNull(group.getUsers())){
			ConsoleController.write("delete member...");
			groupService.deleteAllMember(destGroup.getDn());
			ConsoleController.write("add member,size="+group.getUsers().length+"...");
			userService.addUserToNodeUsed(destGroup.getDn(), group.getUsers(), LdapUser.STATUS_ACTIVE,false);
		}
		if(destGroup!=null){
			ConsoleController.write("build index...");
			indexService.buildAIndexJob(destGroup.getDn());
		}
	}
	@Override
	public void doJob() {
		List<String> groupTeamCodes = ddlService.getAllTeamCode();
		int index=0;
		int count=groupTeamCodes.size();
		for (String teamCode : groupTeamCodes) {
			long start=System.currentTimeMillis();
			ConsoleController.write("["+(++index)+"/"+count+"]teamCode:"+teamCode+" start！");
			try{
			syncATeam(teamCode);
			}catch(Exception e){
				ConsoleController.write(e);
			}
			long end=System.currentTimeMillis();
			ConsoleController.write("teamCode:"+teamCode+" ended！ cost:"+((end-start)/1000)+"s");
		}
		ConsoleController.writeOver();
	}

	@Override
	public String getJobId() {
		return DDL_JOB+".without.mq";
	}
	@Override
	public boolean isJobEquals(Jobable job) {
		if(job!=null){
			return this.getJobId().equals(job.getJobId());
		}
		return false;
	}

}
