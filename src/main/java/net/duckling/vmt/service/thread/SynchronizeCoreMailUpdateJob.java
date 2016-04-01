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

import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.service.IOrgService;

/**
 * 更新coreMail源的组织机构，其实就是删掉，重构
 * @author lvly
 * @since 2013-6-21
 */
public class SynchronizeCoreMailUpdateJob  implements Jobable{
	/**
	 * 组织id
	 */
	private String orgId;
	/**
	 * 组织dn
	 */
	private String orgDN;
	/**
	 * 触发者的id
	 */
	private String umtId;
	/**
	 * 组织机构 Service
	 */
	private IOrgService orgService;
	
	
	/**
	 * 
	 * */
	private boolean rootOnly;
	
	
	/**
	 * 构造，以便加入到任务队列
	 * @param orgId 组织id
	 * @param orgDN 组织dn
	 * @param umtId 触发者id
	 */
	public SynchronizeCoreMailUpdateJob(String orgId,String orgDN,String umtId,boolean rootOnly){
		this.orgId=orgId;
		this.orgDN=orgDN;
		this.umtId=umtId;
		this.orgService=BeanFactory.getBean(IOrgService.class);
		this.rootOnly=rootOnly;
	}
	
	@Override
	public void doJob() {
		if(rootOnly){
			return;
		}
		//Map<String,LdapUser> ldapUsers=toLdapUserMap(userService.searchUsersByAll(new String[]{orgDN}, null));
		//Map<String,CoreMailUser> coreMailUsers=toCoreMailUserMap(coreMailService.getUsers(orgId));
		//List<String> needDelete=new ArrayList<String>();
//		for(Entry<String,LdapUser> entry:ldapUsers.entrySet()){
//			if(!coreMailUsers.containsKey(entry.getKey())){
//				needDelete.add(ldapUsers.get(entry.getKey()).getDn());
//			}
//		}
//		LOG.info("needDelete["+orgDN+"]"+needDelete.size()+","+needDelete);
		//String needDeleteArr[]=Collection2ArrayAdapter.convert(needDelete);
	//	String deletedDeptDN=LdapUtils.getDeletedDeptDN(orgDN);
//		if(commonService.isExist(deletedDeptDN)){
//			List<LdapUser> deletedUsers=userService.searchUsersByDN(deletedDeptDN, null);
//			if(!CommonUtils.isNull(deletedUsers)){
//				for(LdapUser deletedUser:deletedUsers){
//					if(!CommonUtils.isEqualsContain(needDeleteArr, deletedUser.getDn())){
//						userService.unbind(new String[]{deletedUser.getDn()});
//					}
//				}
//			}
//		}
//		if(!CommonUtils.isNull(needDeleteArr)){
//			String deleteDN=deptService.getNeedDeleteDepart(orgDN);
//			userService.move(needDeleteArr, deleteDN);
//		}
		orgService.deleteAllMember(orgDN);
		new SynchronizeCoreMailCreateJob(orgId, umtId,false).doJob();
	}
	
//	private Map<String,LdapUser> toLdapUserMap(List<LdapUser> users){
//		Map<String,LdapUser> map=new HashMap<String,LdapUser>();
//		if(CommonUtils.isNull(users)){
//			return map;
//		}
//		for(LdapUser user:users){
//			map.put(user.getCstnetId(), user);
//		}
//		return map;
//	}
//	private Map<String,CoreMailUser> toCoreMailUserMap(List<CoreMailUser> users){
//		Map<String,CoreMailUser> map=new HashMap<String,CoreMailUser>();
//		if(CommonUtils.isNull(users)){
//			return map;
//		}
//		for(CoreMailUser user:users){
//			map.put(user.getEmail(), user);
//		}
//		return map;
//	}
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
