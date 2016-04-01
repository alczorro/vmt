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

/**
 * 同步DDL的团队到vmt的群组，批量或者指定
 * @author lvly
 * @since 2013-5-29
 */
public interface ISynchronousDDLService {
	/**
	 * 同步过来所有的团队
	 * @param umtId 创建者
	 * @param rootOnly  是否只更新团队，不包括人员变动
	 */
	void synchrounousAll(String umtId, boolean rootOnly);
	/**
	 * 同步一个团队里的数据
	 * @param teamCode 团队标志，symbole=teamCode
	 * */
	void synchrounousAGroup(String teamCode,String umtId,boolean rootOnly);
	
	/**
	 * 同步当前登录用户的团队，如果已存在
	 * @param uid 用户邮箱，ddl就叫uid我哪里知道为啥
	 */
	void synchrounousIfExists(String uid,String umtId);
}
