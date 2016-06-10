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

import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.test.BuildData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lvly
 * @since 2013-6-7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class DepartmentDAOLdapImplTest{
	private String dn;
	@Autowired
	private IDepartmentDAO deptDAO;
	@Before 
	public void before(){
		dn=BuildData.resetOrg();
	}
	@After
	public void after(){
		BuildData.remove(dn);
	}
	@Test
	public void testGetDepart(){
		LdapDepartment dept=deptDAO.getDepartByDN("vmt-symbol=bumen1,"+dn);
		Assert.assertNotNull(dept);
		Assert.assertEquals("system", dept.getCreator());
		Assert.assertEquals("部门1",dept.getName());
		Assert.assertEquals("bumen1", dept.getSymbol());
	}
	@Test
	public void testGetDepartBySymbol(){
		LdapDepartment dept=deptDAO.getDepartBySymbol(dn,"bumen2");
		Assert.assertNotNull(dept);
		Assert.assertEquals("system", dept.getCreator());
		Assert.assertEquals("部门2",dept.getName());
		Assert.assertEquals("bumen2", dept.getSymbol());
	}
	@Test
	public void testGetDn(){
		String resultdn=deptDAO.getDn(dn, "部门2");
		Assert.assertEquals("vmt-symbol=bumen2,"+dn,resultdn);
	}
	
	@Test
	public void testInsert(){
		LdapDepartment dept1=new LdapDepartment();
		dept1.setCreator("system");
		dept1.setCurrentDisplay("JUNIT测试用组织勿动,部门12");
		dept1.setName("部门12");
		dept1.setSymbol("bumen12");
		deptDAO.insert("vmt-symbol=bumen1,"+dn, dept1);
		dept1=deptDAO.getDepartByDN(dept1.getDn());
		Assert.assertEquals("system", dept1.getCreator());
		Assert.assertEquals("部门12", dept1.getName());
		Assert.assertEquals("bumen12", dept1.getSymbol());
	}
	@Test
	public void testIsExists(){
		Assert.assertTrue(deptDAO.isExists(dn, "部门1"));
		Assert.assertFalse(deptDAO.isExists(dn, "部门xx"));
	}
	
}