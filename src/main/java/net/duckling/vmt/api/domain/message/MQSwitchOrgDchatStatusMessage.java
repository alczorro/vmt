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
package net.duckling.vmt.api.domain.message;

import net.duckling.vmt.api.domain.VmtOrg;

public class MQSwitchOrgDchatStatusMessage extends MQBaseMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7721558616038602671L;
	private VmtOrg org;
	private boolean isOpen;
	public MQSwitchOrgDchatStatusMessage(VmtOrg org,boolean isOpen){
		super(MQBaseMessage.OPERATION_SWITCH_DCHAT,MQBaseMessage.SCOPE_ORG);
		this.org=org;
		this.isOpen=isOpen;
	}
	public VmtOrg getOrg() {
		return org;
	}
	public void setOrg(VmtOrg org) {
		this.org = org;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	

}
