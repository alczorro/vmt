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
package net.duckling.vmt.service.mq;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.falcon.api.mq.DFMQFactory;
import net.duckling.falcon.api.mq.DFMQMode;
import net.duckling.falcon.api.mq.IDFPublisher;
import net.duckling.vmt.api.domain.VmtDepart;
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.api.domain.message.MQBaseMessage;
import net.duckling.vmt.api.domain.message.MQCreateDepartMessage;
import net.duckling.vmt.api.domain.message.MQCreateGroupMessage;
import net.duckling.vmt.api.domain.message.MQCreateOrgMessage;
import net.duckling.vmt.api.domain.message.MQDeleteDepartMessage;
import net.duckling.vmt.api.domain.message.MQDeleteGroupMessage;
import net.duckling.vmt.api.domain.message.MQDeleteOrgMessage;
import net.duckling.vmt.api.domain.message.MQLinkUserMessage;
import net.duckling.vmt.api.domain.message.MQMoveDepartMessage;
import net.duckling.vmt.api.domain.message.MQMoveUserMessage;
import net.duckling.vmt.api.domain.message.MQRefreshGroupMessage;
import net.duckling.vmt.api.domain.message.MQRefreshOrgMessage;
import net.duckling.vmt.api.domain.message.MQSwitchGroupDchatStatusMessage;
import net.duckling.vmt.api.domain.message.MQSwitchOrgDchatStatusMessage;
import net.duckling.vmt.api.domain.message.MQUnlinkUserMessage;
import net.duckling.vmt.api.domain.message.MQUpdateDepartMessage;
import net.duckling.vmt.api.domain.message.MQUpdateGroupMessage;
import net.duckling.vmt.api.domain.message.MQUpdateOrgMessage;
import net.duckling.vmt.api.domain.message.MQUpdateUserMessage;
import net.duckling.vmt.common.adapter.VmtDepart2LdapDepartAdapter;
import net.duckling.vmt.common.adapter.VmtGroup2LdapGroupAdapter;
import net.duckling.vmt.common.adapter.VmtOrg2LdapOrgAdapter;
import net.duckling.vmt.common.adapter.VmtUser2LdapUserAdapter;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.domain.MQMessage;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IMQMessageService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-8-1
 */
@Service
public class MQMessageSender {
	private static final Logger LOGGER=Logger.getLogger(MQMessageSender.class);
	@Autowired
	private VmtConfig vmtConfig;
	@Autowired
	private IMQMessageService mqService;
	
	private IDFPublisher sender;
	
	@PostConstruct
	public void init(){
		sender=DFMQFactory.buildPublisher(vmtConfig.getMqUserName(), vmtConfig.getMqPasswd(),vmtConfig.getMqIp(), vmtConfig.getMqExchangeName(), DFMQMode.TOPIC);
	}
	public void send(MQBaseMessage msg){
		String jsonStr=msg.toJsonString();
		MQMessage mqMessage=new MQMessage();
		LOGGER.info("send Message["+msg.getType()+"]:"+jsonStr);
		mqMessage.setContent(jsonStr);
		mqMessage.setPublishTime(msg.getPublishTime());
		mqMessage.setType(msg.getType());
		mqMessage.setId(mqService.insert(mqMessage));
		msg.setMsgId(mqMessage.getId());
		sender.send(msg, msg.getType());
		
	}
	public void sendCreateOrgMessage(LdapOrg ldapOrg,LdapUser creator){
		if(ldapOrg.isHideMobile()){
			creator.setTelephone(null);
		}
		MQBaseMessage message=new MQCreateOrgMessage(VmtOrg2LdapOrgAdapter.convert(ldapOrg),VmtUser2LdapUserAdapter.convert(creator));
		send(message);
	}
	public void sendCreateGroupMessage(LdapGroup group,LdapUser creator){
		
		if(group.isHideMobile()){
			creator.setTelephone(null);
		}
		MQBaseMessage message=new MQCreateGroupMessage(
				VmtGroup2LdapGroupAdapter.convert(group),
				VmtUser2LdapUserAdapter.convert(creator)
				);
		send(message);
	}
	
