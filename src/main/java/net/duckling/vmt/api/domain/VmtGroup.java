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

import java.io.Serializable;

/**
 * api里面封装群组信息的bean
 * 
 * @author lvly
 * @since 2013-5-21
 */
public class VmtGroup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1602339339704120944L;
	/**
	 * 群组的dn
	 **/
	private String dn;
	/**
	 * 群组的标识
	 * */
	private String symbol;
	/**
	 * 这个群组包含多少人，包含未激活
	 * */
	private int count;
	/**
	 * 群组名称
	 * */
	private String name;
	/**
	 * 群组的创建者，umtId
	 * */
	private String creator;

	/**
	 * 群组的管理员，umtId，多个
	 * */
	private String[] admins;

	/**
	 * 来源,一共有，dchat，coreMail，ddl等，不填也可,详件this.FROM_XXX
	 * */
	private String from;
	/**
	 * 用户来源是ddl
	 * */
	public static final String FROM_DDL = "ddl";
	/**
	 * 用户来源是coreMail
	 * */
	public static final String FROM_CORE_MAIL = "coreMail";
	/**
	 * 用户来源是dchat
	 * */
	public static final String FROM_DCHAT = "dchat";

	/**
	 * logo {baseUrl}/{logo}即为返回图片地址
	 * */
	private String logo;

	/**
	 * 给移动端用的，480*960
	 * */
	private String mobileLogo;

	private boolean openDchat;
	
	private String description;

	// add by lvly 140429
//	/**
//	 * 是否开通app列表
//	 * */
//	private boolean isAppOpen;
//
//	/**
//	 * app列表详细信息
//	 * */
//	private List<VmtApp> apps;
//
//	public boolean isAppOpen() {
//		return isAppOpen;
//	}
//
//	public void setAppOpen(boolean isAppOpen) {
//		this.isAppOpen = isAppOpen;
//	}
//
//	public List<VmtApp> getApps() {
//		return apps;
//	}
//
//	public void setApps(List<VmtApp> apps) {
//		this.apps = apps;
//	}

	public boolean isOpenDchat() {
		return openDchat;
	}

	public void setOpenDchat(boolean openDchat) {
		this.openDchat = openDchat;
	}

	/**
	 * 给pc端用的，145*156
	 * */
	private String pcLogo;

	public String getMobileLogo() {
		return mobileLogo;
	}

	public void setMobileLogo(String mobileLogo) {
		this.mobileLogo = mobileLogo;
	}

	public String getPcLogo() {
		return pcLogo;
	}

	public void setPcLogo(String pcLogo) {
		this.pcLogo = pcLogo;
	}

	/* 公开，私密，审核 */
	private String privilege;

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getPrivilege() {
		return this.privilege;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof VmtGroup)) {
			return false;
		}
		VmtGroup group = (VmtGroup) obj;
		return this.getDn().equals(group.getDn());
	}

	@Override
	public int hashCode() {
		return this.getDn().hashCode();
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return
	 */
	public String[] getAdmins() {
		if (admins == null) {
			return null;
		}
		return admins.clone();
	}

	public void setAdmins(String[] admins) {
		this.admins = (admins == null ? null : admins.clone());
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getName() {
		return name;
	}

	public void setName(String currentDisplay) {
		this.name = currentDisplay;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
