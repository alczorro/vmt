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
import net.duckling.vmt.service.mq.MQMessageSenderExt;

import org.apache.log4j.Logger;

/**
 * @author lvly
 * @since 2013-5-31
 */
public class SynchronizeDDLJob implements Jobable{
	private static final Logger LOGGER=Logger.getLogger(SynchronizeDDLJob.class);
	private String teamCode;
	private String umtId;
	private IGroupService groupService;
	private IDDLService ddlService;
	private IUserService userService;
	private IVmtIndexService indexService;
	private IAttributeService attrService;
	private boolean createRootOnly;
	private MQMessageSenderExt sender;
	
	/**
	 * @param teamCode 团队编号 ddl提供
	 * @param umtId 触发者的id
	 */
	public SynchronizeDDLJob(String teamCode,String umtId,boolean createRootOnly){
		this.teamCode=teamCode;
		this.umtId=umtId;
		this.groupService=BeanFactory.getBean(IGroupService.class);
		this.ddlService=BeanFactory.getBean(IDDLService.class);
		this.userService=BeanFactory.getBean(IUserService.class);
		this.indexService=BeanFactory.getBean(IVmtIndexService.class);
		this.createRootOnly=createRootOnly;
		this.attrService=BeanFactory.getBean(IAttributeService.class);
		this.sender=BeanFactory.getBean(MQMessageSenderExt.class);
	}
	@Override
	public void doJob() {
		DDLGroup group=ddlService.getTeamInfo(teamCode);
		LdapGroup destGroup = groupService.getGroupBySymbol(group.getTeamCode(),null);
		if (destGroup==null){
			destGroup=new LdapGroup();
			destGroup.setCreator(umtId);
			destGroup.setAdmins(group.getAdmin());
			destGroup.setName(group.getTeamName());
			destGroup.setSymbol(group.getTeamCode());
			destGroup.setFrom(LdapNode.FROM_DDL);
			groupService.createGroup(destGroup, false);
		} else{
			destGroup = groupService.getGroupBySymbol(group.getTeamCode(),null);
			if(!LdapNode.FROM_DDL.equals(destGroup.getFrom())){
				LOGGER.error("it is from["+destGroup.getFrom()+"] ,not a ddl group");
				return;
			}
			if(!CommonUtils.isNull(group.getTeamName())&&!group.getTeamName().equals(destGroup.getName())){
				attrService.update(destGroup.getDn(), "vmt-name", group.getTeamName());
			}else if(CommonUtils.isNull(group.getTeamName())){
				LOGGER.error("why name is null?:"+group.getTeamCode());
			}else if(group.getTeamName().equals(destGroup.getName())){
				LOGGER.info(" name is not changed");
			}
			if(!CommonUtils.isNull(group.getAdmin())){
				if(!CommonUtils.isNull(destGroup.getAdmins())){
					attrService.delete(destGroup.getDn(), "vmt-admin", destGroup.getAdmins());
				}
				attrService.insert(destGroup.getDn(), "vmt-admin", group.getAdmin());
			}else{
				LOGGER.error("why admin is Null?:"+group.getTeamCode());
			}
		}
		if(createRootOnly){
			return;
		}
		if(!CommonUtils.isNull(group.getUsers())){
			groupService.deleteAllMember(destGroup.getDn());
			userService.addUserToNodeUsed(destGroup.getDn(), group.getUsers(), LdapUser.STATUS_ACTIVE,false);
		}
		if(destGroup!=null){
			indexService.buildAIndexJob(destGroup.getDn());
			sender.sendRefreshMessage(destGroup.getDn());
		}
		
	}

	@Override
	public String getJobId() {
		return DDL_JOB+this.teamCode;
	}
	@Override
	public boolean isJobEquals(Jobable job) {
		if(job!=null){
			return this.getJobId().equals(job.getJobId());
		}
		return false;
	}

}
