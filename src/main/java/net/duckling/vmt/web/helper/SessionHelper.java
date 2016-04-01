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
package net.duckling.vmt.web.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;

/**
 * 对session 的一些操作
 * @author lvly
 * @since 2013-5-10
 */
public final class SessionHelper {
	private SessionHelper(){}
	/**
	 * 判断用户是否登录
	 * @param request 请求
	 * @return boolean 是否登录
	 * */
	public static boolean hasLogin(HttpServletRequest request){
		HttpSession session=request.getSession();
		if(session==null){
			return false;
		}
		return session.getAttribute(KEY.SESSION_LOGIN_USER)!=null;
	}
	/**
	 * 获得session里面的用户信息
	 * @param raquest
	 * @return 用户信息
	 * */
	public static VmtSessionUser getUserInfo(HttpServletRequest request){
		return (VmtSessionUser) request.getSession().getAttribute(KEY.SESSION_LOGIN_USER);
	}

}
