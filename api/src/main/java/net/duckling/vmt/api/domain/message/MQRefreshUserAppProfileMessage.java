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

import java.util.List;

import net.duckling.vmt.api.domain.VmtApiApp;

public class MQRefreshUserAppProfileMessage extends MQBaseMessage  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5771093353144532045L;
	private String umtId;
	private String cstnetId;
	private  List<VmtApiApp> apps;
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	public String getCstnetId() {
		return cstnetId;
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}
	public List<VmtApiApp> getApps() {
		return apps;
	}
	public void setApps(List<VmtApiApp> apps) {
		this.apps = apps;
	}
	public MQRefreshUserAppProfileMessage(List<VmtApiApp> apps) {
		super(OPERATION_REFRESH,SCOPE_ORG);
		this.apps=apps;
	}
}
