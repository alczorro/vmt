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

/**
 * @author lvly
 * @since 2013-8-2
 */
public class MQCreateDepartMessage extends MQBaseMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1209881252444101656L;
	/**
	 * @param operation
	 * @param scope
	 */
	public MQCreateDepartMessage(VmtDepart dept,String parentDn) {
		super(MQBaseMessage.OPERATION_CREATE, MQBaseMessage.SCOPE_DEPT);
		this.dept=dept;
		this.parentDn=parentDn;
	}
	private VmtDepart dept;
	private String parentDn;
	public VmtDepart getDept() {
		return dept;
	}
	public void setDept(VmtDepart dept) {
		this.dept = dept;
	}
	public String getParentDn() {
		return parentDn;
	}
	public void setParentDn(String parentDn) {
		this.parentDn = parentDn;
	}
}
