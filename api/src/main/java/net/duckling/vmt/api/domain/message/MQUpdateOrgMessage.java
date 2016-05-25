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
package net.duckling.vmt.api.domain.message;

import java.util.List;

import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.api.domain.VmtUser;

/**
 * @author lvly
 * @since 2013-8-2
 */
public class MQUpdateOrgMessage extends MQBaseMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8099675457617432086L;




	/**
	 * @param operation
	 * @param scope
	 */
	public MQUpdateOrgMessage(VmtOrg org,List<VmtUser> admins) {
		super(OPERATION_UPDATE, SCOPE_ORG);
		this.org=org;
		this.admins=admins;
	}

	private VmtOrg org;
	private List<VmtUser> admins;
	private String beforeName;
	
	
	

	public String getBeforeName() {
		return beforeName;
	}

	public void setBeforeName(String beforeName) {
		this.beforeName = beforeName;
	}

	public List<VmtUser> getAdmins() {
		return admins;
	}

	public void setAdmins(List<VmtUser> admins) {
		this.admins = admins;
	}

	public VmtOrg getOrg() {
		return org;
	}

	public void setOrg(VmtOrg org) {
		this.org = org;
	}
}
