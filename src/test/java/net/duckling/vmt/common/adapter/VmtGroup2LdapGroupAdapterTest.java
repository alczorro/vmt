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
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.domain.ldap.LdapGroup;

import org.junit.Test;

/**
 * @author lvly
 * @since 2013-6-5
 */
public class VmtGroup2LdapGroupAdapterTest {
	@Test
	public void testl2vSingle(){
		LdapGroup group=new LdapGroup();
		group.setCount(1);
		group.setAdmins(new String[]{"1","2"});
		group.setCreator("1");
		group.setCurrentDisplay("1,2");
		group.setDn("a=1,b=2");
		group.setName("name");
		group.setSymbol("symbol");
		group.setFrom("nima");
		
		VmtGroup vmtGroup=VmtGroup2LdapGroupAdapter.convert(group);
		
		Assert.assertEquals(group.getCount(), vmtGroup.getCount());
		Assert.assertEquals(group.getCreator(), vmtGroup.getCreator());
		Assert.assertEquals(group.getDn(),vmtGroup.getDn());
		Assert.assertEquals(group.getFrom(), vmtGroup.getFrom());
		Assert.assertEquals(group.getName(), vmtGroup.getName());
		Assert.assertEquals(group.getSymbol(), vmtGroup.getSymbol());
		Assert.assertEquals(group.getAdmins().length,vmtGroup.getAdmins().length);
		Assert.assertEquals(group.getAdmins()[0],vmtGroup.getAdmins()[0]);
		Assert.assertEquals(group.getAdmins()[1],vmtGroup.getAdmins()[1]);
	}
	@Test
	public void testv2lSingle(){
		VmtGroup group =new VmtGroup();
		group.setCount(1);
		group.setAdmins(new String[]{"1","2"});
		group.setCreator("1");
		group.setDn("a=1,b=2");
		group.setName("name");
		group.setSymbol("symbol");
		group.setFrom("nima");
		LdapGroup ldapGroup=VmtGroup2LdapGroupAdapter.convert(group);
		Assert.assertEquals(group.getCount(), ldapGroup.getCount());
		Assert.assertEquals(group.getCreator(), ldapGroup.getCreator());
		Assert.assertEquals(group.getDn(),ldapGroup.getDn());
		Assert.assertEquals(group.getFrom(), ldapGroup.getFrom());
		Assert.assertEquals(group.getName(), ldapGroup.getName());
		Assert.assertEquals(group.getSymbol(), ldapGroup.getSymbol());
		Assert.assertEquals(group.getAdmins().length,ldapGroup.getAdmins().length);
		Assert.assertEquals(group.getAdmins()[0],ldapGroup.getAdmins()[0]);
		Assert.assertEquals(group.getAdmins()[1],ldapGroup.getAdmins()[1]);
	}
	@Test
	public void testBatch(){
		List<LdapGroup> groups=new ArrayList<LdapGroup>();
		LdapGroup group1=new LdapGroup();
		group1.setCount(1);
		group1.setAdmins(new String[]{"1","2"});
		group1.setCreator("1");
		group1.setCurrentDisplay("1,2");
		group1.setDn("a=1,b=2");
		group1.setName("name");
		group1.setSymbol("symbol");
		group1.setFrom("nima");
		LdapGroup group2=new LdapGroup();
		group2.setCount(2);
		group2.setAdmins(new String[]{"2","2"});
		group2.setCreator("2");
		group2.setCurrentDisplay("1,2,2");
		group2.setDn("a=1,b=2,c=3");
		group2.setName("name2");
		group2.setSymbol("symbol2");
		group2.setFrom("nima2");
		groups.add(group1);
		groups.add(group2);
		List<VmtGroup> vmtGroups=VmtGroup2LdapGroupAdapter.convert(groups);
		Assert.assertEquals(2, vmtGroups.size());
		VmtGroup vmtGroup1=vmtGroups.get(0);
		VmtGroup vmtGroup2=vmtGroups.get(1);
		Assert.assertEquals(group1.getCount(), vmtGroup1.getCount());
		Assert.assertEquals(group1.getCreator(), vmtGroup1.getCreator());
		Assert.assertEquals(group1.getDn(),vmtGroup1.getDn());
		Assert.assertEquals(group1.getFrom(), vmtGroup1.getFrom());
		Assert.assertEquals(group1.getName(), vmtGroup1.getName());
		Assert.assertEquals(group1.getSymbol(), vmtGroup1.getSymbol());
		Assert.assertEquals(group1.getAdmins().length,vmtGroup1.getAdmins().length);
		Assert.assertEquals(group1.getAdmins()[0],vmtGroup1.getAdmins()[0]);
		Assert.assertEquals(group1.getAdmins()[1],vmtGroup1.getAdmins()[1]);
		
		Assert.assertEquals(group2.getCount(), vmtGroup2.getCount());
		Assert.assertEquals(group2.getCreator(), vmtGroup2.getCreator());
		Assert.assertEquals(group2.getDn(),vmtGroup2.getDn());
		Assert.assertEquals(group2.getFrom(), vmtGroup2.getFrom());
		Assert.assertEquals(group2.getName(), vmtGroup2.getName());
		Assert.assertEquals(group2.getSymbol(), vmtGroup2.getSymbol());
		Assert.assertEquals(group2.getAdmins().length,vmtGroup2.getAdmins().length);
		Assert.assertEquals(group2.getAdmins()[0],vmtGroup2.getAdmins()[0]);
		Assert.assertEquals(group2.getAdmins()[1],vmtGroup2.getAdmins()[1]);
		
		
	}

}
