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

import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapGroup;


/**
 * 针对组织的数据持久层，暂时只有Ldap实现
 * @author lvly
 * @since 2013-4-27
 */
 public interface IGroupDAO {
	/**
	 * 创建机构，不会创建相同名称的机构
	 * @param org 机构信息 
	 * @return 是否成功创建
	 */
	 boolean insert(LdapGroup group);
	
	/**
	 * 机构是否存在
	 * @param orgName 机构名称
	 * @return 是否存在
	 * */
	 boolean isSymbolUsed(String groupName);

	/**
	 * 获得所有我所在的团队
	 * @param umtId 用户id
	 * @return 返回所有团队的dn
	 */
	 List<LdapGroup> getMyGroups(String umtId);
	
	/**
	 * 根据dn获得所在的团队
	 * */
	 LdapGroup getGroupByDN(String groupDN);

	/**
	 * @param umtId
	 * @return
	 */
	 List<LdapGroup> getAdminGroups(String umtId);

	/**
	 * @param from
	 * @param umtId
	 * @return
	 */
	List<LdapGroup> getThirdPartyGroupByUmtId(String umtId, String from);

	/**
	 * @return
	 */
	List<LdapGroup> getAllGroups();

	/**
	 * @param groupName
	 * @return
	 */
	boolean isNameUsed(String groupName);

	/**
	 * @param symbol
	 * @param object
	 * @return
	 */
	LdapGroup getGroupBySymbol(String symbol, String from);

	/**
	 * @param from
	 * @return
	 */
	List<LdapGroup> getThirdPartyGroups(String from);


	/**
	 * @param indexs
	 * @return
	 */
	List<LdapGroup> getGroupBySymbol(List<VmtIndex> indexs);

	/**
	 * 
	 * 
	 * @return
	 */
	List<LdapGroup> getGroupsByMemberVisible();

	/**
	 * @param keyword
	 * @return
	 */
	List<LdapGroup> searchGroupByKeyword(String keyword);

	int getGroupCount();
}
