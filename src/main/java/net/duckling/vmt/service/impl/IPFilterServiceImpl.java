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
/**
 * 
 */
package net.duckling.vmt.service.impl;

import java.util.List;

import net.duckling.vmt.dao.IIPFilterDAO;
import net.duckling.vmt.domain.IPFilter;
import net.duckling.vmt.service.IIPFilterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-8-5
 */
@Service
public class IPFilterServiceImpl implements IIPFilterService{
	@Autowired
	private IIPFilterDAO ipFilterDAO;
	@Override
	public boolean canAccess(String ip) {
		return ipFilterDAO.canAccess(ip);
	}
	
	@Override
	public List<IPFilter> asList() {
		return ipFilterDAO.asList();
	}
	@Override
	public void removeIp(int id) {
		ipFilterDAO.removeIp(id);
	}
	@Override
	public int addIpFilter(String ip) {
		return ipFilterDAO.addIpFilter(ip);
		
	}
}
