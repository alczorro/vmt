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
public class MQMoveDepartMessage extends MQBaseMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6052589506984488232L;
	/**
	 * @param operation
	 * @param scope
	 */
	public MQMoveDepartMessage(VmtDepart dept,boolean containSelf,VmtDepart targetDept,VmtOrg org) {
		super(MQBaseMessage.OPERATION_MOVE, MQBaseMessage.SCOPE_DEPT);
		this.containSelf=containSelf;
		this.targetDept=targetDept;
		this.dept=dept;
		this.targetOrg=org;
	}
	private VmtDepart dept;
	private boolean containSelf;
	private VmtDepart targetDept;
	private VmtOrg targetOrg;
	public VmtDepart getDept() {
		return dept;
	}
	public void setDept(VmtDepart dept) {
		this.dept = dept;
	}
	public boolean isContainSelf() {
		return containSelf;
	}
	public void setContainSelf(boolean containSelf) {
		this.containSelf = containSelf;
	}
	public VmtDepart getTargetDept() {
		return targetDept;
	}
	public void setTargetDept(VmtDepart targetDept) {
		this.targetDept = targetDept;
	}
	public VmtOrg getTargetOrg() {
		return targetOrg;
	}
	public void setTargetOrg(VmtOrg vmtOrg) {
		this.targetOrg = vmtOrg;
	}
}
