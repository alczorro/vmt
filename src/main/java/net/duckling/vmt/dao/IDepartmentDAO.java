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

import net.duckling.vmt.domain.ldap.LdapDepartment;

/**
 * 针对部门的dao接口
 * @author lvly
 * @since 2013-5-2
 */
public interface IDepartmentDAO {
	/**
	 * 插入部门
	 * @param 父节点dn
	 * @param depart
	 * @return
	 */
	boolean insert(String pdn, LdapDepartment depart);

	/**
	 * 查看一个部门名字是否可用，在一个层级下不允许有同名
	 * @param pdn 
	 * @param departName
	 * @return
	 */
	boolean isExists(String pdn,String departName);
	
	/**
	 * 如果，当前目录存在名为departName的部门，则返回dn值
	 * @param pdn
	 * @param departName
	 * @return
	 */
	String getDn(String pdn,String departName);
	
	/**
	 * 获得Department的对象
	 * @param dn
	 * @return 返回部门信息
	 * */
	LdapDepartment getDepartByDN(String dn);

	/**通过部门标识，获得一个部门
	 * @param symbol
	 * @return
	 */
	LdapDepartment getDepartBySymbol(String orgDN,String symbol);

	/**
	 * 根据部门的名称，获得部门，只限下一级
	 * @param teamDn
	 * @param string
	 */
	LdapDepartment getDepartByName(String pdn, String name);

	/**
	 * 获取所有的部门
	 * @return
	 */
	List<LdapDepartment> getDepartByAll(String baseDn);
}
