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


/**
 * api里面封装用户数据用的bean
 * @author lvly
 * @since 2013-5-21
 */
public class VmtUser {
	/**
	 * 用户dn
	 * */
	private String dn;
	/**
	 * 用户的umtId，需要umt给出
	 * */
	private String umtId;
	/**
	 * 用户的真实姓名
	 * */
	private String name;
	/**
	 * 用户的激活状态，有三个值，true-已激活，false-未激活，refuse-拒绝
	 * */
	private String status;
	/**
	 * 用户的登录邮箱
	 * */
	private String cstnetId;
	/**
	 * 用户目录的显示状态，用逗号分隔
	 * */
	private String currentDisplay;
	
	/**
	 * 权重
	 */
	private int listRank;
	
	/**
	 *是否可见 
	 */
	private boolean visible=true;
	
	
	/**
	 * 性别
	 * */
	private String sex;
	
	public static final String SEX_MALE="male";
	public static final String SEX_FEMALE="female";
	
	private String userFrom;
	private String accountStatus;
	
	private boolean disableDchat;
	private String custom1;
	private String custom2;
	
	
	
	public boolean isDisableDchat() {
		return disableDchat;
	}
	public void setDisableDchat(boolean disableDchat) {
		this.disableDchat = disableDchat;
	}
	public String getCustom1() {
		return custom1;
	}
	public void setCustom1(String custom1) {
		this.custom1 = custom1;
	}
	public String getCustom2() {
		return custom2;
	}
	public void setCustom2(String custom2) {
		this.custom2 = custom2;
	}
	public String getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	/**
	 * 办公地址
	 * */
	private String office;
	
	/**
	 * 办公电话
	 * */
	private String officePhone;
	
	/**
	 * 职称
	 * */
	private String title;
	
	/**
	 * 手机
	 */
	private String telephone;
	
	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public int getListRank() {
		return listRank;
	}
	public void setListRank(int listRank) {
		this.listRank = listRank;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrentDisplay() {
		return currentDisplay;
	}
	public void setCurrentDisplay(String currentDisplay) {
		this.currentDisplay = currentDisplay;
	}
	/**
	 * @return the cstnetId
	 */
	public String getCstnetId() {
		return cstnetId;
	}
	/**
	 * @param cstnetId the cstnetId to set
	 */
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}
	

}
