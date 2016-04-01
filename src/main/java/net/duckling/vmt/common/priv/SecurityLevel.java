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
package net.duckling.vmt.common.priv;

/**
 * 用户访问授权登记，拦截URL配置用
 * @author lvly
 * @since 2013-5-23
 */
public enum SecurityLevel {
	/**
	 * 需要Admin权限
	 * */
	ADMIN,
	/**
	 * 需要该团队放开成员添加成员的权限
	 * */
	ADD_PRIV,
	/**
	 * 需要可视权限
	 * */
	VIEW,
	/**
	 * 需要自己操作自己，或者管理员
	 * */
	SELF_OR_ADMIN,
	
	/**
	 * 需要是超级管理员
	 * */
	SUPER_ADMIN
}
