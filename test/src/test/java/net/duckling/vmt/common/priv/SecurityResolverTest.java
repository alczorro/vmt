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
package net.duckling.vmt.common.priv;

import junit.framework.Assert;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.test.BuildData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.vlabs.umt.oauth.UserInfo;

/**
 * @author lvly
 * @since 2013-6-6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class SecurityResolverTest {
	private MockHttpServletRequest request;
	private UserInfo userInfo;
	private VmtSessionUser user;
	private String dn;
	
	@Before
	public void before(){
		dn=BuildData.resetGroup();
		System.out.println(dn);
		reset();
	}
	public void reset(){
		request=new MockHttpServletRequest();
		request.setParameter("dn", dn);
		user=new VmtSessionUser();
		user.setCoreMailDomain("cstnet.cn");
		userInfo=new UserInfo();
		userInfo.setUmtId("10000079");
		user.setUserInfo(userInfo);
		request.getSession(true).setAttribute(KEY.SESSION_LOGIN_USER, user);
	}
	@After
	public void after(){
		System.out.println("delete:"+dn);
		BuildData.remove(dn);
	}
	public SecurityResolver getResolver(String methodName)throws Exception{
		SecurityMapping mapping=SecurityResolverTest.class.getMethod(methodName,null).getAnnotation(SecurityMapping.class);
		return new SecurityResolver(request,mapping);
	}
	/**
	 * dn不存在时
	 * */
	@Test
	public void test1()throws Exception{
		//给出的dn存在
		Assert.assertEquals(true, getResolver("testViewMethod").canAccess());
		request.setParameter("dn", "woca=1,nima=2");
		//给出的dn不存在
		Assert.assertEquals(false, getResolver("testViewMethod").canAccess());
		
		
	}
	/**
	 * 用户不在这个组里面
	 * */
	@Test
	public void test2()throws Exception{
		//这个umtId在库里不存在
		userInfo.setUmtId("10002");
		Assert.assertFalse( getResolver("testViewMethod").canAccess());
	}
	/***
	 * 测试需要管理员权限
	 * */
	@Test
	public void test3() throws Exception{
		//是管理员
		userInfo.setUmtId("10000079");
		Assert.assertTrue(getResolver("testAdminMethod").canAccess());
		//不是管理员的成员
		userInfo.setUmtId("10000083");
		Assert.assertFalse(getResolver("testAdminMethod").canAccess());
	}
	
	/**
	 * 需要超级管理员权限
	 * */
	@Test
	public void testIsSuperAdmin()throws Exception{
		//不是超级管理员
		Assert.assertFalse(getResolver("testSuperAdmin").canAccess());
		user.setIsSuperAdmin(true);
		//是超级管理员
		Assert.assertTrue(getResolver("testSuperAdmin").canAccess());
	}
	/**
	 * 验证是管理员或者自己权限
	 * */
	@Test
	public void testSelfOrAdmin()throws Exception{
		//是自己，也是管理员
		Assert.assertTrue(getResolver("testIsSelfOrAdmin").canAccess());
		//是自己但不是管理员
		reset();
		request.setParameter("dn", "vmt-umtId=10000081,"+dn);
		userInfo.setUmtId("10000081");
		Assert.assertTrue(getResolver("testIsSelfOrAdmin").canAccess());
		//是别人，但是是管理员
		reset();
		request.setParameter("dn", "vmt-umtId=10000080,"+dn);
		Assert.assertTrue(getResolver("testIsSelfOrAdmin").canAccess());
		//是别人，也不是管理员
		reset();
		request.setParameter("dn", "vmt-umtId=10000083,"+dn);
		userInfo.setUmtId("10000082");
		Assert.assertFalse(getResolver("testIsSelfOrAdmin").canAccess());
	}
	
	@SecurityMapping(level=SecurityLevel.VIEW)
	public void testViewMethod(){
		
	}
	@SecurityMapping(level=SecurityLevel.ADMIN)
	public void testAdminMethod(){
		
	}
	@SecurityMapping(level=SecurityLevel.SUPER_ADMIN)
	public void testSuperAdmin(){
		
	}
	@SecurityMapping(level=SecurityLevel.SELF_OR_ADMIN)
	public void testIsSelfOrAdmin(){
		
	}
}
