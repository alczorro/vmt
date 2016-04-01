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
package net.duckling.vmt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.dao.IOrgDomainMappingDAO;
import net.duckling.vmt.domain.index.OrgDetail;
import net.duckling.vmt.domain.index.OrgDomain;
import net.duckling.vmt.service.IOrgDomainMappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgDomainMappingServiceImpl implements IOrgDomainMappingService{
	@Autowired
	private IOrgDomainMappingDAO orgDomainDAO;
	@Override
	public OrgDetail getOrgByDomain(String domain) {
		
		OrgDetail od=orgDomainDAO.findOrgDetailByDomain(domain);
		if(od==null){
			return null;
		}
		List<OrgDetail> details=new ArrayList<OrgDetail>();
		details.add(od);
		List<OrgDomain> domains=orgDomainDAO.findDomainsByOrgId(od.getId());
		mergeDetailAndDomain(details, domains);
		return CommonUtils.first(details);
	}
	@Override
	public List<OrgDetail> findAllOrgDetails() {
		List<OrgDetail> details=orgDomainDAO.findOrgDetailAll();
		List<OrgDomain> domains=orgDomainDAO.findOrgDomainAll();
		mergeDetailAndDomain(details,domains);
		return details;
	}
	private void mergeDetailAndDomain(List<OrgDetail> details,List<OrgDomain> domains){
		if(CommonUtils.isNull(details)||CommonUtils.isNull(domains)){
			return;
		}
		Map<Integer,OrgDetail> detailsMap=new HashMap<Integer,OrgDetail>();
		for(OrgDetail detail:details){
			detailsMap.put(detail.getId(),detail);
		}
		for(OrgDomain domain:domains){
			OrgDetail detail=detailsMap.get(domain.getOrgId());
			if(detail==null){
				continue;
			}
			detail.addDomain(domain);
		}
	}

	@Override
	public void deleteADomain(int domainId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteADetail(int orgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOrgName(String orgSymbol, String orgName) {
		if(!orgDomainDAO.isExistSymbol(orgSymbol)){
			OrgDetail orgDetail=new OrgDetail();
			orgDetail.setName(orgName);
			orgDetail.setSymbol(orgSymbol);
			orgDomainDAO.insertOrgDetail(orgDetail);
		}else{
			orgDomainDAO.updateOrgName(orgSymbol,orgName);
		}
		
		
	}

	@Override
	public OrgDetail findOrgById(int orgId) {
		return null;
	}
	@Override
	public boolean isDomainExists(String domain) {
		return orgDomainDAO.isExistDomain(domain);
	}
	@Override
	public void addDomain(String domain, String symbol) {
		OrgDetail od=orgDomainDAO.findOrgDetailBySymbol(symbol);
		OrgDomain odo=new OrgDomain();
		odo.setDomain(domain);
		odo.setOrgId(od.getId());
		orgDomainDAO.insertOrgDomain(odo);
	}
	@Override
	public void deleteADomainByDomain(String domain) {
		orgDomainDAO.deleteOrgDomainByDomain(domain);
	}
	@Override
	public boolean isSymbolExists(String symbol) {
		return orgDomainDAO.isExistSymbol(symbol);
	}
	@Override
	public void addDetail(OrgDetail detail) {
		detail.setId(orgDomainDAO.insertOrgDetail(detail));
	}
	@Override
	public void addDomain(OrgDomain domain) {
		orgDomainDAO.insertOrgDomain(domain);
		
	}
	@Override
	public void updateIsCas(boolean isCas, String symbol) {
		orgDomainDAO.updateIsCas(isCas,symbol);
		
	}
	@Override
	public void updateIsCoreMail(boolean isCoreMail, String symbol) {
		orgDomainDAO.updateIsCoreMail(isCoreMail,symbol);
	}
	@Override
	public List<OrgDomain> findDomainsByOrgSymbol(String symbol) {
		return orgDomainDAO.findDomainsByOrgSymbol(symbol);
	}

}
