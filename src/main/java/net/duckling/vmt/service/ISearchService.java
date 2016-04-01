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

import net.duckling.vmt.domain.view.ListView;

/**
 * 一些奇怪的搜索逻辑
 * @author lvly
 * @since 2013-5-10
 */
public interface ISearchService {
	/**
	 * 按照层级返回当前目录的实体
	 * @param dn
	 * @return 返回用户或者Department
	 */
	List<ListView> searchByList(String dn,String umtId);
	
	/**
	 * 此方法会，当前实体的所有实体，返回LdapUser或者LdapDepartment实体类
	 * @param dn 
	 * @param umtId umtId
	 * @return list;
	 * */
	List<?> searchByListLocalData(String dn);
	/**
	 * 按照层级搜索部门
	 * @param decodeDN
	 * @return 返回DepartMent
	 */
	List<ListView> searchDepartByList(String decodeDN);
	
	/**
	 * 搜索出团队的，已激活的用户数量
	 * @param dn 团队dn
	 * @return
	 */
	int searchActiveUserCount(String dn);
	
	/**
	 * 搜索底下所有包含的实体，包括部门和用户，树状搜索
	 * @param decodeDN
	 * @return
	 */
	List<ListView> searchAllByTree(String decodeDN);
	

}
