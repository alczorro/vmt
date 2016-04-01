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

import net.duckling.vmt.domain.ldap.LdapGroup;

/**
 * 针对团队的服务层
 * @author lvly
 * @since 2013-5-2
 */
public interface IGroupService {
	/**
	 * 创建机构，不会创建相同名称的机构
	 * @param org 机构信息 
	 * @param addSelf 是否添加自己
	 * @return 是否成功创建
	 * @throws ServiceException
	 * 			ErrorCode.PATTERN_ERROR		字段不符合规范<br>
	 */
	 boolean createGroup(LdapGroup group,boolean addSelf);
	/**
	 * 团队标识否存在
	 * @param groupSymbol 群组标识
	 * @return 是否存在
	 * */
	 boolean isSymbolUsed(String groupSymbol);
	 /**
	  * 团队名称是否存在
	  * */
	 boolean isNameUsed(String groupName);
	 /**
	 * 获得我在的所有组织
	 * @param umtId 用户的umtId
	 * @return
	 */
	 List<LdapGroup> getMyGroups(String umtId);
	/**
	 * 查询我是管理员的group
	 * @param umtId
	 * @return
	 */
	 List<LdapGroup> getAdminGroups(String umtId);
	/**
	 * 获得第三方群组
	 * @param from 来源，见LdapNode.FROM_XXX
	 * @param umtId 所属用户，即admin为当前用户的
	 * */
	List<LdapGroup> getThirdPartyGroupByUmtId(String from, String umtId);
	
	/**通过团队标志获得群组
	 * @param symbol 标志
	 * @param fromDdl 
	 * @return
	 */
	LdapGroup getGroupBySymbol(String symbol, String from);
	/** 获得第三方群组
	 * @param object
	 * @return
	 */
	List<LdapGroup> getThirdPartyGroups(String from);
	
	/**
	 * 根据dn获得group的信息
	 * @param decodeDN
	 * @return
	 */
	LdapGroup getGroupByDN(String decodeDN);
	
	/**
	 * 删除group
	 * */
	void deleteAllMember(String dn);
	/**
	 * 获得所有成员信息团队内可见
	 * @return
	 */
	List<LdapGroup> getGroupsByMemberVisible();
	/**
	 * 查找群组
	 * @param keyword
	 * @return
	 */
	List<LdapGroup> searchGroupByKeyword(String keyword);
	List<LdapGroup> getMyGroupsAll(String umtId);
	/**
	 * 查找群组的总数
	 * @return
	 */
	int getGroupCount();
}
