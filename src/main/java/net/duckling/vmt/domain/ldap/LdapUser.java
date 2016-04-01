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
package net.duckling.vmt.domain.ldap;

import java.io.Serializable;

import net.duckling.vmt.common.ldap.LdapMapping;
import net.duckling.vmt.common.ldap.LdapObjectClass;
import net.duckling.vmt.common.ldap.LdapType;
import net.duckling.vmt.common.util.CharUtils;
import net.duckling.vmt.common.util.HashCodeGenerator;

/**
 * 用户实体类
 * @author lvly
 * @since 2013-4-27
 */
@LdapMapping(objectClass={LdapObjectClass.CLASS_VMT_USER,LdapObjectClass.CLASS_VMT_NODE})
public class LdapUser implements Serializable,Comparable<LdapUser>{
	
	private static final long serialVersionUID = -3458418429250299479L;
	public static final String STATUS_ACTIVE="true";
	public static final String STATUS_TEMP="false";
	public static final String STATUS_APPLY="apply";
	public static final String STATUS_REFUSE="refuse";
	/**
	 * 用戶的dn
	 */
	@LdapMapping(type = LdapType.DN)
	private String dn;
	
	/**
	 * 用戶的邮箱
	 */
	@LdapMapping("vmt-id")
	private String cstnetId;
	
	/**
	 * 用户状态，激活，未激活，拒绝
	 */
	@LdapMapping("vmt-status")
	private String status=STATUS_TEMP;
	
	/**
	 * 用户真实姓名
	 */
	@LdapMapping("vmt-name")
	private String name;
	
	/**
	 * 用户真实姓名的拼音
	 */
	@LdapMapping("vmt-pinyin")
	private String pinyin;
	
	/**
	 * 用户的umtId
	 */
	@LdapMapping(value="vmt-umtId",type=LdapType.RDN)
	private String umtId;
	
	/**
	 * 用户所属组织机构或者群组的dn
	 */
	@LdapMapping("vmt-root")
	private String root;
	
	/**
	 * 拼音第一个字母
	 */
	@LdapMapping("vmt-search-index")
	private String code;
	
	/**
	 * 激活码
	 */
	@LdapMapping("vmt-random")
	private String random;
	
	/**
	 * 显示路径，以逗号分隔
	 */
	@LdapMapping("vmt-current-display")
	private String currentDisplay;
	
	/**
	 * 权重
	 */
	@LdapMapping(value="vmt-list-rank",type=LdapType.UPDATE_NULL)
	private int listRank;
	
	/**
	 *是否可见 
	 */
	@LdapMapping(value="vmt-visible")
	private boolean visible=true;
	
	@LdapMapping(value="vmt-sex")
	private String sex;
	
	@LdapMapping(value="vmt-expire-time")
	private String expireTime;
	
	public static final String SEX_MALE="male";
	public static final String SEX_FEMALE="female";
	
	public String getSexDisplay(){
		if(SEX_MALE.equals(sex)){
			return "男";
		}
		if(SEX_FEMALE.equals(sex)){
			return "女";
		}
		return "";
	}
	
	
	@LdapMapping(value="vmt-office")
	private String office;
	
	@LdapMapping(value="vmt-office-phone")
	private String officePhone;
	
	@LdapMapping(value="vmt-title")
	private String title;
	
	@LdapMapping(value="vmt-telephone")
	private String telephone;
	
	@LdapMapping(value="vmt-user-from")
	private String userFrom;

	public static final String USER_FROM_CORE_MAIL="coreMail";
	public static final String USER_FROM_UMT="umt";
	
	@LdapMapping(value="vmt-account-status")
	private String accountStatus=ACCOUNT_STATUS_NORMAL;
	public static final String ACCOUNT_STATUS_NORMAL="normal";
	public static final String ACCOUNT_STATUS_STOP="stop";
	public static final String ACCOUNT_STATUS_LOCKED="locked";
	
	
	public String getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public String getAccountStatusDisplay(){
		if(ACCOUNT_STATUS_NORMAL.equals(getAccountStatus())){
			return "正常";
		}
		if(ACCOUNT_STATUS_LOCKED.equals(getAccountStatus())){
			return "锁定";
		}
		if(ACCOUNT_STATUS_STOP.equals(getAccountStatus())){
			return "停用";
		}
		return "";
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
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
	
	public static String getCode(String pinyin){
		if(CharUtils.isFirstCharIsLetter(pinyin)){
			return pinyin.substring(0,1).toLowerCase();
		}
		return "#";
	}
	
	
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	public String getCurrentDisplay() {
		return currentDisplay;
	}

	public void setCurrentDisplay(String currentDisplay) {
		this.currentDisplay = currentDisplay;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	/**
	 * 用户的唯一标识，无真实含义，可用于前端js，辨认用户用
	 */
	private String oid;
	
	@LdapMapping(value="vmt-disable-dchat")
	private boolean disableDchat;
	
	@LdapMapping(value="vmt-custom-1")
	private String custom1;
	@LdapMapping(value="vmt-custom-2")
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
	/**
	 * 获得用户的唯一标识用
	 * @return
	 */
	public String getOid() {
		setOid();
		return this.oid;
	}
	@Override
	public int compareTo(LdapUser o) {
		if(o==null){
			return 1;
		}
		int result=o.listRank-this.listRank;
		if(result==0){
			return this.getPinyin().compareTo(o.getPinyin());
		}
		return result;
	}
	/**
	 * 
	 */
	public void setOid(){
		this.oid=HashCodeGenerator.getCode(this.cstnetId);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "{cstnetId:"+this.cstnetId+",trueName:"+this.name+",umtId:"+this.umtId+",root:"+this.root+"}\n";
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSex() {
		return sex;
	}
	public String getOffice() {
		return office;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public String getTitle() {
		return title;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getCstnetId() {
		return cstnetId;
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getUmtId() {
		return umtId;
	}

	public void setUmtId(String vmtId) {
		this.umtId = vmtId;
	}
}
