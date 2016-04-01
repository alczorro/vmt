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

import net.duckling.vmt.dao.IVmtAppSwitchDAO;
import net.duckling.vmt.service.IVmtAppSwitchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class VmtAppSwitchServiceImpl implements IVmtAppSwitchService {
	@Autowired
	private IVmtAppSwitchDAO switchDAO;
	@Override
	public void ifNotExitsThenInsert(String teamDn) {
		if(!switchDAO.isExists(teamDn)){
			switchDAO.insert(teamDn);
		}
	}
	@Override
	public void insert(String teamDn) {
		switchDAO.insert(teamDn);
	}
	@Override
	public void delete(String decodeDN) {
		switchDAO.delete(decodeDN);
	}
	@Override
	public void deleteAll() {
		switchDAO.deleteAll();
	}
	@Override
	public boolean isExits(String dn) {
		return switchDAO.isExists(dn);
	}

}
