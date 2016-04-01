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
package net.duckling.vmt.service.rest;

import java.util.Date;
import java.util.List;

import net.duckling.vmt.api.IRestMessageService;
import net.duckling.vmt.api.domain.message.MQBaseMessage;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.service.IMQMessageService;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.server.annotation.RestMethod;

/**
 * @author lvly
 * @since 2013-8-7
 */
public class RestMessageServiceImpl implements IRestMessageService{
	private IMQMessageService mqservice;
	
	public RestMessageServiceImpl(){
		this.mqservice=BeanFactory.getBean(IMQMessageService.class);
		
	}
	@Override
	@RestMethod("getMessageByDate")
	public List<MQBaseMessage> getMessageByDate(Date fromDate, Date toDate) throws ServiceException {
		return null;
	}
	@Override
	@RestMethod("getMessageById")
	public MQBaseMessage getMessageById(int id) throws ServiceException {
		return null;
	}

}
