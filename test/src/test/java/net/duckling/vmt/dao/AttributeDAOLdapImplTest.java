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

import junit.framework.Assert;
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
 * @since 2013-6-7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class AttributeDAOLdapImplTest {
	@Autowired
	private IAttributeDAO attrDAO;
	private String attrName="vmt-admin";
	private String dn;
	//每次做完一个test，需要恢复成原来的数据
	@After
	public void reset(){
		BuildData.remove(dn);
	}
	@Before
	public void before(){
		dn=BuildData.resetGroup();
	}
	@Test
	public void test1(){
		//删除
		attrDAO.delete(dn, attrName, new String[]{"10000080"});
		String[] values=attrDAO.get(dn, attrName);
		Assert.assertEquals(values.length, 1);
		Assert.assertEquals(values[0],"10000079");
		
	}
	@Test
	public void test2(){
		//新增
		attrDAO.insert(dn, attrName, new String[]{"10000081"});
		String[] values=attrDAO.get(dn, attrName);
		Assert.assertEquals(values.length, 3);
		Assert.assertEquals(values[0],"10000080");
		Assert.assertEquals(values[1],"10000079");
		Assert.assertEquals(values[2],"10000081");
	}
	@Test
	public void test3(){
		//更新
		attrDAO.update(dn, attrName, "123");
		String[] values=attrDAO.get(dn, attrName);
		Assert.assertEquals(values.length, 1);
		Assert.assertEquals(values[0],"123");
	}
	@Test
	public void test4(){
		//查询
		attrDAO.get(dn, attrName);
		String[] values=attrDAO.get(dn, attrName);
		Assert.assertEquals(values.length, 2);
		Assert.assertEquals(values[0],"10000080");
		Assert.assertEquals(values[1],"10000079");
	}
	@Test
	public void test5(){
		//验证已存在的
		Assert.assertTrue(attrDAO.isExists(dn, attrName,"10000079" ));
		//验证不存在的
		Assert.assertFalse(attrDAO.isExists(dn, attrName, "isnotexists"));
	}
}
