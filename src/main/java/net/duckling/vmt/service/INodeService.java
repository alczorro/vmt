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
package net.duckling.vmt.service;


import net.duckling.vmt.domain.ldap.LdapNode;

/**
 * 
 * @author lvly
 * @since 2013-6-21
 */
public interface INodeService {
	/**
	 * 通过dn获得节点
	 * @param dn
	 * @return
	 */
	LdapNode getNode(String dn);
	
	/**
	 * 更改显示路径，用于更名
	 * @param pdn 更名的节点dn
	 * @param newName 更改的名称
	 */
	void updateSonAndSelfDisplayName(String pdn,String newName);
	
	/**
	 * 移动节点后，需要更新该节点的显示连接
	 * @param dn
	 * @param moveto
	 */
	void updateMove(String dn,String moveto);
	
	/**
	 * 标识是否被使用
	 * @param pdn
	 * @param symbol
	 * @return
	 */
	boolean isSymbolUsed(String pdn,String symbol);
	
	/**
	 * 更新子节点的可见属性
	 * @param dn
	 * @param visible
	 * @param updateSelf
	 */
	void updateSubObjectVisible(String dn,boolean visible,boolean updateSelf);
	

}
