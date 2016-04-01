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
package net.duckling.vmt.common.adapter;

import net.duckling.vmt.api.domain.VmtDepart;
import net.duckling.vmt.domain.ldap.LdapDepartment;

/**
 * VmtDepart和LdapDepart相互转换器
 * VmtDepart 用于提供给外部的VmtDepart对象
 * LdapDepart 用户本地持久化的部门数据对象
 * @author lvly
 * @since 2013-5-21
 */
public final class VmtDepart2LdapDepartAdapter {
	private VmtDepart2LdapDepartAdapter(){}
	/**
	 *@param depart
	 *@return
	 */
	public static LdapDepartment  convert(VmtDepart depart){
		if(depart== null){
			return null;
		}
		LdapDepartment  ldapDepart=new LdapDepartment();
		ldapDepart.setCreator(depart.getCreator());
		ldapDepart.setCurrentDisplay(depart.getCurrentDisplay());
		ldapDepart.setName(depart.getName());
		ldapDepart.setSymbol(depart.getSymbol());
		ldapDepart.setDn(depart.getDn());
		ldapDepart.setListRank(depart.getListRank());
		return ldapDepart;
	}
	public static VmtDepart convert(LdapDepartment department){
		if(department==null){
			return null;
		}
		VmtDepart dept=new VmtDepart();
		dept.setCreator(department.getCreator());
		dept.setCurrentDisplay(department.getCurrentDisplay());
		dept.setDn(department.getDn());
		dept.setName(department.getName());
		dept.setSymbol(department.getSymbol());
		dept.setVisible(department.isVisible());
		dept.setListRank(department.getListRank());
		return dept;
	}

}
