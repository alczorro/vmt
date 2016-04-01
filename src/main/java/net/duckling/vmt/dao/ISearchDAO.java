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

import net.duckling.vmt.domain.view.ListView;

/**
 * 一些奇怪的搜索逻辑
 * @author lvly
 * @since 2013-5-10
 */
public interface ISearchDAO {
	/**
	 * 按照层级返回当前目录的尸体
	 * @param dn
	 * @param isAdmin 
	 * @return 返回用户或者Department
	 */
	List<ListView> searchByList(String dn,boolean searchUnConfirm, boolean isAdmin);

	/**
	 * 获得部门列表，注意只获得部门
	 * @param decodeDN
	 * @return
	 */
	List<ListView> searchDepartByList(String decodeDN);

	/**
	 * 获得已激活用户的数量
	 * @param dn
	 * @return
	 */
	int searchActiveUserCount(String dn);
	/**
	 * 和searchByList类似，但是返回LdapUser或者LdapDepart
	 * @param dn
	 * @return
	 */
	List<?> searchByListLocalData(String dn);

	/**
	 * 根据树状搜索，把下面的部门和用户都搜出来
	 * @param dn
	 * @return
	 */
	List<ListView> searchDepartByAll(String dn);
	

}
