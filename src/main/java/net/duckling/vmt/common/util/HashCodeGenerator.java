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
package net.duckling.vmt.common.util;

import net.duckling.cloudy.common.CommonUtils;

/**
 * 把hashCode转换成，更通用一些的字符串
 * @author lvly
 * @since 2013-5-9
 */
public final class HashCodeGenerator {
	/**
	 * 
	 */
	private HashCodeGenerator(){
		
	}
	/**
	 * 通过字符串生成一些，好看一些的hasoCode
	 * @param str
	 * @return
	 */
	public static String getCode(String str){
		if(CommonUtils.isNull(str)){
			return "";
		}
		int hashCode=str.hashCode();
		if(hashCode<0){
			return "HCM"+-1*hashCode;
		}else{
			return "HC"+hashCode;
		}
	}
}
