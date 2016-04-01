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

import java.util.List;

import net.duckling.vmt.domain.IPFilter;

/**
 * @author lvly
 * @since 2013-8-5
 */
public interface IIPFilterService {
	/**
	 * ip是否在过滤队列里面，是否可以访问
	 * @param ip
	 * @return
	 */
	boolean canAccess(String ip);
	
	/**
	 * 获取所有，允许ip列表
	 * @return
	 */
	List<IPFilter> asList();

	/**
	 * 删掉过滤ip
	 * @param id
	 */
	void removeIp(int id);

	/**
	 * 增加ip拦截
	 * @param ip
	 */
	int addIpFilter(String ip);

}
