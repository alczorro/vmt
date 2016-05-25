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
 * 组织结构实体类
 * @author lvly
 * @since 2013-5-21
 */
public class VmtOrg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -16339339704120944L;
	/**
	 * 组织机构的dn
	 * */
	private String dn;
	/**
	 *组织机构的标识
	 * */
	private String symbol;
	/**
	 * 组织机构里面包含多少人，包含未激活的用户
	 * */
	private int count;
	
	/**
	 *组织机构的显示名称
	 * */
	private String name;
	/**
	 * 组织机构的创建者，umtId
	 */
	private String creator;
	
	/**
	 * 组织机构的管理员
	 * */
	private String[] admins;
	
	/**
	 * 组织机构的显示名，不需要用户给出
	 * */
	
	private String currentDisplay;
	
	/**
	 * logo {baseUrl}/{logo}即为返回图片地址
	 * */
	private String logo;
	
	/**
	 * 给移动端用的，480*960
	 * */
	private String mobileLogo;
	
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
	/*公开，私密，审核*/
	private String privilege;
	/**
	 * 是否是院内用户
	 * */
	private boolean isCas;
	/**
	 * 是否是邮箱用户
	 * */
	private boolean isCoreMail;
	/**
	 * 组织包含的域名
	 * */
	private String[] domain;
	
	/**
	 * 类型
	 * */
	private String type;
	
	/**
	 * 是否开通科信
	 * */
	private boolean openDchat;
	
	//add by lvly 140429
//	/**
//	 * 是否开通app列表
//	 * */
//	private boolean isAppOpen;
//	
//	/**
//	 * app列表详细信息
//	 * */
//	private List<VmtApp> apps;
	
	
	
	
	
//	public boolean isAppOpen() {
//		return isAppOpen;
//	}
//	public void setAppOpen(boolean isAppOpen) {
//		this.isAppOpen = isAppOpen;
//	}
//	public List<VmtApp> getApps() {
//		return apps;
//	}
//	public void setApps(List<VmtApp> apps) {
//		this.apps = apps;
//	}
	public boolean isOpenDchat() {
		return openDchat;
	}
	public void setOpenDchat(boolean openDchat) {
		this.openDchat = openDchat;
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
	public String[] getDomain() {
		return domain;
	}
	public void setDomain(String[] domain) {
		this.domain = domain;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPrivilege(String privilege) {
		this.privilege=privilege;
	}
	public String getPrivilege(){
		return this.privilege;
	}
	
	
	
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
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
		return admins==null?null:admins.clone();
	}
	/**
	 * @param admins
	 */
	public void setAdmins(String[] admins) {
		this.admins = admins==null?null:admins.clone();
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
	/**
	 * @return the currentDisplay
	 */
	public String getCurrentDisplay() {
		return currentDisplay;
	}
	/**
	 * @param currentDisplay the currentDisplay to set
	 */
	public void setCurrentDisplay(String currentDisplay) {
		this.currentDisplay = currentDisplay;
	}
	
	
	
}
