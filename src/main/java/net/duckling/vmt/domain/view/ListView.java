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
package net.duckling.vmt.domain.view;

import net.duckling.vmt.common.util.HashCodeGenerator;
import net.duckling.vmt.domain.ldap.LdapDepartment;

/**
 * index2，层级视图里面承载数据用
 * @author lvly
 * @since 2013-5-10
 */
public class ListView{
	public static final String TYPE_DEPART="folder";
	public static final String TYPE_USER="link";
	
	/**
	 * 当前实体的dn
	 */
	private String dn;
	/**
	 * 如果是用户的话，代表邮箱
	 */
	private String email;
	/**
	 * 唯一标识，临时
	 */
	private String oid;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 类型，部门或者人员
	 */
	private String type;
	/**
	 * 当前显示路径，以逗号分隔
	 */
	private String currentDisplay;
	
	/**
	 * 排序权重
	 */
	private int listRank;
	/**
	 * 状态，如果是部门的话，无此值
	 */
	private String status;
	
	private boolean visible=true;
	private String mobile;
	private String umtId;
	
	
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	/** 构造函数，从部门构造一个对象
	 * @param depart
	 */
	public ListView(LdapDepartment depart){
		this.type=TYPE_DEPART;
		this.currentDisplay=depart.getCurrentDisplay();
		this.name=depart.getName();
		this.dn=depart.getDn();
	}
	public ListView(){
		
	}
	
	public String getCurrentDisplay() {
		return currentDisplay;
	}
	public void setCurrentDisplay(String currentDisplay) {
		this.currentDisplay = currentDisplay;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	
	public int getListRank() {
		return listRank;
	}
	public void setListRank(int listRank) {
		this.listRank = listRank;
	}
	/**
	 * @return
	 */
	public String getOid() {
		if(TYPE_USER.equals(this.type)){
			this.oid=HashCodeGenerator.getCode(email);
		}else{
			this.oid= HashCodeGenerator.getCode(dn);
		}
		return this.oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
