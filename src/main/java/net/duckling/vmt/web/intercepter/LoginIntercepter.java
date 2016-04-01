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
package net.duckling.vmt.web.intercepter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.priv.SecurityResolver;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.UmtRequestUtils;
import net.duckling.vmt.common.util.UrlUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.IVmtMessageService;
import net.duckling.vmt.web.helper.SessionHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * uri里面开头user的视为必须要登录的操作，拦截
 * @author lvly
 * @since 2013-5-10
 */
public class LoginIntercepter extends HandlerInterceptorAdapter{
	private static final Logger LOGGER=Logger.getLogger(LoginIntercepter.class);
	@Autowired
	private VmtConfig config;
	
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IVmtMessageService msgService;
	
	/**
	 * 拦截之前做的事情，检查用户登录状态，如果未登录，那么先把uri保存起来，登录成功以后跳转
	 *@param request
	 *@param response
	 *@param handler
	 *@return  是否允许访问
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if(SessionHelper.hasLogin(request)){
			HandlerMethod method=(HandlerMethod)handler;
			SecurityMapping mapping=getMapping(method);
			if(mapping==null){
				return true;
			}
			if(!new SecurityResolver(request, mapping).canAccess()){
				try {
					response.sendRedirect(request.getContextPath()+"/msg/"+LdapUtils.encode("您无权访问此页面"));
				} catch (IOException e) {
					LOGGER.error(e.getMessage(),e);
				}
				return false;
			}
			return true;
			
		}else{
			String requestUrl=UrlUtils.getFullRequestUrl(request);
			String dn=request.getParameter("dn");
			if(!CommonUtils.isNull(dn)){
				String encodeDN=LdapUtils.encode(dn);
				requestUrl=UrlUtils.updateParams(requestUrl, "dn",encodeDN);
			}
			
			try {
				response.sendRedirect(UmtRequestUtils.getOauthLoginUrl(config)+"&state="+URLEncoder.encode(requestUrl, KEY.GLOBAL_ENCODE));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
			}
			return false;
		}
	}
	private SecurityMapping getMapping(HandlerMethod method){
		SecurityMapping mapping = method.getMethod().getAnnotation(SecurityMapping.class);
		if(mapping==null){
			mapping=method.getBean().getClass().getAnnotation(SecurityMapping.class);
		}
		return mapping;
	}
}
