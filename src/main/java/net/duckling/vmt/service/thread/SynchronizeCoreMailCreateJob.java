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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.coremail.CoreMailOrgUnit;
import net.duckling.vmt.domain.coremail.CoreMailUser;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IDepartmentService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;

import org.apache.log4j.Logger;

import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.rest.ServiceException;

/**
 * @author lvly
 * @since 2013-5-29
 */
public class SynchronizeCoreMailCreateJob implements Jobable{
	private static final Logger LOG=Logger.getLogger(SynchronizeCoreMailCreateJob.class);
	/**
	 * 组织id
	 */
	private String orgId;
	/**
	 * 用户的umtId，触发的人
	 */
	private String umtId;
	/**
	 * 用户服务类
	 */
	private IUserService userService;
	/**
	 * 邮箱api服务类
	 */
	private ICoreMailService coreMailService;
	/**
	 * 组织结构的管理类
	 */
	private IOrgService orgService;
	/**
	 * 部门服务类
	 */
	private IDepartmentService deptService;
	/**
	 * umtapi服务类
	 */
	private UserService umtService;
	
	private IVmtIndexService indexService;
	private boolean createRootOnly;
	private MQMessageSenderExt sender;
	
	/**
	 * 构造
	 * @param orgId
	 * @param umtId
	 */
	public SynchronizeCoreMailCreateJob(String orgId,String umtId,boolean createRootOnly){
		this.orgId=orgId;
		this.umtId=umtId;
		this.userService=BeanFactory.getBean(IUserService.class);
		this.coreMailService=BeanFactory.getBean(ICoreMailService.class);
		this.orgService=BeanFactory.getBean(IOrgService.class);
		this.deptService=BeanFactory.getBean(IDepartmentService.class);
		this.umtService=BeanFactory.getBean(UserService.class);
		this.indexService=BeanFactory.getBean(IVmtIndexService.class);
		this.createRootOnly=createRootOnly;
		this.sender=BeanFactory.getBean(MQMessageSenderExt.class);
	}
	private void createUser(Map<String,LdapDepartment> map,String rootDN,List<CoreMailUser> users){
		Map<String,BatchCreateUser> batch=new HashMap<String,BatchCreateUser>();
		int index=0;
		for(CoreMailUser user:users){
			String pdn=rootDN;
			if(user.getOuId()!=null){
				pdn=map.get(user.getOuId()).getDn();
			}
			BatchCreateUser uList=batch.get(pdn);
			if(uList==null){
				uList=new BatchCreateUser();
				batch.put(pdn, uList);
			}
			LOG.info("["+(++index)+"]user email :"+user.getEmail()+",");
			uList.addUser(user);
			if(user.isAdmin()){
				try {
					userService.addAdmin(rootDN, umtService.generateUmtId(new String[]{user.getEmail()})[0]);
				} catch (ServiceException e) {
					LOG.error(e.getMessage(),e);
				}
			}
		}
		for(Entry<String,BatchCreateUser> entry:batch.entrySet()){
			userService.addUserToNodeCoreMail(entry.getKey(),entry.getValue().getUsers());
			LOG.info("----------------------------add success to:"+entry.getKey()+",size:"+entry.getValue().getUsers().size());
		}
		LOG.info("-------------------------------end---------------------------------");
	}
	
	/**
	 * @param coreMailUser
	 * @return
	 */
	private Set<String> getDepartDic(List<CoreMailUser> users) {
		Set<String> depart=new HashSet<String>();
		for(CoreMailUser user:users){
			depart.add(user.getOuId());
		}
		return depart;
	}
	private void readDepart(Map<String,CoreMailOrgUnit> depDic,String ouId,String orgId){
		if(depDic.containsKey(ouId)){
			return;
		}else{
			CoreMailOrgUnit departInfo=coreMailService.getUnit(orgId, ouId);
			depDic.put(departInfo.getOuId(),departInfo);
			if(!CommonUtils.isNull(departInfo.getParentId())){
				readDepart(depDic,departInfo.getParentId(),orgId);
				return;
			}
		}
	}
	private void createDepart(Map<String,CoreMailOrgUnit> depDic,String path,Map<String,LdapDepartment> result,String rootDN,String umtId){
		List<CoreMailOrgUnit> sub=selectSub(getLast(path), depDic);
		for(CoreMailOrgUnit dep1:sub){
			LdapDepartment depart=new LdapDepartment();
			depart.setName(dep1.getOuName());
			depart.setSymbol(dep1.getOuId());
			depart.setCreator(umtId);
			depart.setListRank(dep1.getListRank());
			LdapDepartment parentDepart=result.get(dep1.getParentId());
			if(parentDepart==null){
				deptService.create(rootDN, depart);
			}else{
				deptService.create(parentDepart.getDn(),depart);
			}
			LOG.info("create depart:"+depart.getName()+"["+depart.getDn()+"]");
			result.put(dep1.getOuId(), depart);
			createDepart(depDic, path+","+dep1.getOuId(),result,rootDN,umtId);
		}
	}
	private List<CoreMailOrgUnit> selectSub(String ouId,Map<String,CoreMailOrgUnit> depDic){
		List<CoreMailOrgUnit> depart=new ArrayList<CoreMailOrgUnit>();
		for(Entry<String,CoreMailOrgUnit> entry:depDic.entrySet()){
			CoreMailOrgUnit dep=entry.getValue();
			if(dep!=null&&ouId.equals(dep.getParentId())){
				depart.add(dep);
			}
		}
		return depart;
	}
	private String getLast(String path){
		if(path.isEmpty()){
			return path;
		}
		return path.substring(path.lastIndexOf(',')+1);
	}
	
	@Override
	public void doJob() {
		String orgName=coreMailService.getOrgName(orgId);
		if(CommonUtils.isNull(orgName)){
			return;
		}
		LdapOrg org=null;
		if(orgService.isExists(orgId)){
			LOG.warn("this org["+orgId+"] is exits,do update... please update");
			org=orgService.getOrgBySymbol(orgId);
		}else{
			org=new LdapOrg();
			org.setSymbol(LdapUtils.removeDangerous(orgId));
			org.setMemberVisible(false);
			org.setCreator(umtId);
			org.setCurrentDisplay(LdapUtils.removeDangerous(orgName));
			org.setName(LdapUtils.removeDangerous(orgName));
			org.setFrom(LdapNode.FROM_CORE_MAIL);
			org.setCoreMail(false);
			orgService.createOrg(org,false);
		}
		if(createRootOnly){
			return;
		}
		
		List<CoreMailUser> users=coreMailService.getUsers(orgId);
		Set<String> depart=getDepartDic(users);
		Map<String,CoreMailOrgUnit> depDic=new HashMap<String,CoreMailOrgUnit>();
		for(String ouId:depart){
			readDepart(depDic,ouId,orgId);
		}
		Map<String,LdapDepartment> result=new HashMap<String,LdapDepartment>();
		createDepart(depDic,"", result,org.getDn(),umtId);
		createUser(result,org.getDn(),users);
		indexService.buildAIndexJob(org.getDn());
		sender.sendRefreshMessage(org.getDn());
	}
	@Override
	public String getJobId() {
		return COREMAIL_JOB+this.orgId;
	}
	@Override
	public boolean isJobEquals(Jobable job) {
		if(job!=null){
			return this.getJobId().equals(job.getJobId());
		}
		return false;
	}

}
