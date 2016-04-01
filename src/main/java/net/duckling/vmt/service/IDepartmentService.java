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

import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.view.ListView;
import net.duckling.vmt.exception.LdapOpeException;

/**
 * 针对部门的操作
 * @author lvly
 * @since 2013-5-2
 */
public interface IDepartmentService {
	String NEED_DELETE_DEPART="need-remove";
	
	/**
	 * 创建部门
	 * @param pdn parentDN，父部门的路径
	 * @param depart 部门实体，包含需要插入的信息
	 * @return 成功与否的flag
	 * @throws LdapOpeException 
	 */
	boolean create(String pdn,LdapDepartment depart);
	/**
	 * 查看是否存在此部门
	 * @param pdn 父部门路径
	 * @param departName 部门名称
	 * @return 是否存在
	 */
	boolean isNameExists(String pdn,String departName);
	
	 /**
	  * 移动部门的凶残能力,会产生较多的时间复杂度，不推荐用户使用
	  * @param destDn 目标dn
	  * @param orgDn 源dn
	  * @param includeSelf  包含自己
	  * @param isVisibleExtend 可见属性，是否按照目标团队
	  * */
	 boolean moveDepartment(String destDn,String orgDn, boolean sonOnly,boolean isVisibleExtend);
	 
	 /**
	  * 移动部门的时候是否会产生合并操作，会产生较多的时间复杂度，不推荐用户使用
	  * @param destDn 目标dn
	  * @param orgDn 源dn
	  * */
	 List<ListView> isMoveCauseMerge(String destDn,String orgDn,boolean sonOnly);
	 
	 /**
	  * 获得需要删除人员部门
	  * <br>coreMail同步时，如果已经同步过，就不管组织结构了，比对vmt比coreMail多出来的人
	  * <br>如果没有此部门，则创建
	  * @param orgDN 组织机构dn
	  * @return dn
	  * */
	 String getNeedDeleteDepart(String orgDN);
	 
	 
	 /**
	  * 通过部门标识获得部门、
	  * @param symbol
	  * @param orgDN
	  * @return 匹配的部门实体
	  * */
	 LdapDepartment getDepartBySymbol(String orgDN,String symbol);
	/**
	 * 根据部门的dn获取dn
	 * @param decodeDestDn
	 * @return
	 */
	LdapDepartment getDepartByDN(String decodeDestDn);
	
	/**
	 * 更新部门的额外信息，除name，如果更名请用commonService.rename
	 * @param dept
	 */
	LdapDepartment updateDepart(LdapDepartment dept);
	/**
	 * 根据给出的path，获得部门，path是部门名称，如果没有则创建,因为递归创建，所以里面会发mq消息
	 * @param teamDn
	 * @param paths
	 * @return
	 */
	LdapDepartment getDepartByPath(String teamDn, String[] paths);
	/**
	 * 获得所有的部门
	 * @return
	 */
	List<LdapDepartment> getDepartByAll(String baseDn);
	
}
