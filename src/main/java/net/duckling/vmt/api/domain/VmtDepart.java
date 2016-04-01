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
 * api包里封装部门信息用的bean
 * @author lvly
 * @since 2013-5-21
 */
public class VmtDepart implements Serializable{
	private static final long serialVersionUID = 7596428860782313351L;
	/**
	 * 部门dn
	 * */
	private String dn;
	/**
	 * 部门名称
	 * */
	private String name;
	/**
	 * 部门标识
	 * */
	private String symbol;
	/**
	 * 创建者
	 * */
	private String creator;
	/**
	 * 该部门显示路径，包含自己
	 * */
	private String currentDisplay;
	/**
	 * 权重
	 */
	private int listRank;
	/**
	 * 是否可见
	 * 
	 * */
	private boolean visible;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
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
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	
	
	

}
