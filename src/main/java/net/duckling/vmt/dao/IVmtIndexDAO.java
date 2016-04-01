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
import java.util.Map;

import net.duckling.vmt.domain.index.VmtIndex;

/**
 * @author lvly
 * @since 2013-7-16
 */
public interface IVmtIndexDAO {
	/**
	 * 同步所有用户的信息到数据库
	 */
	void insertIndex(List<VmtIndex> indexs);
	
	/**
	 * 清空所有索引
	 */
	void deleteAll();
	
	/**
	 * 删除一个用户的索引
	 * */
	void deleteAUserIndex(String umtId);
	
	/**
	 * 删除一个团队的所有
	 * */
	void deleteATeamIndex(String teamDn);
	
	/**
	 * 删除某个用户在某个团队的索引
	 * */
	void deleteIndex(String dn,String umtId);
	
	/**
	 * 根据用户查询到索引
	 * @param type 
	 * */
	List<VmtIndex> selectIndexByType(String umtId, int type);
	/**
	 * update user status
	 * */
	void updateStatus(String umtId,String dn,String status);

	/**
	 * @param umtId
	 * @param status
	 * @return
	 */
	List<VmtIndex> selectIndexByStatus(String umtId, String status);

	/**
	 * @param convertField
	 * @param typeGroup
	 * @return
	 */
	Map<String, int[]> selectCountByType(String[] convertField, int typeGroup);

	/**
	 * @param userDn
	 * @param userName
	 */
	void updateUserName(String userDn, String userName);

	/**
	 * @param teamName
	 * @param teamName2
	 */
	void updateTeamName(String teamName, String teamName2);

	/**
	 * @param userDNs
	 */
	void deleteAUserIndexByUserDN(String[] userDNs);

	void deleteIndex(String teamDn);


	/**
	 * @param umtId
	 * @return
	 */
	boolean isUserExistsOtherOrg(String umtId);

	/**
	 * @param dn
	 * @param visible
	 */
	void updateUserVisible(String dn, boolean visible);

	/**
	 * @param dn
	 * @param status
	 */
	void updateUserStatus(String dn, String status);

	/**
	 * @param teamDns
	 * @param statusApply
	 * @return
	 */
	List<VmtIndex> selectIndexByTeamDNAndStatus(String[] teamDns, String statusApply);

	List<VmtIndex> selectIndexByTypeAll(String umtId, int type);

	boolean isUseableVmtMember(String email);

	VmtIndex getUseableVmtUser(String email);

	List<VmtIndex> selectIndexByKeyword(String trim);

	VmtIndex selectIndexById(int indexId);
	
}