	public void sendCreateDeptMessage(LdapDepartment dept,String parentDn){
		MQBaseMessage message=new MQCreateDepartMessage(VmtDepart2LdapDepartAdapter.convert(dept),parentDn);
		send(message);
	}
	protected void setTelephoneNull(List<LdapUser> users){
		if(CommonUtils.isNull(users)){
			for(LdapUser u:users){
				u.setTelephone(null);
			}
		}
	}
	public void sendCreateUserMessage(List<LdapUser> users,LdapOrg org ,LdapDepartment dept,LdapGroup group){
		List<VmtUser> vUsers=VmtUser2LdapUserAdapter.convert(users);
		if(CommonUtils.isNull(vUsers)){
			return;
		}
		if(org!=null&&org.isHideMobile()){
			setTelephoneNull(users);
		}
		if(group!=null&&group.isHideMobile()){
			setTelephoneNull(users);
		}
		
		MQBaseMessage message=new MQLinkUserMessage(
				vUsers,
				VmtOrg2LdapOrgAdapter.convert(org),
				VmtDepart2LdapDepartAdapter.convert(dept),
				VmtGroup2LdapGroupAdapter.convert(group));
		send(message);
	}
	public MQMoveDepartMessage getMoveDeptMessage(LdapDepartment dept,boolean containSelf,LdapDepartment targetDept,LdapOrg org){
		return new MQMoveDepartMessage(VmtDepart2LdapDepartAdapter.convert(dept),containSelf,VmtDepart2LdapDepartAdapter.convert(targetDept),VmtOrg2LdapOrgAdapter.convert(org));

	}
	public void sendMoveUserMessage(List<LdapUser> users,LdapOrg org,LdapDepartment dept){
		if(org!=null&&org.isHideMobile()){
			setTelephoneNull(users);
		}
		VmtOrg vOrg=VmtOrg2LdapOrgAdapter.convert(org);
		List<VmtUser> vUsers=VmtUser2LdapUserAdapter.convert(users);
		if(CommonUtils.isNull(vUsers)){
			return;
		}
		VmtDepart vDepart=VmtDepart2LdapDepartAdapter.convert(dept);
		if(vUsers.size()>100){
			int howMany100=vUsers.size()/100;
			int last=vUsers.size()%100;
			for(int i=0;i<howMany100;i++){
				int start=i*100;
				int end=start+100;
				LOGGER.info("send move batch: from "+start+" to "+end);
				sendPeopleMove(vOrg, vUsers.subList(start, end), vDepart);
			}
			if(last!=0){
				int start=howMany100*100;
				int end=vUsers.size()-1;
				LOGGER.info("send move batch: from "+start+" to "+end);
				sendPeopleMove(vOrg,vUsers.subList(start, end),vDepart);
			}
			
		}else{
			sendPeopleMove(vOrg, vUsers, vDepart);
		}
		
	}
	private void sendPeopleMove(VmtOrg o,List<VmtUser> vUsers,VmtDepart vDepart){
		MQBaseMessage message=new MQMoveUserMessage(vUsers,
				o, vDepart);
		send(message);
	}
	/**
	 * @param group
	 * @param admins
	 */
	public void sendDeleteGroupMessage(LdapGroup group) {
		MQBaseMessage message=new MQDeleteGroupMessage(
				VmtGroup2LdapGroupAdapter.convert(group));
		send(message);
		
	}
	/**
	 * @param org
	 */
	public void sendDeleteOrgMessage(LdapOrg org) {
		MQBaseMessage message=new MQDeleteOrgMessage(
				VmtOrg2LdapOrgAdapter.convert(org));
		send(message);
	}
	/**
	 * @param user
	 */
	public void sendDeleteUserMessage(LdapUser user,LdapOrg org,LdapDepartment dept ,LdapGroup group) {
		List<LdapUser> users=new ArrayList<LdapUser>();
		users.add(user);
		sendDeleteUserMessage(users, org, dept, group);
		
	}
	public void sendDeleteUserBatch(List<VmtUser> users,VmtOrg org,VmtDepart dept ,VmtGroup group){
		MQBaseMessage message=new MQUnlinkUserMessage(users,org,dept,group);
		send(message);
	}
	public void sendDeleteUserMessage(List<LdapUser> users,LdapOrg org,LdapDepartment dept ,LdapGroup group) {
		if(org!=null&&org.isHideMobile()){
			setTelephoneNull(users);
		}
		if(group!=null&&group.isHideMobile()){
			setTelephoneNull(users);
		}
		VmtOrg vOrg=VmtOrg2LdapOrgAdapter.convert(org);
		List<VmtUser> vUsers=VmtUser2LdapUserAdapter.convert(users);
		if(CommonUtils.isNull(vUsers)){
			return;
		}
		VmtDepart vDepart=VmtDepart2LdapDepartAdapter.convert(dept);
		VmtGroup vGroup=VmtGroup2LdapGroupAdapter.convert(group);
		if(vUsers.size()>100){
			int howMany100=vUsers.size()/100;
			int last=vUsers.size()%100;
			for(int i=0;i<howMany100;i++){
				int start=i*100;
				int end=start+100;
				LOGGER.info("send move batch: from "+start+" to "+end);
				sendDeleteUserBatch( vUsers.subList(start, end),vOrg, vDepart,vGroup);
			}
			if(last!=0){
				int start=howMany100*100;
				int end=vUsers.size()-1;
				LOGGER.info("send move batch: from "+start+" to "+end);
				sendDeleteUserBatch( vUsers.subList(start, end),vOrg, vDepart,vGroup);
			}
		}else{
			sendDeleteUserBatch( vUsers,vOrg, vDepart,vGroup);
		}
		
	}
	/**
	 * @param org
	 * @param dept
	 */
	public void sendDeleteDepartMessage(LdapOrg org, LdapDepartment dept) {
		MQBaseMessage message=new MQDeleteDepartMessage(VmtOrg2LdapOrgAdapter.convert(org),VmtDepart2LdapDepartAdapter.convert(dept));
		send(message);
	}
	/**
	 * @param group
	 * @param users
	 */
	public void sendUpdateGroup(LdapGroup group, List<LdapUser> users,String beforeName) {
		if(group!=null&&group.isHideMobile()){
			setTelephoneNull(users);
		}
		MQUpdateGroupMessage message=new MQUpdateGroupMessage(VmtGroup2LdapGroupAdapter.convert(group),
				VmtUser2LdapUserAdapter.convert(users));
		message.setBeforeName(beforeName);
		send(message);
	}
	public void sendUpdateOrg(LdapOrg org,List<LdapUser> users,String beforeName){
		if(org!=null&&org.isHideMobile()){
			setTelephoneNull(users);
		}
		MQUpdateOrgMessage message=new MQUpdateOrgMessage(VmtOrg2LdapOrgAdapter.convert(org),VmtUser2LdapUserAdapter.convert(users));
		message.setBeforeName(beforeName);
		send(message);
	}
	/**
	 * @param dept
	 */
	public void sendUpdateDept(LdapDepartment dept,String beforeName) {
		MQUpdateDepartMessage message=new MQUpdateDepartMessage(VmtDepart2LdapDepartAdapter.convert(dept));
		message.setCurrentDept(beforeName);
		send(message);
	}
	/**
	 * @param dn
	 */
	public void sendUpdateUser(LdapUser user) {
		VmtUser users=VmtUser2LdapUserAdapter.convert(user);
		if(users==null){
			return;
		}
		MQBaseMessage message=new MQUpdateUserMessage(users);
		send(message);
	}
	/**
	 * @param group
	 */
	public void sendRefreshGroupMessage(LdapGroup group) {
		MQBaseMessage msg=new MQRefreshGroupMessage(VmtGroup2LdapGroupAdapter.convert(group));
		send(msg);
	}
	public void sendRefreshGroupMessage(List<LdapGroup> groups) {
		List<VmtGroup> vmtGroups=VmtGroup2LdapGroupAdapter.convert(groups);
		for(VmtGroup g:vmtGroups){
			MQBaseMessage msg=new MQRefreshGroupMessage(g);
			send(msg);
		}
		
	}
	/**
	 * @param org
	 */
	public void sendRefreshOrgMessage(LdapOrg org) {
		MQBaseMessage msg=new MQRefreshOrgMessage(VmtOrg2LdapOrgAdapter.convert(org));
		send(msg);
	}
	public void sendRefreshOrgMessage(List<LdapOrg> orgs) {
		List<VmtOrg> vmtOrgss=VmtOrg2LdapOrgAdapter.convert(orgs);
		for(VmtOrg g:vmtOrgss){
			MQBaseMessage msg=new MQRefreshOrgMessage(g);
			send(msg);
		}
	}
	public void sendOpenOrgDchatMessage(LdapOrg org, boolean isOpen) {
		MQBaseMessage msg=new MQSwitchOrgDchatStatusMessage(VmtOrg2LdapOrgAdapter.convert(org), isOpen);
		send(msg);
		
	}
	public void sendOpenGroupDchatMessage(LdapGroup groupByDN, boolean isOpen) {
		MQBaseMessage msg=new MQSwitchGroupDchatStatusMessage(VmtGroup2LdapGroupAdapter.convert(groupByDN), isOpen);
		send(msg);
		
	}
}
