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

import java.util.List;

import net.duckling.vmt.dao.IVmtAppProfileDAO;
import net.duckling.vmt.domain.VmtAppProfile;
import net.duckling.vmt.service.IVmtAppProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class VmtAppProfileService implements IVmtAppProfileService{
	@Autowired
	private IVmtAppProfileDAO proDAO;
	@Override
	public boolean getStatus(String umtId, String fkId, String appType) {
		VmtAppProfile pro=new VmtAppProfile();
		pro.setAppType(appType);
		pro.setFkId(fkId);
		pro.setUmtId(umtId);
		return proDAO.getStatus(pro);
	}
	@Override
	public void update(VmtAppProfile pro) {
		if(proDAO.isExsits(pro.getUmtId(),pro.getFkId(),pro.getAppType())){
			proDAO.changeProfile(pro);
		}else{
			proDAO.addProfile(pro);
		}
	}
	@Override
	public List<VmtAppProfile> getProfiles(String umtId) {
		return proDAO.getProfiles(umtId);
	}
	

}
