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
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.test.BuildData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 节点的Service， 节点是group和org的抽象类
 * @author lvly
 * @since 2013-6-9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class NodeDAOImplTest {
	@Autowired
	private INodeDAO nodeDAO;
	
	private String dn;
	
	@Before
	public void before(){
		dn=BuildData.resetOrg();
	}
	@After
	public void after(){
		BuildData.remove(dn);
	}
	
	@Test
	public void test_getNode(){
		LdapNode node=nodeDAO.getNode("vmt-symbol=bumen1,"+dn);
		Assert.assertEquals(node.getCurrentDisplay(),"JUNIT测试用组织勿动,部门1");
		Assert.assertEquals(node.getSymbol(), "bumen1");
		node=nodeDAO.getNode(dn);
		Assert.assertEquals("ddl",node.getFrom());
	}
	
	@Test
	public void test_isSymbolUsed(){
		Assert.assertTrue(nodeDAO.isSymbolUsed("vmt-symbol=bumen2,"+dn, "bumen21"));
		Assert.assertTrue(nodeDAO.isSymbolUsed(dn, "bumen1"));
		Assert.assertFalse(nodeDAO.isSymbolUsed("vmt-symbol=bumen2,"+dn, "bumen21xxx"));
	}
	@Test
	public void test_updateSonsAndMyDisplayName(){
		DistinguishedName dN=new DistinguishedName(dn);
		nodeDAO.updateSonsAndMyDisplayName(dn, dN.size(), "新名称");
		LdapNode node=nodeDAO.getNode("vmt-symbol=bumen1,"+dn);
		Assert.assertEquals("新名称,部门1", node.getCurrentDisplay());
	}
}
