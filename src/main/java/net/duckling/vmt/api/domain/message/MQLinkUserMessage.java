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
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.api.domain.VmtUser;

/**
 * @author lvly
 * @since 2013-8-2
 */
public class MQLinkUserMessage extends MQBaseMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6470886033402544877L;

	/**
	 * @param operation
	 * @param scope
	 */
	public MQLinkUserMessage(List<VmtUser> users,VmtOrg org,VmtDepart dept,VmtGroup group) {
		super(OPERATION_CREATE, SCOPE_USER);
		this.users=users;
		this.org=org;
		this.dept=dept;
		this.group=group;
	}
	
	private List<VmtUser> users;
	private VmtOrg org;
	private VmtDepart dept;
	private VmtGroup group;
	
	public boolean isGroup(){
		return org==null&&dept==null&&group!=null;
	}
	public boolean isOrg(){
		return org!=null&&group==null;
	}
	public boolean isDept(){
		return org!=null&&dept!=null&&group==null;
	}

	public List<VmtUser> getUsers() {
		return users;
	}
	public void setUsers(List<VmtUser> users) {
		this.users = users;
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
	public VmtGroup getGroup() {
		return group;
	}
	public void setGroup(VmtGroup group) {
		this.group = group;
	}
	
}
