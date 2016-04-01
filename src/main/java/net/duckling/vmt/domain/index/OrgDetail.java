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

import java.util.ArrayList;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;

@TableMapping("vmt_org")
public class OrgDetail {
	@FieldMapping(type = Type.ID)
	private int id;
	@FieldMapping("org_name")
	private String name;
	@FieldMapping("org_symbol")
	private String symbol;
	// 是否是coreMail组织
	@FieldMapping("is_coremail")
	private boolean isCoreMail;
	// 是否院内用户
	@FieldMapping("is_cas")
	private boolean isCas;
	// 类型，待定，可能的值有，用户自建
	@FieldMapping("type")
	private int type;
	
	private List<OrgDomain> domains=new ArrayList<OrgDomain>();
	public void addDomain(OrgDomain domain){
		domains.add(domain);
	}
	public List<OrgDomain> getDomains(){
		return domains;
	}
	public String[] getDomainsByArray(){
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public boolean isCoreMail() {
		return isCoreMail;
	}

	public void setCoreMail(boolean isCoreMail) {
		this.isCoreMail = isCoreMail;
	}

	public boolean isCas() {
		return isCas;
	}

	public void setCas(boolean isCas) {
		this.isCas = isCas;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
