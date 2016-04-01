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

import net.duckling.vmt.domain.coremail.CoreMailOrgUnit;
import net.duckling.vmt.domain.coremail.CoreMailUser;

/**
 * 针对coreMail查询的接口，暂时没有写操作，只有读操作
 * @author lvly
 * @since 2013-5-29
 */
public interface ICoreMailService {
	/**
	 * 获得部门信息
	 * @param orgId 组织id
	 * @param ouId 部门id
	 * @return 组织部门信息
	 */
	CoreMailOrgUnit getUnit(String orgId,String ouId);
	
	/**
	 * 创建coreMail部门
	 * @param unit
	 */
	void createUnit(CoreMailOrgUnit unit);
	/**
	 * 查询一个组织下的所有可见用户
	 * @param orgId 组织标识
	 * @return 包含该组织下的所有用户
	 **/
	List<CoreMailUser> getUsers(String orgId);
	
	/**
	 * 根据用户的邮件域名，获得该域名下的组织标识
	 * @param 邮件域名，比如邮件时a@b.com 则域名是b.com
	 * @return 组织标识列表，有可能一个域名对应两个组织
	 * */
	String[] getOrgIdFromDomain(String domain);
	
	/**
	 * 获得组织机构名称
	 * @param orgId 组织机构id
	 * @return 组织机构名称
	 * */
	String getOrgName(String orgId);
	
	/**
	 * 获得coreMail内部所有的orgId
	 * */
	String[] getAllOrgId();
	
	/**
	 * 更新用户在组织通讯录中是否可见
	 * @param cstnetId haha@cstnet.cn之类的邮箱地址
	 * @param privacyLevel  2 表示组织内公开, 4 表示全站公开,0表示不公开
	 */
	void updateUserPrivacyLevel(String cstnetId,String privacyLevel);
	
	/**
	 * 移动人
	 * @param cstnetId 用户邮箱
	 * @param pouId 如果为空，则认为移动到根
	 */
	void moveUser(String cstnetId,String pouId);
	
	/**
	 * 移动部门
	 * @param unitId 部门id
	 * @param punitId 如果父部门id为空，则认为移动到根
	 */
	void moveUnit(String orgId,String unitId,String punitId);
	
	/**
	 * 删除部门
	 * @param orgId 组织id
	 * @param ouId 部门id
	 */
	void deleteUnit(String orgId,String ouId);
	
	/**
	 * 查询用户是否是coreMail用户
	 * @param cstnetId 用户是否存在
	 * @return
	 */
	boolean isUserExists(String cstnetId);
	
	/**
	 * 部门更名
	 * @param orgId 组织id
	 * @param ouId 部门id
	 * @param newName 部门新名称
	 * */
	void updateUnitName(String orgId,String ouId,String newName);
	
	/**
	 * 更新用户账户状态
	 * @param email 邮箱
	 * @param status 状态
	 */
	void updateAccount(String email,String status,String expireTime);

	/**
	 * 删除用户，完全删除
	 * @param cstnetId 邮箱
	 */
	void deleteUser(String cstnetId);
	
	/**
	 * 获得组织机构的域名
	 * @param orgId
	 * @return
	 */
	String[] getDomainListFromOrgId(String orgId);
	
	/**注册邮箱用户
	 * @param user
	 */
	void createUser(CoreMailUser user);

	/**
	 * 更改密码
	 * @param cstnetId
	 * @param password
	 */
	boolean changePassword(String cstnetId, String password);
}
