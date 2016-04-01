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
package net.duckling.vmt.dao;

import java.util.List;

import net.duckling.vmt.domain.index.OrgDetail;
import net.duckling.vmt.domain.index.OrgDomain;

/**
 * 域名关联关系，一个组织多个域名
 * @author lvlongyun
 * @since 2014-2-10
 * 
 * */
public interface IOrgDomainMappingDAO {
	/**
	 * 新建一个org，手动维护
	 * */
	int insertOrgDetail(OrgDetail detail);
	
	/**
	 * 新建一个org的域名信息，手动维护
	 * */
	void insertOrgDomain(OrgDomain domain);
	/**
	 * 
	 * */
	void deleteOrgDetail(int orgDetailId);
	
	void deleteOrgDomain(int domainId);
	
	void updateOrgDetail(OrgDetail orgDetail);
	
	void updateOrgDomain(OrgDomain orgDomain);
	
	OrgDetail findOrgDetailById(int orgDetailId);
	
	OrgDetail findOrgDetailByDomain(String domain);

	List<OrgDomain> findOrgDomainAll();
	
	List<OrgDetail> findOrgDetailAll();

	void updateOrgName(String orgSymbol, String orgName);

	boolean isExistSymbol(String orgSymbol);
	
	OrgDetail findOrgDetailBySymbol(String orgSymbol);

	boolean isExistDomain(String domain);

	void deleteOrgDomainByDomain(String domain);

	void updateIsCas(boolean isCas, String symbol);

	void updateIsCoreMail(boolean isCoreMail, String symbol);

	List<OrgDomain> findDomainsByOrgSymbol(String symbol);

	List<OrgDomain> findDomainsByOrgId(int id);

}
