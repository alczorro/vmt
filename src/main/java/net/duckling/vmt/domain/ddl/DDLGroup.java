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
package net.duckling.vmt.domain.ddl;

/**
 * @author lvly
 * @since 2013-5-30
 */
public class DDLGroup {
	private String teamName;
	private String teamCode;
	private String admin[];
	private String users[];
	private String accessType;
	
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	/**
	 * @return
	 */
	public String[] getAdmin() {
		return admin==null?null:admin.clone();
	}
	/**
	 * @param admin
	 */
	public void setAdmin(String[] admin) {
		this.admin = admin==null?null:admin.clone();
	}
	/**
	 * @return
	 */
	public String[] getUsers() {
		return users==null?users:users.clone();
	}
	public void setUsers(String[] users) {
		this.users = users==null?null:users.clone();
	}

}
