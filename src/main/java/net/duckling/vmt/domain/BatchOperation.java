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
package net.duckling.vmt.domain;

/**
 * @author lvly
 * @since 2013-8-28
 */
public class BatchOperation {
	public static final String TYPE_UPDATE="update";
	public static final String TYPE_IMPORT="import";
	public static final String TYPE_REGIST_UMT="regist.umt";
	public static final String TYPE_REGIST_COREMAIL="regist.coreMail";
	private String type;
	private String beforename;
	private String beforecurrentDisplay;
	private String teamDn;
	private int index;
	private int all;
	
	
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}

	public String getTeamDn() {
		return teamDn;
	}

	public void setTeamDn(String teamDn) {
		this.teamDn = teamDn;
	}

	public String getBeforecurrentDisplay() {
		return beforecurrentDisplay;
	}

	public void setBeforecurrentDisplay(String beforecurrentDisplayName) {
		this.beforecurrentDisplay = beforecurrentDisplayName;
	}

	public String getBeforename() {
		return beforename;
	}

	public void setBeforename(String beforename) {
		this.beforename = beforename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
