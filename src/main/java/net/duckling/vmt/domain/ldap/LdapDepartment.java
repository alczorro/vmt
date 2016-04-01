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
import net.duckling.vmt.common.util.HashCodeGenerator;
import net.duckling.vmt.common.util.PinyinUtils;

/**
 * 部门实体类，所属于组织
 * @author lvly
 * @since 2013-5-2
 */
@LdapMapping(objectClass={LdapObjectClass.CLASS_VMT_DEPART,LdapObjectClass.CLASS_VMT_NODE})
public class LdapDepartment implements Comparable<LdapDepartment>,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5877284643315813495L;
	/**
	 * 部门的dn
	 */
	@LdapMapping(type = LdapType.DN)
	private String dn;
	/**
	 * 部门名称
	 */
	@LdapMapping("vmt-name")
	private String name;
	/**
	 *部门的创建者，umtId
	 */
	@LdapMapping("vmt-creator")
	private String creator;
	/**
	 * 当前显示路径，以逗号分隔
	 */
	@LdapMapping("vmt-current-display")
	private String currentDisplay;
	@LdapMapping("vmt-visible")
	private boolean isVisible=true;
	/**
	 * 部门标识，在一个组织里面唯一
	 */
	@LdapMapping(value="vmt-symbol",type=LdapType.RDN)
	private String symbol;
	@Override
	public int compareTo(LdapDepartment o) {
		int result=o.getListRank()-this.getListRank();
		if(result==0){
			return PinyinUtils.getPinyin(this.getName()).compareTo(PinyinUtils.getPinyin(o.getName())); 
		}
		return result;
	}
	private String oid;
	
	public String getOid() {
		this.oid=HashCodeGenerator.getCode(this.dn);
		return oid;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	private int count;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * 权重
	 */
	@LdapMapping(value="vmt-list-rank",type=LdapType.UPDATE_NULL)
	private int listRank;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCurrentDisplay() {
		return currentDisplay;
	}
	public void setCurrentDisplay(String currentDisplay) {
		this.currentDisplay = currentDisplay;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
