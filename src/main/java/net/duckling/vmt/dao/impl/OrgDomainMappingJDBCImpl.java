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
package net.duckling.vmt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IOrgDomainMappingDAO;
import net.duckling.vmt.domain.index.OrgDetail;
import net.duckling.vmt.domain.index.OrgDomain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class OrgDomainMappingJDBCImpl implements IOrgDomainMappingDAO{
	@Autowired
	private BaseDAO<OrgDomain> domainDAO;
	@Autowired
	private BaseDAO<OrgDetail> detailDAO;
	@Override
	public int insertOrgDetail(OrgDetail detail) {
		return detailDAO.insert(detail);
	}

	@Override
	public void insertOrgDomain(OrgDomain domain) {
		domainDAO.insert(domain);
		
	}

	@Override
	public void deleteOrgDetail(int orgDetailId) {
		OrgDetail od=new OrgDetail();
		od.setId(orgDetailId);
		detailDAO.delete(od);
	}

	@Override
	public void deleteOrgDomain(int domainId) {
		OrgDomain od=new OrgDomain();
		od.setId(domainId);
		domainDAO.delete(od);
		
	}

	@Override
	public void updateOrgDetail(OrgDetail orgDetail) {
		detailDAO.update(orgDetail);
		
	}

	@Override
	public void updateOrgDomain(OrgDomain orgDomain) {
		domainDAO.update(orgDomain);
		
	}

	@Override
	public OrgDetail findOrgDetailById(int orgDetailId) {
		OrgDetail od=new OrgDetail();
		od.setId(orgDetailId);
		return detailDAO.selectOne(od);
	}

	@Override
	public OrgDetail findOrgDetailByDomain(String domain) {
		String sql="select o.* from vmt_org o,vmt_org_domain d where d.org_id=o.id and d.`org_domain`=:domain";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("domain", CommonUtils.trim(domain));
		
		return CommonUtils.first(detailDAO.getTmpl().query(sql, params, detailDAO.getORMParser(OrgDetail.class).getRowMapper()));
		
	}

	@Override
	public List<OrgDomain> findOrgDomainAll() {
		return domainDAO.select(new OrgDomain());
	}

	@Override
	public List<OrgDetail> findOrgDetailAll() {
		return detailDAO.select(new OrgDetail());
	}
	@Override
	public void updateOrgName(String orgSymbol, String orgName) {
		String sql="update vmt_org set org_name=:orgName where org_symbol=:orgSymbol";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orgName",orgName);
		params.put("orgSymbol", orgSymbol);
		detailDAO.getTmpl().update(sql, params);
	}
	@Override
	public boolean isExistSymbol(String orgSymbol) {
		OrgDetail detail=new OrgDetail();
		detail.setSymbol(orgSymbol);
		return detailDAO.getCount(detail)>0;
	}
	@Override
	public OrgDetail findOrgDetailBySymbol(String orgSymbol) {
		OrgDetail orgDetail=new OrgDetail();
		orgDetail.setSymbol(orgSymbol);
		return detailDAO.selectOne(orgDetail);
	}
	@Override
	public boolean isExistDomain(String domain) {
		OrgDomain d=new OrgDomain();
		d.setDomain(domain);
		
		return domainDAO.getCount(d)>0;
	}
	@Override
	public void deleteOrgDomainByDomain(String domain) {
		OrgDomain d=new OrgDomain();
		d.setDomain(domain);
		domainDAO.delete(d);
		
	}

	@Override
	public void updateIsCas(boolean isCas, String symbol) {
		String sql="update vmt_org set is_cas="+(isCas?1:0)+" where org_symbol=:symbol";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("symbol", symbol);
		domainDAO.getTmpl().update(sql, params);
	}

	@Override
	public void updateIsCoreMail(boolean isCoreMail, String symbol) {
		String sql="update vmt_org set is_coremail="+(isCoreMail?1:0)+" where org_symbol=:symbol";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("symbol", symbol);
		domainDAO.getTmpl().update(sql, params);
	}
	@Override
	public List<OrgDomain> findDomainsByOrgSymbol(String symbol) {
		String sql="select d.* from vmt_org o,vmt_org_domain d where d.org_id=o.id and o.`org_symbol`=:orgSymbol";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orgSymbol",symbol);
		return detailDAO.getTmpl().query(sql, params, domainDAO.getORMParser(OrgDomain.class).getRowMapper());
	}
	@Override
	public List<OrgDomain> findDomainsByOrgId(int id) {
		OrgDomain od=new OrgDomain();
		od.setOrgId(id);
		return domainDAO.select(od);
	}
}
