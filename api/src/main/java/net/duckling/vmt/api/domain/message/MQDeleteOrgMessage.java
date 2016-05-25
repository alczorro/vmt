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
public class MQDeleteOrgMessage extends MQBaseMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1454147594777638781L;

	/**
	 * @param operation
	 * @param scope
	 */
	public MQDeleteOrgMessage(String operation, String scope) {
		super(operation, scope);
	}

	/**
	 * @param convert
	 */
	public MQDeleteOrgMessage(VmtOrg org) {
		super(OPERATION_DELETE,SCOPE_ORG);
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
