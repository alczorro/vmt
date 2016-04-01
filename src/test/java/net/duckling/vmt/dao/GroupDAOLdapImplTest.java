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
package net.duckling.vmt.dao;

import java.util.List;

import junit.framework.Assert;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.test.BuildData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lvly
 * @since 2013-6-9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class GroupDAOLdapImplTest {
	@Autowired
	private IGroupDAO groupDAO;
	@Autowired
	private LdapTemplate tmpl;
	private String dn;
	@Before
	public void before(){
		dn=BuildData.resetGroup();
	}
	@After
	public void after(){
		BuildData.remove(dn);
	}
	@Test
	public void test_getAdminGroups(){
		//这个人是管理员
		List<LdapGroup> groups=groupDAO.getAdminGroups("10000080");
		Assert.assertEquals(1, groups.size());
		//没有这个人是管理员的群组
		groups=groupDAO.getAdminGroups("unkown");
		Assert.assertEquals(0, groups.size());
	}
	@Test
	public void test_getAllGroups(){
		List<LdapGroup> groups=groupDAO.getAllGroups();
		Assert.assertEquals(1, groups.size());
		LdapGroup group=groups.get(0);
		Assert.assertEquals(group.getName(),"JUNIT测试用群组勿动");
		Assert.assertEquals(group.getDn(),dn);
	}
	@Test
	public void test_getGroupByDN(){
		LdapGroup group=groupDAO.getGroupByDN(dn);
		Assert.assertEquals(group.getName(), "JUNIT测试用群组勿动");
		Assert.assertEquals(group.getPassword(), "passw0rd");
		Assert.assertEquals(group.getAdmins().length, 2);
		Assert.assertEquals(group.getDn(),dn);
	}
	
	@Test
	public void test_getGroupBySymbol(){
		String symbol=LdapUtils.getLastValue(dn);
		LdapGroup group=groupDAO.getGroupBySymbol(symbol, "");
		Assert.assertEquals(group.getName(), "JUNIT测试用群组勿动");
		Assert.assertEquals(group.getPassword(), "passw0rd");
		Assert.assertEquals(group.getAdmins().length, 2);
		Assert.assertEquals(group.getDn(),dn);
	}
	@Test
	public void test_getMyGroups(){
		List<LdapGroup> groups=groupDAO.getMyGroups("10000083");
		Assert.assertEquals(1, groups.size());
		LdapGroup group=groups.get(0);
		Assert.assertEquals(group.getName(), "JUNIT测试用群组勿动");
		Assert.assertEquals(group.getPassword(), "passw0rd");
		Assert.assertEquals(group.getAdmins().length, 2);
		Assert.assertEquals(group.getDn(),dn);
		groups=groupDAO.getMyGroups("unkown");
		Assert.assertEquals(0, groups.size());
	}
	
	@Test
	public void test_getThirdPartyGroupByUmtId(){
		List<LdapGroup> groups=groupDAO.getThirdPartyGroupByUmtId("10000079","ddl");
		Assert.assertEquals(1, groups.size());
		groups=groupDAO.getThirdPartyGroupByUmtId("10000079","ddfffffl");
		Assert.assertEquals(0, groups.size());
		groups=groupDAO.getThirdPartyGroupByUmtId("1000007229","ddl");
		Assert.assertEquals(0, groups.size());
	}
	
	@Test(expected=NameNotFoundException.class)
	public void test_insert(){
		LdapGroup group=new LdapGroup();
		group.setName("尼玛");
		group.setSymbol("nima");
		group.setCreator("system");
		group.setAdmins(new String[]{"system"});
		group.setPassword("s;lkdf");
		groupDAO.insert(group);
		Assert.assertNotNull(groupDAO.getGroupBySymbol("nima",""));
		tmpl.unbind(group.getDn(), true);
		groupDAO.getGroupByDN(group.getDn());
	}
	@Test
	public void test_isNameUsed(){
		Assert.assertTrue(groupDAO.isNameUsed("JUNIT测试用群组勿动"));
		Assert.assertFalse(groupDAO.isNameUsed("JUNIT"));
		String symbol=LdapUtils.getLastValue(dn);
		Assert.assertTrue(groupDAO.isSymbolUsed(symbol));
		Assert.assertFalse(groupDAO.isSymbolUsed("lkjsdflk"));
	}
}
