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

import java.util.List;
import java.util.Map;

import net.duckling.cloudy.db.simpleORM.parse.ORMParser;
import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IVmtAppProfileDAO;
import net.duckling.vmt.domain.VmtAppProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VmtAppProfileJDBCDAOImpl implements IVmtAppProfileDAO{
	
	@Autowired
	private BaseDAO<VmtAppProfile> proDAO; 
	@Override
	public int addProfile(VmtAppProfile profile) {
		return proDAO.insert(profile);
	}
	@Override
	public void changeProfile(VmtAppProfile profile) {
		String update="update vmt_app_profile set `value`=:value where fkId=:fkId and appType=:appType";
		ORMParser<VmtAppProfile> parser=proDAO.getORMParser(VmtAppProfile.class);
		Map<String,Object> params=parser.getValueMap(profile);
		proDAO.getTmpl().update(update, params);
	}
	@Override
	public boolean isExsits(String umtId,String fkId,String appType) {
		VmtAppProfile profile =new VmtAppProfile();
		profile.setUmtId(umtId);
		profile.setFkId(fkId);
		profile.setAppType(appType);
		return proDAO.getCount(profile)>0;
	}
	@Override
	public boolean getStatus(VmtAppProfile pro) {
		VmtAppProfile result=proDAO.selectOne(pro);
		if(result==null){
			return true;
		}else{
			return VmtAppProfile.VALUE_SHOW.equals(result.getValue());
		}
	}
	@Override
	public List<VmtAppProfile> getProfiles(String umtId) {
		VmtAppProfile pro=new VmtAppProfile();
		pro.setUmtId(umtId);
		return proDAO.select(pro);
	}
	
}
