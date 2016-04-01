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
package net.duckling.vmt.domain.ldap;

import net.duckling.vmt.common.ldap.LdapMapping;
import net.duckling.vmt.common.ldap.LdapObjectClass;
import net.duckling.vmt.common.ldap.LdapType;
import net.duckling.vmt.domain.KEY;
/**
 * 节点的抽象类，是group,org 的交集
 * @author lvly
 * @since 2013-6-21
 */
@LdapMapping(objectClass=LdapObjectClass.CLASS_VMT_NODE)
public class LdapNode {
	/**
	 * 节点的dn
	 */
	@LdapMapping(type = LdapType.DN)
	private String dn;
	/**
	 * 当前显示路径，以逗号分隔
	 */
	@LdapMapping("vmt-current-display")
	private String currentDisplay;
	/**
	 * 标识
	 */
	@LdapMapping(value="vmt-symbol")
	private String symbol;
	/**
	 * 来源
	 */
	@LdapMapping(value=KEY.DB_VMT_FROM)
	private String from;
	/**
	 * 来源是ddl同步
	 */
	public static final String FROM_DDL="ddl";
	/**
	 * 来源是邮箱同步
	 */
	public static final String FROM_CORE_MAIL="coreMail";
	/**
	 * 来源是dchat创建
	 */
	public static final String FROM_DCHAT="dchat";
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
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
	/**
	 * 增加显示路径
	 * @param display
	 * @return
	 */
	public String addDisplay(String display){
		return this.currentDisplay+","+display;
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
