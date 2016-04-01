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

import cn.vlabs.rest.ServiceClient;
import cn.vlabs.rest.ServiceContext;
import cn.vlabs.rest.ServiceException;

/**基本服务，所有的服务都继承自此类，主要用于构造通讯对象
 * <br>其他应用请勿直接使用此类
 * @author lvly
 * @since 2013-6-3
 */
public class BaseService {
	private ServiceContext context;
	private ServiceClient client;
	/**
	 * Constructor
	 * @param baseUrl umt的serviceurl，如http://vmt.escience.cn/services
	 * */
	public BaseService(String baseUrl){
		context = new ServiceContext(baseUrl);
		context.setKeepAlive(true);
		client = new ServiceClient(context);
	}
	/**
	 * 发送请求
	 * @param service 服务接口名称 ，比如"Servie.someMethod"
	 * @param value 传参，一个的话，直接赋值即可，如果是多个，请用Object[]{}包装
	 * @return 返回值，需要自己强转
	 * @throws ServiceException 异常包装类，统一用此类
	 */
	public Object sendService(String service, Object value) throws ServiceException{
		Object returnValue= client.sendService(service, value);
		context.close();
		return returnValue;
	}
}
