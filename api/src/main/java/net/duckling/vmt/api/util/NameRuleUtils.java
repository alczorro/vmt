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
package net.duckling.vmt.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.duckling.cloudy.common.CommonUtils;

public class NameRuleUtils {
	public static  boolean isNodeIDMatch(String value){
		return match("^[/[a-zA-Z0-9\\-\\_\\.\\!\\~\\*\\'()\\u4e00-\\u9fa5]{0,1023}]+$",value);
	}
	private static boolean match(String regix,String value){
		Pattern p=Pattern.compile(regix);
		Matcher matcher=p.matcher(CommonUtils.trim(value));
		return matcher.find();
	}
	public static boolean isDDLNameMatch(String value){
		return !match("[:\\\\/<>*?|\"]",value)&&!CommonUtils.isNull(value);
	}
	public static boolean isSymbolMatch(String value){
		return match("^[a-zA-Z0-9\\-]{1,1023}$",value);
	}
}
