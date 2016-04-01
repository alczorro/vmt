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
package net.duckling.vmt.domain.index;

import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;

@TableMapping("vmt_org_domain")
public class OrgDomain {
	@FieldMapping(type = Type.ID)
	private int id;
	@FieldMapping("org_id")
	private int orgId;
	@FieldMapping("org_domain")
	private String domain;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public static String[] extractDomain(List<OrgDomain> domains){
		String[] result=new String[domains.size()];
		if(CommonUtils.isNull(domains)){
			return result;
		}
		int index=0;
		for(OrgDomain domain:domains){
			result[index++]=domain.getDomain();
		}
		return result;
	}
	
	
}
