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
package net.duckling.vmt.common.readFile.impl;

import net.duckling.cloudy.common.CommonUtils;

/**
 * @author lvly
 * @since 2013-8-28
 */
public class XlsBatchUtils {
	public static String dealCurrentDisplay(String currentDisplay){
		if(CommonUtils.isNull(currentDisplay)||!currentDisplay.contains(",")){
			return "/";
		}
		String result=currentDisplay.replace(",", "/");
		result=result.substring(result.indexOf("/"));
		return result;
	}
}
