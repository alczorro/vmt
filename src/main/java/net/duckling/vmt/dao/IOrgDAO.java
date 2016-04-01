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
package net.duckling.vmt.dao;

import java.util.List;

import net.duckling.vmt.domain.SearchOrgDomainMappingCondition;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapOrg;


/**
 * 针对组织的数据持久层，暂时只有Ldap实现
 * @author lvly
 * @since 2013-4-27
 */
/**
 * @author lvly
 * @since 2013-6-21
 */
public interface IOrgDAO {
	/**
	 * 创建机构，不会创建相同名称的机构
	 * @param org 机构信息 
	 * @return 是否成功创建
	 */
	 boolean insert(LdapOrg org);
	
	/**
	 * 机构是否存在
	 * @param orgSymbol 机构标识
	 * @return 是否存在
	 * */
	 boolean isExists(String orgSymbol);

	/**
	 * 获得所有我所在的团队
	 * @param umtId 用户id
	 * @return 返回所有团队的dn
	 */
	 List<LdapOrg> getMyOrgs(String umtId);
	 
	 
	 /**
	  * 因为是超级管理员，所以能看到所有团队
	  * */
	 List<LdapOrg> getAllOrgs();
	
	/**
	 * 根据DN获取团队
	 * @param dn 团队的dn
	 * @return 团队
	 * */
	 LdapOrg getOrgByDN(String dn);
	
	/**
	 * 获得我是管理员的团队
	 * @param umtId 我的id
	 * @return list
	 * */
	 List<LdapOrg> getAdminOrgs(String umtId);
	 
	 /**
	  *是否有symbol为xx的coreMail同步机构存在，存在就不同步了 
	  *@param orgId 机构id
	  *@param from LdapNode.XXX
	  *@return dn
	  *<p>如果存在则返回dn，如果不存在，则返回null
	  * 
	  * */
	 String getThirdPartyOrg(String orgId, String from);

	/**
	 * 根据组织标识，获得组织
	 * @param orgSymbol
	 * @return
	 */
	LdapOrg getOrgBySymbol(String orgSymbol);

	/**
	 * 获得第三方的org
	 * @param from
	 * @return
	 */
	List<LdapOrg> getThirdPartyOrgs(String from);

	/**
	 * @param orgIndex
	 * @return
	 */
	List<LdapOrg> getOrgBySymbol(List<VmtIndex> orgIndex);

	/**
	 * @return
	 */
	List<LdapOrg> getOrgsByMemberVisible();

	List<LdapOrg> getOrgsByOrgDomainMapping(SearchOrgDomainMappingCondition od);

	LdapOrg getOrgByDomain(String domain);

	int getOrgCount();

	int getOrgCountWithDChat();

	

}
