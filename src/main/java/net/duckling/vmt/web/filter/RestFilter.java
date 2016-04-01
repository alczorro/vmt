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
package net.duckling.vmt.web.filter;

import java.lang.reflect.Method;

import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.service.IIPFilterService;

import org.apache.log4j.Logger;

import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.protocal.Envelope;
import cn.vlabs.rest.server.Predefined;
import cn.vlabs.rest.server.filter.Filter;
import cn.vlabs.rest.server.filter.RequestContext;
import cn.vlabs.rest.server.filter.RestContext;

/**
 * @author lvly
 * @since 2013-8-5
 */
public class RestFilter implements Filter{
	private IIPFilterService ipFilter;
	private static final Logger LOGGER=Logger.getLogger(RestFilter.class);
	
	@Override
	public void destroy() {
		
	}

	@Override
	public Envelope doFilter(Method method, Object[] args, RequestContext context,RestSession session) {
		if (!ipFilter.canAccess(context.getRemoteAddr())){
			LOGGER.warn("it is dangerous:"+context.getRemoteAddr());
			return Predefined.AUTHORIZE_FAILED;
		}
		return null;
	}

	@Override
	public void init(RestContext arg0) {
		ipFilter=BeanFactory.getBean(IIPFilterService.class);
	}

}
