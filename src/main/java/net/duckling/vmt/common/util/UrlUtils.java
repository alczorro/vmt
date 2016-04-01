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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.duckling.cloudy.common.CommonUtils;

/**
 * URL操作的工具类
 * @author lvly
 * @since 2013-4-15
 */
public final class UrlUtils {
	private static final int HTTPS_DEFAULT_PORT = 443;
	private static final int HTTP_DEFAULT_PORT = 80;
	private UrlUtils(){}
	/**
	 * 构造根URL，例如：http://localhost:8080/dhome
	 * @param request
	 * @return
	 */
	public static String getBaseURL(HttpServletRequest request) {
		return getDomain(request) + request.getContextPath();
	}
	/***
	 * 只获取网址，协议，之类的不要
	 * @param  if the url is http://www.lvlongyun.com/people/index.html
	 * @return www.lvlongyun.com
	 * 
	 * */
	public static String getAddress(HttpServletRequest request){
		return getBaseURL(request).replace(request.getScheme(), "").replace("://", "").replace(request.getContextPath(), "");
	}

	/**
	 * 构造Domain，例如：http://localhost:8080
	 * @param request
	 * @return
	 */
	public static String getDomain(HttpServletRequest request) {
		String url = request.getScheme() + "://" + request.getServerName();
		int port = request.getServerPort();
		if ((port != HTTP_DEFAULT_PORT) && (port != HTTPS_DEFAULT_PORT)) {
			url = url + ":" + port;
		}
		return url;
	}
	/**
	 * 给url添加参数，如果已经有别的参数就加&，如果没有就加？
	 * @param url 目标url
	 * @param key 参数key
	 * @param value 参数值
	 * */
	public static String addParam(String url,String key,String value){
		if(CommonUtils.isNull(url)||CommonUtils.isNull(key)||CommonUtils.isNull(value)){
			return url;
		}else if(url.contains(key+"=")){
			return url;
		}else{
			if(url.contains("?")){
				return url+"&"+key+"="+value;
			}else{
				return url+"?"+key+"="+value;
			}
		}
	}
	/**
	 * 获得请求的url，完整,包括post请求的所有东西都会构建成url
	 * @param request http请求
	 * @return 用于请求的url，浏览器打的什么，这里就应该体现神马
	 * */
	public static String getFullRequestUrl(HttpServletRequest request){
		String url=request.getRequestURL().toString();
		for(Enumeration<String> paramNames=request.getParameterNames();paramNames.hasMoreElements();){
			String pName=paramNames.nextElement();
			url=addParam(url, pName, request.getParameter(pName));
		}
		return url;
	}
	/**
	 * 
	 * 更新url里面的值，get方法的时候尤为好用,
	 * @param url 比如url是 http://test.com?a=1&b=2&c=3
	 * @param key  如果key=a
	 * @param value 且value=3
	 * @return 则返回http://test.com?a=3&b=2&c=3
	 */
	public static String updateParams(String url,String key,String value){
		if(!url.contains("?")&&!url.contains(key)){
			return url;
		}
		String[] urlSplit=url.split("\\?");
		if(urlSplit.length<2){
			return url;
		}
		String[] keyValue=urlSplit[1].split("&");
		for(int i=0;i<keyValue.length;i++){
			if(keyValue[i].startsWith(key)){
				keyValue[i]=key+"="+value;
			}
		}
		StringBuilder returnUrl=new StringBuilder().append(urlSplit[0]).append("?");
		for(String param:keyValue){
			returnUrl.append(param).append("&");
		}
		return returnUrl.substring(0,returnUrl.length()-1);
	}
}
