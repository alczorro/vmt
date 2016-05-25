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

import net.duckling.vmt.api.domain.VmtGroup;

public class MQSwitchGroupDchatStatusMessage extends MQBaseMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2829180264095178591L;
	/**
	 * 
	 */
	private VmtGroup group;
	private boolean isOpen;
	public MQSwitchGroupDchatStatusMessage(VmtGroup group,boolean isOpen){
		super(MQBaseMessage.OPERATION_SWITCH_DCHAT,MQBaseMessage.SCOPE_GROUP);
		this.group=group;
		this.isOpen=isOpen;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public VmtGroup getGroup() {
		return group;
	}
	public void setGroup(VmtGroup group) {
		this.group = group;
	}
	
	

}
