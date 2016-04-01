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
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.view.ListView;
import net.duckling.vmt.test.BuildData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lvly
 * @since 2013-6-18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class SearchDAOLdapImplTest {
	@Autowired
	private ISearchDAO searchDAO;
	
	private String orgDN;
	@Before
	public void before(){
		orgDN=BuildData.resetOrg();
	}
	@After
	public void after(){
		BuildData.remove(orgDN);
	}
	
	@Test
	public void test_searchActiveUserCount(){
		Assert.assertEquals(4, searchDAO.searchActiveUserCount(orgDN));
	}
	
	@Test
	public void test_searchByList(){
		List<ListView> list=searchDAO.searchByList(orgDN, true,false);
		Assert.assertEquals(2,list.size());
		Assert.assertEquals("folder", list.get(0).getType());
		Assert.assertEquals("部门1", list.get(0).getName());
		Assert.assertEquals("folder", list.get(1).getType());
		Assert.assertEquals("部门2", list.get(1).getName());
		
		list=searchDAO.searchByList(list.get(1).getDn(), true,false);
		Assert.assertEquals(2,list.size());
		Assert.assertEquals("link", list.get(0).getType());
		Assert.assertEquals("徐志坚君", list.get(0).getName());
		Assert.assertEquals("folder", list.get(1).getType());
		Assert.assertEquals("部门21", list.get(1).getName());
		
		list=searchDAO.searchByList(list.get(1).getDn(), false,false);
		Assert.assertEquals(1,list.size());
		Assert.assertEquals("link", list.get(0).getType());
		Assert.assertEquals("某人的扣扣", list.get(0).getName());
	}
	@Test
	public void test_searchByListLocalData(){
		List<?> list=searchDAO.searchByListLocalData(orgDN);
		LdapDepartment dept1=((LdapDepartment)list.get(0));
		LdapDepartment dept2=((LdapDepartment)list.get(1));
		Assert.assertEquals(2,list.size());
		Assert.assertEquals("部门1", dept1.getName());
		Assert.assertEquals("部门2", dept2.getName());
		
		list=searchDAO.searchByListLocalData(dept2.getDn());
		LdapUser user21=(LdapUser)list.get(0);
		LdapDepartment dept21=((LdapDepartment)list.get(1));
		Assert.assertEquals(2,list.size());
		Assert.assertEquals("徐志坚君", user21.getName());
		Assert.assertEquals("10000082", user21.getUmtId());
		Assert.assertEquals("部门21", dept21.getName());
		Assert.assertEquals("bumen21", dept21.getSymbol());
		
	}
	
	@Test
	public void test_searchDepartByList(){
		List<ListView> list=searchDAO.searchDepartByList(orgDN);
		Assert.assertEquals(2,list.size());
		Assert.assertEquals("folder", list.get(0).getType());
		Assert.assertEquals("部门1", list.get(0).getName());
		Assert.assertEquals("folder", list.get(1).getType());
		Assert.assertEquals("部门2", list.get(1).getName());
		
		list=searchDAO.searchDepartByList(list.get(1).getDn());
		Assert.assertEquals(1,list.size());
		Assert.assertEquals("folder", list.get(0).getType());
		Assert.assertEquals("部门21", list.get(0).getName());
	}
}
