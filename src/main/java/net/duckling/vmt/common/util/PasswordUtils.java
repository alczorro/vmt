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
package net.duckling.vmt.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.duckling.cloudy.common.CommonUtils;

public class PasswordUtils {
	public static final Pattern ALL_SMALL=Pattern.compile("^[a-z]*$");
	public static final Pattern ALL_BIG=Pattern.compile("^[A-Z]*$");
	public static final Pattern ALL_NUM=Pattern.compile("^[0-9]*$");
	public static final Pattern HAS_SPACE=Pattern.compile("\\s+");
	public static boolean isMatch(Pattern p,String password){
		Matcher matcher=p.matcher(password);
		return matcher.find();
	}
	public static boolean canUse(String pwd) {
		pwd=CommonUtils.trim(pwd);
			return	
				!CommonUtils.isNull(pwd)&&
				pwd.length()>=8&
				!isMatch(ALL_SMALL, pwd)&&
				!isMatch(ALL_BIG,pwd)&&
				!isMatch(ALL_NUM,pwd);
	}
}
