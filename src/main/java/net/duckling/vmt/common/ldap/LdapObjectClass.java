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
package net.duckling.vmt.common.ldap;

/**
 * 静态类，只用成员变量的值
 * @author lvly
 * @since 2013-5-2
 */
public final class LdapObjectClass {
	private LdapObjectClass(){}
	/**
	 * 搜索条件里的key，每次写好烦啊，还容易写错
	 * */
	public static final String OBJECTCLASS="objectclass";
	/**
	 * 用户对应的Ldap里的Schema
	 * */
	public static final String CLASS_VMT_USER="vmt-user";
	/**
	 * 组织机构对应的Ldap里的Schema
	 * */
	public static final String CLASS_VMT_ORG="vmt-org";
	/**
	 * 部门对应的Ldap里的Schema
	 * */
	public static final String CLASS_VMT_DEPART="vmt-depart";
	/**
	 * 群组对应的Ldap里的Schema
	 * */
	public static final String CLASS_VMT_GROUP="vmt-group";
	/**
	 * 设置对应的Ldap里的Schema，可设置的对象为Group和Org
	 * */
	public static final String CLASS_VMT_SETTING="vmt-setting";
	/**
	 * 节点对应的Ldap里的Schema，节点包含Group,Depart,Org,User
	 * */
	public static final String CLASS_VMT_NODE="vmt-node";

}
