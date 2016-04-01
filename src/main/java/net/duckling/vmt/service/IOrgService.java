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

import java.util.List;

import net.duckling.vmt.domain.SearchOrgDomainMappingCondition;
import net.duckling.vmt.domain.ldap.LdapOrg;

/**
 * @author lvly
 * @since 2013-5-2
 */
public interface IOrgService {
	/**
	 * 创建机构，不会创建相同名称的机构
	 * @param org 机构信息 
	 * @param createSelf 创建自己吗？
	 * @return 是否成功创建
	 */
	 boolean createOrg(LdapOrg org,boolean createSelf);
	
	/**
	 * 机构是否存在
	 * @param orgSymbol 机构标识
	 * @return 是否存在
	 * */
	 boolean isExists(String orgSymbol);
	 
	 /**
	  * 通过symbol获得组织机构
	  * */
	 LdapOrg getOrgBySymbol(String orgSymbol);

	/**
	 * 获得我在的所有组织
	 * @param umtId 用户的umtId
	 * @return
	 */
	 List<LdapOrg> getMyOrgs(String umtId);
	/**
	 * 获得我是管理员的组织
	 * @param umtId  用户的umtId
	 * @return 
	 * */
	 List<LdapOrg> getAdminOrgs(String umtId);
	 
	 /**
	  *是否有symbol为xx的coreMail同步机构存在，存在就不同步了 
	  *@param orgId 机构id;
	  *@param from LdapNode xxx;
	  *@return dn
	  *<p>如果存在则返回dn，如果不存在，则返回null
	  * 
	  * */
	String getThirdPartyOrg(String orgId, String from);
	
	/**
	 * 删除除了已删除人员以外的所有人，包括人，
	 * @param orgDN
	 */
	void deleteAllMember(String orgDN);
	
	/**
	 * 获得第三方同步过来的org
	 * @param from
	 * @return
	 */
	List<LdapOrg> getThirdPartyOrgs(String from);

	/**
	 * 根据dn获得组织机构的详细信息
	 * @param decodeDN
	 * @return
	 */
	LdapOrg getOrgByDN(String decodeDN);
	/**
	 * 查询获得组织机构的总数
	 * @return 组织结构的总数
	 */
	int getOrgCount();
	/**
	 * 查询获得组织机构的总数
	 * @return
	 */
	int getOrgCountWithDChat();

	/**
	 * @return
	 */
	List<LdapOrg> getOrgsByMemberVisible();

	List<LdapOrg> getOrgsByOrgDomainMapping(SearchOrgDomainMappingCondition od);
	
	LdapOrg getOrgByDomain(String domain);

	List<LdapOrg> getMyOrgsAll(String umtId);

}
