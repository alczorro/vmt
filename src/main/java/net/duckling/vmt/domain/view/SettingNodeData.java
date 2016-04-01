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
package net.duckling.vmt.domain.view;

import java.util.Date;

import net.duckling.vmt.common.util.HashCodeGenerator;

/**
 * 设置页面要用到的数据结构
 * @author lvly
 * @since 2013-6-21
 */
public class SettingNodeData {
	/**
	 * 管理员
	 */
	private AdminData[] admins;
	/**
	 * 当前节点的dn
	 */
	private String dn;
	/**
	 * 组织或者群组的名称
	 */
	private String name;
	/**
	 * 唯一标识，无任何意义，可以前端当做id
	 */
	private String oid;
	/**
	 * 当前节点的密码
	 */
	private String password;
	/**
	 * 权限
	 */
	private String privilege;
	/**
	 * 是否让成员可见
	 */
	private boolean memberVisible;
	/**
	 *是否可以看到未激活的用户 
	 */
	private boolean unconfirmVisible;
	/**
	 * 团队来源
	 */
	private String from;
	/**
	 * 标识
	 */
	private String symbol;
	private Date fromDate;
	private Date toDate;
	private boolean isGroup;
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	private Object data;
	
	public boolean isUpgraded() {
		//return isUpgraded;
		return true;
	}

	public void setUpgraded(boolean isUpgraded) {
		this.isUpgraded = isUpgraded;
	}

	private boolean isUpgraded;
	
	private int count;
	
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(long fromDate) {
		if(fromDate!=0){
			this.fromDate = new Date(fromDate);
		}
		
	}

	public Date getToDate(){
		return toDate;
	}

	public void setToDate(long toDate) {
		if(toDate!=0){
			this.toDate = new Date(toDate);
		}
	}
	
	/**
	 * 
	 * */
	private int logoId;
	
	private int mobileLogo;
	private int pcLogo;
	
	public int getMobileLogo() {
		return mobileLogo;
	}

	public void setMobileLogo(int mobileLogo) {
		this.mobileLogo = mobileLogo;
	}

	public int getPcLogo() {
		return pcLogo;
	}

	public void setPcLogo(int pcLogo) {
		this.pcLogo = pcLogo;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getLogoId() {
		return logoId;
	}
	public void setLogoId(int logoId) {
		this.logoId = logoId;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	public boolean isMemberVisible() {
		return memberVisible;
	}
	public void setMemberVisible(boolean memberVisible) {
		this.memberVisible = memberVisible;
	}
	public boolean isUnconfirmVisible() {
		return unconfirmVisible;
	}
	public void setUnconfirmVisible(boolean unconfirmVisible) {
		this.unconfirmVisible = unconfirmVisible;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	/**
	 * @return
	 */
	public AdminData[] getAdmins() {
		return admins==null?null:admins.clone();
	}
	/**
	 * @param admins
	 */
	public void setAdmins(AdminData[] admins) {
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
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return
	 */
	public String getOid() {
		this.oid=HashCodeGenerator.getCode(this.dn);
		return this.oid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

}
