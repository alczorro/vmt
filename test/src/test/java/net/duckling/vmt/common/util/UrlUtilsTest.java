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

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author lvly
 * @since 2013-6-21
 */
public class UrlUtilsTest {
	@Test
	public void test_addParam(){
		Assert.assertEquals("http://baidu.com?a=1",UrlUtils.addParam("http://baidu.com", "a", "1"));
		Assert.assertEquals("http://baidu.com?a=1&b=2",UrlUtils.addParam("http://baidu.com?a=1", "b", "2"));
	}
	@Test
	public void test_getAddress(){
		MockHttpServletRequest request=new MockHttpServletRequest();
		request.setServerName("www.baidu.com");
		request.setServerPort(8080);
		request.setScheme("https");
		Assert.assertEquals("www.baidu.com:8080",UrlUtils.getAddress(request));
		request.setServerPort(80);
		Assert.assertEquals("www.baidu.com",UrlUtils.getAddress(request));
	}
	@Test
	public void test_getBaseURL(){
		MockHttpServletRequest request=new MockHttpServletRequest();
		request.setServerName("www.baidu.com");
		request.setServerPort(8080);
		request.setScheme("https");
		request.setContextPath("/vmt");
		Assert.assertEquals("https://www.baidu.com:8080/vmt",UrlUtils.getBaseURL(request));
		request.setServerPort(80);
		request.setContextPath("/");
		Assert.assertEquals("https://www.baidu.com/",UrlUtils.getBaseURL(request));
	}
	
	@Test
	public void test_getDomain(){
		MockHttpServletRequest request=new MockHttpServletRequest();
		request.setServerName("www.baidu.com");
		request.setServerPort(8080);
		request.setScheme("https");
		request.setContextPath("/vmt");
		Assert.assertEquals("https://www.baidu.com:8080",UrlUtils.getDomain(request));
		request.setServerPort(80);
		request.setContextPath("/");
		Assert.assertEquals("https://www.baidu.com",UrlUtils.getDomain(request));
	}
	
	@Test
	public void test_getFullRequestUrl(){
		MockHttpServletRequest request=new MockHttpServletRequest();
		request.setParameter("a","1");
		request.setParameter("b","2");
		request.setServerName("www.baidu.com");
		request.setServerPort(8080);
		request.setScheme("https");
		request.setRequestURI("/vmt/haha");
		Assert.assertEquals("https://www.baidu.com:8080/vmt/haha?a=1&b=2",UrlUtils.getFullRequestUrl(request));
	}
	
	@Test
	public void test_updateParams(){
		Assert.assertEquals("https://www.baidu.com:8080/vmt/haha?a=1&b=3",UrlUtils.updateParams("https://www.baidu.com:8080/vmt/haha?a=1&b=2", "b", "3"));
	}
}
