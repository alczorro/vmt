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
package net.duckling.vmt.common.adapter;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.domain.ldap.LdapOrg;

import org.junit.Test;

/**
 * @author lvly
 * @since 2013-6-5
 */
public class VmtOrg2LdapOrgAdapterTest {
	@Test
	public void testl2vSingle(){
		LdapOrg org=new LdapOrg();
		org.setAdmins(new String[]{"1","2","3"});
		org.setCount(1);
		org.setCreator("123");
		org.setDn("a=1,b=2");
		org.setName("name1");
		org.setSymbol("symbol");
		org.setCurrentDisplay("display");
		VmtOrg vmtOrg=VmtOrg2LdapOrgAdapter.convert(org);
		
		Assert.assertEquals(org.getAdmins().length, vmtOrg.getAdmins().length);
		Assert.assertEquals(org.getAdmins()[0], vmtOrg.getAdmins()[0]);
		Assert.assertEquals(org.getAdmins()[1], vmtOrg.getAdmins()[1]);
		Assert.assertEquals(org.getAdmins()[2], vmtOrg.getAdmins()[2]);
		Assert.assertEquals(org.getCount(), vmtOrg.getCount());
		Assert.assertEquals(org.getDn(), vmtOrg.getDn());
		Assert.assertEquals(org.getName(), vmtOrg.getName());
		Assert.assertEquals(org.getSymbol(), vmtOrg.getSymbol());
		Assert.assertEquals(org.getCurrentDisplay(), vmtOrg.getCurrentDisplay());
	}
	@Test
	public void testv2lSingle(){
		VmtOrg org=new VmtOrg();
		org.setAdmins(new String[]{"1","2","3"});
		org.setCount(1);
		org.setCreator("123");
		org.setDn("a=1,b=2");
		org.setName("name1");
		org.setSymbol("symbol");
		org.setCurrentDisplay("display");
		LdapOrg ldapOrg=VmtOrg2LdapOrgAdapter.convert(org);
		
		Assert.assertEquals(org.getAdmins().length, ldapOrg.getAdmins().length);
		Assert.assertEquals(org.getAdmins()[0], ldapOrg.getAdmins()[0]);
		Assert.assertEquals(org.getAdmins()[1], ldapOrg.getAdmins()[1]);
		Assert.assertEquals(org.getAdmins()[2], ldapOrg.getAdmins()[2]);
		Assert.assertEquals(org.getCount(), ldapOrg.getCount());
		Assert.assertEquals(org.getDn(), ldapOrg.getDn());
		Assert.assertEquals(org.getName(), ldapOrg.getName());
		Assert.assertEquals(org.getSymbol(), ldapOrg.getSymbol());
	}
	@Test
	public void testBatch(){
		List<LdapOrg> orgs=null;
		Assert.assertTrue(CommonUtils.isNull(VmtOrg2LdapOrgAdapter.convert(orgs)));
		orgs=new ArrayList<LdapOrg>();
		Assert.assertTrue(CommonUtils.isNull(VmtOrg2LdapOrgAdapter.convert(orgs)));
		LdapOrg org1=new LdapOrg();
		org1.setAdmins(new String[]{"1","2","3"});
		org1.setCount(1);
		org1.setCreator("123");
		org1.setDn("a=1,b=2");
		org1.setName("name1");
		org1.setSymbol("symbol");
		org1.setCurrentDisplay("display");
		
		LdapOrg org2=new LdapOrg();
		org2.setAdmins(new String[]{"1","2","32"});
		org2.setCount(1);
		org2.setCreator("1232");
		org2.setDn("a=1,b=22");
		org2.setName("name12");
		org2.setSymbol("symbol2");
		org2.setCurrentDisplay("display2");
		
		orgs.add(org1);
		orgs.add(org2);
		
		List<VmtOrg> vmtOrgs=VmtOrg2LdapOrgAdapter.convert(orgs);
		Assert.assertEquals(2, vmtOrgs.size());
		VmtOrg vmtOrg1=vmtOrgs.get(0);
		VmtOrg vmtOrg2=vmtOrgs.get(1);
		Assert.assertEquals(org1.getAdmins().length, vmtOrg1.getAdmins().length);
		Assert.assertEquals(org1.getAdmins()[0], vmtOrg1.getAdmins()[0]);
		Assert.assertEquals(org1.getAdmins()[1], vmtOrg1.getAdmins()[1]);
		Assert.assertEquals(org1.getAdmins()[2], vmtOrg1.getAdmins()[2]);
		Assert.assertEquals(org1.getCount(), vmtOrg1.getCount());
		Assert.assertEquals(org1.getDn(), vmtOrg1.getDn());
		Assert.assertEquals(org1.getName(), vmtOrg1.getName());
		Assert.assertEquals(org1.getSymbol(), vmtOrg1.getSymbol());
		Assert.assertEquals(org1.getCurrentDisplay(), vmtOrg1.getCurrentDisplay());
		
		Assert.assertEquals(org2.getAdmins()[0], vmtOrg2.getAdmins()[0]);
		Assert.assertEquals(org2.getAdmins()[1], vmtOrg2.getAdmins()[1]);
		Assert.assertEquals(org2.getAdmins()[2], vmtOrg2.getAdmins()[2]);
		Assert.assertEquals(org2.getCount(), vmtOrg2.getCount());
		Assert.assertEquals(org2.getDn(), vmtOrg2.getDn());
		Assert.assertEquals(org2.getName(), vmtOrg2.getName());
		Assert.assertEquals(org2.getSymbol(), vmtOrg2.getSymbol());
		Assert.assertEquals(org2.getCurrentDisplay(), vmtOrg2.getCurrentDisplay());
		
		
	}

}
