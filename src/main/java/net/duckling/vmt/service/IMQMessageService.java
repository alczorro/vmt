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
package net.duckling.vmt.service;

import java.util.Date;
import java.util.List;

import net.duckling.vmt.domain.MQMessage;

/**
 * @author lvly
 * @since 2013-8-2
 */
public interface IMQMessageService {
	/**
	 * 
	 * @param message
	 */
	int insert(MQMessage message);
	/**
	 * 可能需要重放，查询消息
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	List<MQMessage> queryMessage(Date fromDate,Date toDate);
}
