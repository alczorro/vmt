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

import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;

/**
 * @author lvly
 * @since 2013-7-16
 */
public interface IVmtIndexService {
	/**
	 * 重新建立索引,同步过程，可能需要等较长时间，单是不知结果
	 */
	void buildIndexSynchronous();
	/**
	 * 增加单条用户数据
	 * @param teamDN
	 * @param umtId
	 */
	void addIndexByUmtId(String teamDN,boolean[] flag,String[] umtId);
	/**
	 * 增加单条用户数据
	 * @param teamDN
	 * @param cstnetId
	 */
	void addIndexByCstnetId(String teamDN,boolean[] flag,String[] cstnetId);
	
	/**
	 * @param teamDN
	 * @param flag
	 * @param user
	 */
	void addIndexByUser(String teamDN,boolean[] flag,LdapUser user);
	/**
	 * 重新建立索引，异步过程，体验较好，但是不知结果
	 * */
	void buildIndexJob();
	/**
	 * 重建索引，异步过程，但是根据dn同步，值更新一个组织的索引
	 * @param decode
	 */
	void buildAIndexJob(String dn);
	/**
	 * 重建索引，过程，但是根据dn同步，值更新一个组织的索引
	 * @param decode
	 */
	void buildAIndexSynchronous(String dn);
	/**
	 * 根据用户查询到索引
	 * */
	List<VmtIndex> selectIndexByType(String umtId,int type);
	/**
	 * 根据用户查询到索引
	 * */
	List<VmtIndex> selectIndexByStatus(String umtId,String status);
	
	/**
	 * 根据LdapOrg获得count数量
	 * */
	void setOrgCount(List<LdapOrg> orgs,String umtId);
	
	/**
	 * 根据LdapGroups获得count数量
	 * */
	void setGroupCount(List<LdapGroup> groups,String umtId);
	/**
	 * 更新用户的状态
	 * @param dn
	 * @param umtId
	 * @param statusActive
	 */
	void updateStatus(String dn, String umtId, String statusActive);
	/**
	 * 更新用户姓名
	 * @param userDn
	 * @param userName
	 */
	void updateUserName(String userDn,String userName);
	/**
	 * 更新组织机构的名字
	 * @param teamDn
	 * @param teamName
	 */
	void updateTeamName(String teamDn,String teamName);
	
	/**
	 * 删除某个组织下的某个用户
	 * */
	void deleteUser(String[] userDNs);
	/**
	 * 删除
	 * @param teamDN
	 * @param umtId
	 */
	void deleteIndex(String teamDN, String umtId);
	
	/**
	 * 删除某个组织下的索引
	 * */
	void deleteIndex(String teamDn);
	
	/**
	 * 判定是否umtId
	 * @param orgDN
	 * @param umtId
	 * @return
	 */
	//boolean isUserExistsOtherOrg(String umtId);
	/**
	 * 
	 * @param dn
	 * @param listRank
	 */
	void updateUserVisible(String dn, boolean visible);
	/**
	 * @param dn
	 * @param b
	 */
	void updateUserStatus(String dn, String status);
	/**
	 * @param extract
	 * @param statusApply
	 * @return
	 */
	List<VmtIndex> selectIndexByTeamDNAndStatus(String[] teamDns, String statusApply);
	//不管用户状态,存在就拿出来
	List<VmtIndex> selectIndexByTypeAll(String umtId, int typeGroup);
	
	/**
	 * 是否vmt存在的用户
	 * */
	boolean isUseableVmtMember(String email);
	
	/**获取vmt存在的某个用户*/
	VmtIndex getUseableVmtUser(String email);
	
	List<VmtIndex> selectIndexByKeyword(String trim);
	
	VmtIndex selectIndexById(int indexId);
	
	
}
