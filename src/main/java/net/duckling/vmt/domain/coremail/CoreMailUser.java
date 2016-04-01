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
package net.duckling.vmt.domain.coremail;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.domain.ldap.LdapUser;

/**
 * 用于承载CoreMail用户信息的bean
 * @author lvly
 * @since 2013-5-29
 */
public class CoreMailUser {
	/**
	 * 用户邮箱
	 * */
	private String email;
	/**
	 * 用户所属部门id
	 * */
	private String ouId;
	/**
	 * 用户所属组织id
	 * */
	private String orgId;
	
	/**
	 * 是否是组织管理员
	 * */
	private boolean isAdmin;
	
	/**
	 * 权重
	 * */
	private int listRank;
	
	/**
	 * 是否可见
	 * */
	private boolean visible;
	
	private String name;
	
	private String password;
	
	private String domain;
	
	/**
	 * 日期格式为 yyyy-MM-dd
	 * */
	private String expireTime;
	
	private String status;
	
	public String getStatus() {
		if(CommonUtils.isNull(status)){
			return "0";
		}
		return status;
	}
	public void setStatus(String ldapStatus) {
		this.status = getCoreMailStatus(ldapStatus);
	}
	public String getCoreMailStatus(){
		return CoreMailUser.getCoreMailStatus(status);
	}	
	public static String getCoreMailStatus(String status){
		switch (status) {
		case LdapUser.ACCOUNT_STATUS_LOCKED: {
			return  "4";
		}
		case LdapUser.ACCOUNT_STATUS_NORMAL: {
			return "0";
		}
		case LdapUser.ACCOUNT_STATUS_STOP: {
			return "1";
		}
		}
		return "0";
	}
	
	
	
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public int getListRank() {
		return listRank;
	}
	public void setListRank(int listRank) {
		this.listRank = listRank;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOuId() {
		return ouId;
	}
	public void setOuId(String ouId) {
		this.ouId = ouId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the isAdmin
	 */
	public boolean isAdmin() {
		return isAdmin;
	}
	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
}
