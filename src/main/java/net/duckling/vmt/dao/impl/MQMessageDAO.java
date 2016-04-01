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

import java.util.Date;
import java.util.List;

import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IMQMessageDAO;
import net.duckling.vmt.domain.MQMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2013-8-2
 */
@Component
public class MQMessageDAO implements IMQMessageDAO {
	
	@Autowired
	private BaseDAO<MQMessage> baseDAO;
	
	@Override
	public int insert(MQMessage message) {
		return baseDAO.insert(message);
	}

	@Override
	public List<MQMessage> queryMessage(Date fromDate, Date toDate) {
		return baseDAO.select(new MQMessage()," and publish_time>="+fromDate.getTime()+" and publish_time<="+toDate.getTime());
	}

}
