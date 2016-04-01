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
package net.duckling.vmt.service;

import java.util.List;

import net.duckling.vmt.domain.index.OrgDetail;
import net.duckling.vmt.domain.index.OrgDomain;

public interface IOrgDomainMappingService {
	OrgDetail getOrgByDomain(String domain);
	
	List<OrgDetail> findAllOrgDetails();
	
	void deleteADomain(int domainId);
	
	void deleteADetail(int orgId);
	
	void updateOrgName(String orgSymbol,String orgName);
	
	OrgDetail findOrgById(int orgId);

	boolean isDomainExists(String domain);

	void addDomain(String domain, String symbol);
	void addDetail(OrgDetail orgDetail);

	void deleteADomainByDomain(String trim);

	boolean isSymbolExists(String trim);

	void addDomain(OrgDomain odo);

	void updateIsCas(boolean isCas, String last);

	void updateIsCoreMail(boolean isCoreMail, String last);

	List<OrgDomain> findDomainsByOrgSymbol(String symbol);

}
