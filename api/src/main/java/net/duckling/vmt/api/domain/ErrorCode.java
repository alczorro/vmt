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
package net.duckling.vmt.api.domain;

/**
 * api操作返回一场
 * @author lvly
 * @since 2013-5-21
 */
public final class ErrorCode {
	private ErrorCode(){
		
	}
	/**
	 * 要操作的dn，并非一个群组
	 * */
	public static final int NOT_A_GROUP=1110001;
	/**
	 * 要操作的DN不存在
	 * */
	public static final int DN_NOT_EXISTS=1110002;
	/***
	 * 用户不存在
	 * */
	public static final int USER_NOT_EXISTS = 1110003;
	/**
	 * 要操作的dn，并非是一个组织
	 * */
	public static final int NOT_A_ORG=1110004;
	/**
	 * 团队标志已被占用
	 * */
	public static final int SYMBOL_USED=1110005;
	/**
	 * 需要非空的字段是空
	 * */
	public static final int FILELD_REQUIRED=1110006;
	
	/**
	 * 名字已被占用
	 * */
	public static final int NAME_USED=1110007;
	/**
	 * 字段的值并非我想要的
	 * */
	public static final int FILELD_EXPECT=1110008;
	/**
	 * 格式错误
	 * */
	public static final int PATTERN_ERROR=1110009;
	
	/**
	 * 标识不存在
	 * */
	public static final int SYMBOL_NOT_FOUND=1110010;
}
