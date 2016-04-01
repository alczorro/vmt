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
package net.duckling.vmt.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lvly
 * @since 2013-5-3
 */
public final class DBErrorCode {
	private DBErrorCode(){
	}
	/**
	 * 路径不存在
	 * */
	public static final int DN_NOT_EXISTS = 1;
	/**
	 * 实体已存在
	 * */
	public static final int NODE_EXISTS = 2;
	/**
	 * 路径已存在
	 * */
	public static final int DN_EXISTS=3;

	private static final Map<Integer, String> DIC = new HashMap<Integer, String>();
	static {
		DIC.put(DN_NOT_EXISTS, "DN_NOT_EXISTS:this[{0}] dn not Exists");
		DIC.put(NODE_EXISTS, "NODE IS EXISTS:the node {0} is Exists");
	}
	/**
	 * 根据errorCode获得描述
	 * @param errorCode 请看DBErrorCode.xxx
	 * @param var 要替换的值
	 * @return 描述
	 */
	public static String getDesc(int errorCode, String... var) {
		String desc = DIC.get(errorCode);
		if (desc == null) {
			return "unkown";
		}
		if (var != null) {
			for (int i = 0; i < var.length; i++){
				desc = desc.replaceAll("\\{" + i + "\\}", var[i]);
			}
		}
		return desc;
	}
}
