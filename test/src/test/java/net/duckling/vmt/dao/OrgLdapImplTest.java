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
package net.duckling.vmt.dao;


import java.util.List;

import junit.framework.Assert;
import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.test.BuildData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.InvalidNameException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class OrgLdapImplTest {
	
	@Autowired
	private IOrgDAO orgDAO;
	
	@Autowired
	private ICommonDAO commonDAO;
	
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
	public void test_insert_get_delete(){
		LdapOrg org=new LdapOrg();
		org.setName("JUNIT测试用组织勿动");
		org.setCount(6);
		org.setSymbol("testCeshiyongzuzhi"+System.nanoTime());
		org.setCurrentDisplay("JUNIT测试用组织勿动");
		org.setMemberVisible(true);
		org.setCreator("10000079");
		org.setAdmins(new String[]{"10000080","10000079"});
		org.setFrom("ddl");
		orgDAO.insert(org);
		
		LdapOrg dbOrg=orgDAO.getOrgByDN(org.getDn());
		Assert.assertEquals(dbOrg.getName(), org.getName());
		Assert.assertEquals(dbOrg.getCount(), org.getCount());
		Assert.assertEquals(dbOrg.getSymbol(), org.getSymbol());
		Assert.assertEquals(dbOrg.getCurrentDisplay(),org.getCurrentDisplay());
		Assert.assertEquals(dbOrg.getCreator(), org.getCreator());
		Assert.assertEquals(dbOrg.getFrom(), org.getFrom());
		
		commonDAO.unbind(org.getDn());
		Assert.assertNull(orgDAO.getOrgBySymbol(LdapUtils.getLastValue(org.getDn())));
		
	}
	
	@Test
	public void test_getAdminOrgs(){
		List<LdapOrg> orgs=orgDAO.getAdminOrgs("10000080");
		Assert.assertEquals(1, orgs.size());
		Assert.assertTrue(CommonUtils.isEqualsContain(orgs.get(0).getAdmins(), "10000080"));
		
		orgs=orgDAO.getAdminOrgs("10000079");
		Assert.assertEquals(1, orgs.size());
		Assert.assertTrue(CommonUtils.isEqualsContain(orgs.get(0).getAdmins(), "10000079"));
		
		orgs=orgDAO.getAdminOrgs("unkownuser");
		Assert.assertEquals(0, orgs.size());
	}
	
	@Test
	public void test_getAllOrgs(){
		List<LdapOrg> orgs=orgDAO.getAllOrgs();
		Assert.assertEquals(1, orgs.size());
	}
	
	@Test
	public void test_getMyOrgs(){
		List<LdapOrg> orgs=orgDAO.getMyOrgs("10000082");
		Assert.assertEquals(1, orgs.size());
		orgs=orgDAO.getMyOrgs("unkownuser");
		Assert.assertEquals(0, orgs.size());
	}
	
	@Test(expected=InvalidNameException.class)
	public void test_getOrgByDN(){
		LdapOrg org=orgDAO.getOrgByDN(dn);
		Assert.assertNotNull(org);
		org=orgDAO.getOrgByDN("unkowndn");
		Assert.assertNull(org);
	}
	
	@Test
	public void test_getOrgBySymbol(){
		Assert.assertNotNull(orgDAO.getOrgBySymbol(LdapUtils.getLastValue(dn)));
		Assert.assertNull(orgDAO.getOrgBySymbol("unkownsymbol"));
	}
	
	@Test
	public void test_getThirdPartyOrg(){
		Assert.assertNotNull(orgDAO.getThirdPartyOrg(LdapUtils.getLastValue(dn), "ddl"));
		Assert.assertNull(orgDAO.getThirdPartyOrg(LdapUtils.getLastValue(dn), "dchat"));
		Assert.assertNull(orgDAO.getThirdPartyOrg("unkownorgId", "ddl"));
	}
	
	@Test
	public void test_isExists(){
		Assert.assertTrue(orgDAO.isExists(LdapUtils.getLastValue(dn)));
		Assert.assertFalse(orgDAO.isExists("unkownOrg"));
	}
	
}
