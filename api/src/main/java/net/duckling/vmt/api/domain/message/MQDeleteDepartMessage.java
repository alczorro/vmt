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

import net.duckling.vmt.api.domain.VmtDepart;
import net.duckling.vmt.api.domain.VmtOrg;

/**
 * @author lvly
 * @since 2013-8-2
 */
public class MQDeleteDepartMessage extends MQBaseMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6599758194816136245L;
	/**
	 * @param operation
	 * @param scope
	 */
	public MQDeleteDepartMessage(VmtOrg org,VmtDepart dept) {
		super(OPERATION_DELETE, SCOPE_DEPT);
		this.dept=dept;
		this.org=org;
	}
	private VmtDepart dept;
	private VmtOrg org;
	public VmtDepart getDept() {
		return dept;
	}
	public void setDept(VmtDepart dept) {
		this.dept = dept;
	}
	public VmtOrg getOrg() {
		return org;
	}
	public void setOrg(VmtOrg org) {
		this.org = org;
	}
	
	
}
