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
package net.duckling.vmt.api.impl;

import java.util.Date;
import java.util.List;

import net.duckling.vmt.api.IRestMessageService;
import net.duckling.vmt.api.domain.message.MQBaseMessage;
import cn.vlabs.duckling.api.umt.rmi.exception.LoginFailed;
import cn.vlabs.rest.ServiceException;

/**
 * 用于重放消息队列，比如获取消息出错，需要重新获得
 * @author lvly
 * @since 2013-8-7
 */
@SuppressWarnings("unchecked")
public class MessageService extends BaseService implements IRestMessageService{
	/**
	 * Constructor
	 * @param baseUrl umt的serviceurl，如http://vmt.escience.cn/services
	 * @throws LoginFailed 与vmt发生通讯错误
	 * */
	public MessageService(String baseUrl)  {
		super(baseUrl);
	}
	@Override
	public MQBaseMessage getMessageById(int id) throws ServiceException {
		return (MQBaseMessage)sendService("Message.getMessageById", new Object[]{id});
	}

	@Override
	public List<MQBaseMessage> getMessageByDate(Date fromDate, Date toDate) throws ServiceException {
		return (List<MQBaseMessage>)sendService("Message.getMessageByDate", new Object[]{fromDate,toDate});
	}
	
}
