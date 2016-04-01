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
package net.duckling.vmt.domain;

import cn.vlabs.umt.oauth.UserInfo;

/**
 * VMT 存放在session里的用户数据
 * @author lvly
 * @since 2013-6-21
 */
public class VmtSessionUser {
	/**
	 * Oauth用户对象
	 */
	private UserInfo userInfo;
	/**
	 * 如果存在，则代表这个domain是coremail所承认的 域名
	 */
	private String coreMailDomain;
	/**
	 * 是否是超级管理员
	 */
	private boolean isSuperAdmin;
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public String getCoreMailDomain() {
		return coreMailDomain;
	}
	public void setCoreMailDomain(String coreMailDomain) {
		this.coreMailDomain = coreMailDomain;
	}
	public boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}
	public void setIsSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	

	

}
