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

import net.duckling.vmt.api.domain.VmtOrg;

/**
 * @author lvly
 * @since 2013-8-2
 */
public class MQRefreshOrgMessage extends MQBaseMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7102257333164555291L;

	/**
	 * @param operation
	 * @param scope
	 */
	public MQRefreshOrgMessage(VmtOrg org) {
		super(OPERATION_REFRESH,SCOPE_ORG);
		this.org=org;
	}

	private VmtOrg org;

	public VmtOrg getOrg() {
		return org;
	}

	public void setOrg(VmtOrg org) {
		this.org = org;
	}

}
