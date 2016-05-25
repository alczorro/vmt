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

import net.duckling.vmt.api.domain.VmtDepart;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.api.domain.VmtUser;

/**
 * @author lvly
 * @since 2013-8-2
 */
public class MQMoveUserMessage extends MQBaseMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1670679688963466164L;
	/**
	 * @param operation
	 * @param scope
	 */
	public MQMoveUserMessage(List<VmtUser> user,VmtOrg org,VmtDepart dept) {
		super(MQBaseMessage.OPERATION_MOVE, MQBaseMessage.SCOPE_USER);
		this.user=user;
		this.org=org;
		this.dept=dept;
	}
	private List<VmtUser> user;
	private VmtOrg org;
	private VmtDepart dept;
	public List<VmtUser> getUser() {
		return user;
	}
	public void setUser(List<VmtUser> user) {
		this.user = user;
	}
	public VmtOrg getOrg() {
		return org;
	}
	public void setOrg(VmtOrg org) {
		this.org = org;
	}
	public VmtDepart getDept() {
		return dept;
	}
	public void setDept(VmtDepart dept) {
		this.dept = dept;
	}
}
