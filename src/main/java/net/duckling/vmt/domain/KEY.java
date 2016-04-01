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
package net.duckling.vmt.domain;

/**
 * 属性名
 * GLOBAL_XX 全局属性 <br/>
 * SESSION_XX session里面对象的key<br/>
 * DB_XX 数据库用的key<br/>
 * @author lvly
 * @since 2013-5-3
 */
/**
 * @author lvly
 * @since 2013-7-30
 */
public final class KEY {
	private KEY(){
		
	}
	/**
	 * 全局编码
	 * */
	public static final String GLOBAL_ENCODE="UTF-8";
	/**
	 * 全局系统
	 * */
	public static final String GLOBAL_SYSTEM="system";
	/**
	 * escience域名
	 * */
	public static final String GLOBAL_ESCIENCE_DOMAIN="escience.cn";
	
	/**
	 * Ldap里的member属性，给openfire用
	 * */
	public static final String DB_MEMBER_KEY="vmt-member";
	
	/**
	 * DB--用户umtId
	 * */
	public static final String DB_UMTID="vmt-umtid";
	
	/**
	 * 组织机构的baseDN
	 */
	public static final String DB_ORG_BASE_DN="ou=org";
	/**
	 * 群组的baseDN
	 */
	public static final String DB_GROUP_BASE_DN="ou=group";
	
	/**
	 * DB-用户激活状态
	 * */
	public static final String DB_STATUS="vmt-status";
	/**
	 * DB-用户邮箱
	 * */
	public static final String DB_CSTNET_ID="vmt-id";
	
	/**
	 * 
	 * DB 姓名，泛指
	 * */
	public static final String DB_NAME="vmt-name";
	
	/**
	 * DB 用于拼音
	 **/
	
	public static final String DB_PINYIN="vmt-pinyin";
	
	/**
	 * DB-空串，请用于dn
	 * */
	public static final String DB_EMPTY="";
	
	/**
	 * DB
	 * */
	
	/***
	 * DB vmt-root，用于全局搜索
	 * */
	public static final String DB_VMT_ROOT="vmt-root";
	
	public static final String DB_VMT_FROM="vmt-from";
	
	
	/**
	 * 已登录放置到session里的用户
	 * */
	public static final String SESSION_LOGIN_USER="loginUser";
	
	
	/**
	 * 一秒的长度
	 * */
	public static final int ONE_SEC=1000;
	public static final String DB_VMT_SYMBOL="vmt-symbol";
	public static final String DB_CURRENT_DISPLAY_NAME="vmt-current-display";
	public static final String DB_SEX="vmt-sex";
	public static final String DB_OFFICE="vmt-office";
	public static final String DB_OFFICE_PHONE="vmt-office-phone";
	public static final String DB_TELEPHONE="vmt-telephone";
	public static final String DB_TITLE="vmt-title";
	public static final String DB_LIST_RANK="vmt-list-rank";
	public static final String DB_VISIBLE="vmt-visible";
	
	public static final String GLOBAL_DATA_SPLIT="!~!";
	
	
}
