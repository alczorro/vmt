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
package net.duckling.vmt.api;

import java.util.Date;
import java.util.List;

import net.duckling.vmt.api.domain.message.MQBaseMessage;
import cn.vlabs.rest.ServiceException;

/**
 * 如果出错，请调用此方法，用于重放
 * @author lvly
 * @since 2013-8-7
 */
public interface IRestMessageService {
	/**
	 * 根据id重放消息
	 * @param id 当初获得消息的时候
	 * @return 命中的消息
	 * @throws ServiceException 
	 */
	MQBaseMessage getMessageById(int id) throws ServiceException;
	
	/**
	 * 根据时间段重返消息队列
	 * @param fromDate 起始时间
	 * @param toDate 结束时间
	 * @return 命中的消息队列
	 * @throws ServiceException 
	 * */
	List<MQBaseMessage> getMessageByDate(Date fromDate,Date toDate) throws ServiceException;
}
