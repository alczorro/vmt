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
package net.duckling.vmt.api.domain;
/**
 * 
 * 组织和域名的关系，一对多的关系
 * */
public class VmtOrgDomain {
	/**
	 * 标识
	 * 
	 * **/
	private String orgSymbol;
	/**
	 * 组织名称
	 * 
	 * */
	private String orgName;
	/**
	 * 是否院内用户
	 * */
	private boolean isCas;
	/**
	 * 是否邮箱用户
	 * */
	private boolean isCoreMail;
	/**
	 * 保留字段，用户类型
	 * */
	private int type;
	/**
	 * 域名
	 * */
	private String[] domains;
	public String getOrgSymbol() {
		return orgSymbol;
	}
	public void setOrgSymbol(String orgSymbol) {
		this.orgSymbol = orgSymbol;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public boolean isCas() {
		return isCas;
	}
	public void setCas(boolean isCas) {
		this.isCas = isCas;
	}
	public boolean isCoreMail() {
		return isCoreMail;
	}
	public void setCoreMail(boolean isCoreMail) {
		this.isCoreMail = isCoreMail;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String[] getDomains() {
		return domains;
	}
	public void setDomains(String[] domains) {
		this.domains = domains;
	}
	

}
