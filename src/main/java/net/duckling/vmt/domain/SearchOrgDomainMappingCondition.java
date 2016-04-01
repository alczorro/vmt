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
package net.duckling.vmt.domain;

import net.duckling.cloudy.common.CommonUtils;

public class SearchOrgDomainMappingCondition {
	private String name;
	private String domain;
	private String cas;
	private String coreMail;
	
	public boolean isDefaulCondition(){
		return (CommonUtils.isNull(coreMail))
				&&CommonUtils.isNull(cas)
				&&CommonUtils.isNull(domain)
				&&CommonUtils.isNull(name);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCas() {
		return cas;
	}

	public void setCas(String cas) {
		this.cas = cas;
	}

	public String getCoreMail() {
		return coreMail;
	}

	public void setCoreMail(String coreMail) {
		this.coreMail = coreMail;
	}

	

}
