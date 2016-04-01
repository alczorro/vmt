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
package net.duckling.vmt.service;

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
 * @since 2013-6-19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class AttributeServiceImplTest {
	
	@Autowired
	private IAttributeService attrService;
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
	public void test_delete(){
		attrService.delete(dn, "vmt-admin",new String[]{"10000080","10000079"});
		Assert.assertNull(attrService.get(dn, "vmt-admin"));
	}
	@Test
	public void test_delete1(){
		attrService.delete(dn, "vmt-admin",new String[]{"10000079"});
		String[] admins=attrService.get(dn, "vmt-admin");
		Assert.assertEquals(admins.length,1);
		Assert.assertEquals("10000080", admins[0]);
	}
	@Test
	public void test_get(){
		String[] admins=attrService.get(dn, "vmt-admin");
		Assert.assertEquals(admins.length,2);
		Assert.assertEquals("10000080", admins[0]);
		Assert.assertEquals("10000079", admins[1]);
	}
	
	@Test
	public void test_insert(){
		attrService.insert(dn, "vmt-admin", new String[]{"a","b"});
		String[] admins=attrService.get(dn, "vmt-admin");
		Assert.assertEquals(admins.length,4);
		Assert.assertEquals("10000080", admins[0]);
		Assert.assertEquals("10000079", admins[1]);
		Assert.assertEquals("b", admins[2]);
		Assert.assertEquals("a", admins[3]);
	}
	@Test
	public void test_isExists(){
		Assert.assertTrue(attrService.isExists(dn, "vmt-admin", "10000079"));
		Assert.assertTrue(attrService.isExists(dn, "vmt-admin", "10000080"));
		Assert.assertFalse(attrService.isExists(dn, "vmt-admin", "a"));
	}
	
	@Test
	public void test_update(){
		attrService.update(dn, "vmt-admin", "wokao");
		String[] admins=attrService.get(dn, "vmt-admin");
		Assert.assertEquals(admins.length,1);
		Assert.assertEquals("wokao", admins[0]);
	}
}
