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
package net.duckling.vmt.service;


/**
 * 同步coreMail信息接口
 * @author lvly
 * @since 2013-5-29
 */
public interface ISynchronousCoreMailService {
	
	/**
	 * 根据已登录用户的域名同步
	 * @param domain 登陆用户的域名
	 * @param umtId 登陆用户的umtId
	 */
	void synchrounousByDomain(String domain,String umtId);
	
	/**
	 * @param orgId
	 * @param umtId
	 */
	void synchrounousByOrgId(String orgId,String umtId);
	
	/**
	 * 同步所有团队过来
	 * @param rootOnly ,只更新组织，不更新人员和部门
	 * */
	void synchrounousAll(String umtId, boolean rootOnly);
	
	/**
	 * @param domain
	 * @param umtId
	 */
	void synchrounouseByDomainIfNotExists(String domain,String umtId);
}
