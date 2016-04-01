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
package net.duckling.vmt.dao.impl;

import java.util.List;

import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IIPFilterDAO;
import net.duckling.vmt.domain.IPFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2013-8-5
 */
@Component
public class IPFilterDAOImpl implements IIPFilterDAO {
	@Autowired
	private BaseDAO<IPFilter> baseDAO;
	@Override
	public boolean canAccess(String ip) {
		IPFilter fi=new IPFilter();
		fi.setIp(ip);
		return baseDAO.selectOne(fi)!=null;
	}
	@Override
	public List<IPFilter> asList() {
		return baseDAO.select(new IPFilter());
	}
	@Override
	public void removeIp(int id) {
		IPFilter fi=new IPFilter();
		fi.setId(id);
		baseDAO.delete(fi);
	}
	@Override
	public int addIpFilter(String ip) {
		IPFilter fi=new IPFilter();
		fi.setIp(ip);
		return baseDAO.insert(fi);
		
	}
}
