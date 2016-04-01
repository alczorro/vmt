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
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.domain.ldap.LdapUser;

import org.junit.Test;

/**
 * @author lvly
 * @since 2013-6-5
 */
public class VmtUser2LdapUserAdapterTest {
	@Test
	public void tetl2v(){
		LdapUser user=new LdapUser();
		user.setCstnetId("test@teset.com");
		user.setCurrentDisplay("display");
		user.setDn("dn");
		user.setName("name");
		user.setStatus("refuse");
		user.setUmtId("123");
		VmtUser vmtUser=VmtUser2LdapUserAdapter.convert(user);
		Assert.assertEquals(user.getCstnetId(), vmtUser.getCstnetId());
		Assert.assertEquals(user.getCurrentDisplay(), vmtUser.getCurrentDisplay());
		Assert.assertEquals(user.getDn(), vmtUser.getDn());
		Assert.assertEquals(user.getName(), vmtUser.getName());
		Assert.assertEquals(user.getStatus(), vmtUser.getStatus());
		Assert.assertEquals(user.getUmtId(), vmtUser.getUmtId());
	}
	@Test
	public void testbatch(){
		List<LdapUser> users=null;
		Assert.assertTrue(CommonUtils.isNull(users));
		users=new ArrayList<>();
		Assert.assertTrue(CommonUtils.isNull(users));
		LdapUser user1=new LdapUser();
		user1.setCstnetId("test@teset.com");
		user1.setCurrentDisplay("display");
		user1.setDn("dn");
		user1.setName("name");
		user1.setStatus("refuse");
		user1.setUmtId("123");
		
		LdapUser user2=new LdapUser();
		user2.setCstnetId("test@teset.com");
		user2.setCurrentDisplay("display");
		user2.setDn("dn");
		user2.setName("name");
		user2.setStatus("refuse");
		user2.setUmtId("123");
		users.add(user1);
		users.add(user2);
		List<VmtUser> vmtUsers=VmtUser2LdapUserAdapter.convert(users);
		Assert.assertEquals(2, vmtUsers.size());
		VmtUser vmtUser1=vmtUsers.get(0);
		VmtUser vmtUser2=vmtUsers.get(1);
		Assert.assertEquals(user1.getCstnetId(), vmtUser1.getCstnetId());
		Assert.assertEquals(user1.getCurrentDisplay(), vmtUser1.getCurrentDisplay());
		Assert.assertEquals(user1.getDn(), vmtUser1.getDn());
		Assert.assertEquals(user1.getName(), vmtUser1.getName());
		Assert.assertEquals(user1.getStatus(), vmtUser1.getStatus());
		Assert.assertEquals(user1.getUmtId(), vmtUser1.getUmtId());
		
		Assert.assertEquals(user2.getCstnetId(), vmtUser2.getCstnetId());
		Assert.assertEquals(user2.getCurrentDisplay(), vmtUser2.getCurrentDisplay());
		Assert.assertEquals(user2.getDn(), vmtUser2.getDn());
		Assert.assertEquals(user2.getName(), vmtUser2.getName());
		Assert.assertEquals(user2.getStatus(), vmtUser2.getStatus());
		Assert.assertEquals(user2.getUmtId(), vmtUser2.getUmtId());
		
		
	}

}
