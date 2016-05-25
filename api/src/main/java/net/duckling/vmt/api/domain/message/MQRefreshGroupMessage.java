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

import net.duckling.vmt.api.domain.VmtGroup;

/**
 * @author lvly
 * @since 2013-8-2
 */
public class MQRefreshGroupMessage extends MQBaseMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4117283526678334501L;

	/**
	 * @param operation
	 * @param scope
	 */
	public MQRefreshGroupMessage(String operation, String scope) {
		super(operation, scope);
	}

	/**
	 * @param convert
	 */
	public MQRefreshGroupMessage(VmtGroup group) {
		super(OPERATION_REFRESH,SCOPE_GROUP);
		this.group=group;
	}

	private VmtGroup group;

	public VmtGroup getGroup() {
		return group;
	}

	public void setGroup(VmtGroup group) {
		this.group = group;
	}

}
