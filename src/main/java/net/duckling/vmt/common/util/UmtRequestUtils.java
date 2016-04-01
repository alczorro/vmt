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
package net.duckling.vmt.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.domain.KEY;

import org.apache.log4j.Logger;

/**
 * 有关Umt-oauth的工具类
 * @author lvly
 * @since 2013-4-15
 */
public final class UmtRequestUtils {
	private UmtRequestUtils(){}
	private static final Logger LOGGER=Logger.getLogger(UmtRequestUtils.class);
	/**
	 * 获得登出url
	 * @param config 配置，vmt.properties的java实体类
	 * @param request 请求，用于获取回调地址用 
	 * @return 拼好的url，内涵回调地址
	 */
	public static String getLogoutUrl(VmtConfig config,HttpServletRequest request){
		StringBuffer sb=new StringBuffer();
		try {
			sb.append(config.getUmtLogoutUrl())
			  .append("?").append("WebServerURL=")
			  .append(URLEncoder.encode(UrlUtils.getBaseURL(request),KEY.GLOBAL_ENCODE));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return sb.toString();
	}
	/**
	 * 获得oauth的请求url
	 * @param config 
	 * @return oauth requestToken Url
	 * */
	public static String getOauthLoginUrl(VmtConfig config){
		StringBuffer sb=new StringBuffer();
		try {
			sb.append(config.getOauthUmtAuthorizeURL())
			  .append("?").append("response_type=code")
			  .append("&redirect_uri=").append(URLEncoder.encode(CommonUtils.trim(config.getOauthUmtRedirectURL()),"UTF-8"))
			  .append("&client_id=").append(CommonUtils.trim(config.getOauthUmtClientId()))
			  .append("&theme=").append(CommonUtils.trim(config.getOauthUmtTheme()));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return sb.toString();
	}
}
