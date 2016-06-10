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
package net.duckling.vmt.service;

import junit.framework.Assert;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.test.BuildData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class DepartmentServiceImplTest {
	@Autowired
	private IDepartmentService deptService;
	
	@Autowired
	private ICommonService commonService;
	
	private String dn;
	//每次做完一个test，需要恢复成原来的数据
	@After
	public void reset(){
		BuildData.remove(dn);
	}
	@Before
	public void before(){
		dn=BuildData.resetOrg();
	}
	@Test
	public void test_insert_get(){
		LdapDepartment dept=new LdapDepartment();
		dept.setName("部门3");
		dept.setSymbol("bumen3");
		dept.setCreator("nimei");
		deptService.create(dn, dept);
		LdapDepartment dept2=deptService.getDepartBySymbol(dn, "bumen3");
		Assert.assertEquals(dept.getName(), dept2.getName());
		Assert.assertEquals(dept.getSymbol(), dept2.getSymbol());
		Assert.assertEquals(dept.getCreator(), dept2.getCreator());
		Assert.assertEquals("JUNIT测试用组织勿动,部门3", dept.getCurrentDisplay());
	}
	
	@Test
	public void test_getNeedDeleteDepart(){
		Assert.assertFalse(commonService.isExist("vmt-symbol=need-remove,"+dn));
		String deleteDepart=deptService.getNeedDeleteDepart(dn);
		Assert.assertEquals("vmt-symbol=need-remove,"+dn, deleteDepart);
		Assert.assertTrue(commonService.isExist("vmt-symbol=need-remove,"+dn));
		deleteDepart=deptService.getNeedDeleteDepart(dn);
		Assert.assertEquals("vmt-symbol=need-remove,"+dn, deleteDepart);
		Assert.assertTrue(commonService.isExist("vmt-symbol=need-remove,"+dn));
	}
	
	@Test
	public void test_isMoveCauseMerge(){
		LdapDepartment dept=new LdapDepartment();
		dept.setName("部门21");
		dept.setSymbol("bumen21");
		dept.setCreator("nimei");
		deptService.create(dn, dept);
		Assert.assertEquals(1,deptService.isMoveCauseMerge(dn, "vmt-symbol=bumen21,vmt-symbol=bumen2,"+dn, false).size());
		Assert.assertEquals(0,deptService.isMoveCauseMerge(dn, "vmt-symbol=bumen21,vmt-symbol=bumen2,"+dn, true).size());
	}
	
	@Test
	public void test_isNameExists(){
		Assert.assertTrue(deptService.isNameExists(dn, "部门2"));
		Assert.assertTrue(deptService.isNameExists(dn, "部门1"));
		Assert.assertFalse(deptService.isNameExists(dn, "部门222"));
	}
	
	@Test
	public void test_move(){
		String bumen21="vmt-symbol=bumen21,vmt-symbol=bumen2,"+dn;
		deptService.moveDepartment("vmt-symbol=bumen1,"+dn, bumen21, true,false);
		
		Assert.assertTrue(commonService.isExist("vmt-umtId=10000083,vmt-symbol=bumen1,"+dn));
		Assert.assertTrue(commonService.isExist("vmt-umtId=10000084,vmt-symbol=bumen1,"+dn));
	}
	@Test
	public void test_move1(){
		String bumen21="vmt-symbol=bumen21,vmt-symbol=bumen2,"+dn;
		deptService.moveDepartment("vmt-symbol=bumen1,"+dn, bumen21, false,false);
		
		Assert.assertTrue(commonService.isExist("vmt-umtId=10000083,vmt-symbol=bumen21,vmt-symbol=bumen1,"+dn));
		Assert.assertTrue(commonService.isExist("vmt-umtId=10000084,vmt-symbol=bumen21,vmt-symbol=bumen1,"+dn));
	}
}
