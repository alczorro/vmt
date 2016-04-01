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
package net.duckling.vmt.domain.index;

import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;

/**
 * @author lvly
 * @since 2013-7-16
 */
@TableMapping("vmt_user_team_rel")
public class VmtIndex {
	@FieldMapping(type=Type.ID)
	private int id;
	
	@FieldMapping("team_dn")
	private String teamDN;
	@FieldMapping("team_name")
	private String teamName;
	@FieldMapping("umt_id")
	private String umtId;
	@FieldMapping("user_name")
	private String userName;
	@FieldMapping("user_cstnet_id")
	private String userCstnetId;
	
	@FieldMapping("user_dn")
	private String userDN;
	
	@FieldMapping
	private int type;
	@FieldMapping
	private String status;
	@FieldMapping
	private String symbol;
	@FieldMapping("user_visible")
	private boolean visible;
	

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public static final int TYPE_ORG=1;
	
	public static final int TYPE_GROUP=2;

	public int getId() {
		return id;
	}

	public String getUserDN() {
		return userDN;
	}

	public void setUserDN(String userDN) {
		this.userDN = userDN;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTeamDN() {
		return teamDN;
	}

	public void setTeamDN(String teamDN) {
		this.teamDN = teamDN;
	}

	public String getUmtId() {
		return umtId;
	}

	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}

	public int getType() {
		return type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getUserCstnetId() {
		return userCstnetId;
	}

	public void setUserCstnetId(String userCstnetId) {
		this.userCstnetId = userCstnetId;
	}

}
