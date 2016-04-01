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
import net.duckling.vmt.common.config.VmtConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lvly
 * @since 2013-6-21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class UmtRequestUtilsTest {
	@Autowired
	private VmtConfig config;
	private MockHttpServletRequest request=new MockHttpServletRequest();
	@Test
	public void test_getLogoutUrl(){
		request.setScheme("http");
		request.setServerName("www.baidu.com");
		request.setServerPort(80);
		Assert.assertEquals("http://passporttest.escience.cn/logout?WebServerURL=http%3A%2F%2Fwww.baidu.com", UmtRequestUtils.getLogoutUrl(config, request));
	}
	@Test
	public void test_getOauthUrl(){
		Assert.assertEquals("http://passporttest.escience.cn/oauth2/authorize?response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fvmt7%2Foauth%2Fcallback&client_id=123453&theme=full",UmtRequestUtils.getOauthLoginUrl(config));
	}
}
