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
import net.duckling.vmt.common.util.PinyinUtils;
import net.duckling.vmt.domain.ldap.LdapUser;

import org.junit.Test;

import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;

/**
 * @author lvly
 * @since 2013-6-3
 */
public class UmtUser2LdapUserAdapterTest {
	@Test
	public void testSinglel2u(){
		LdapUser user=new LdapUser();
		user.setCstnetId("test@test.com");
		user.setName("测试均");
		user.setUmtId("100001");
		UMTUser umtUsr=UmtUser2LdapUserAdapter.convert(user);
		Assert.assertEquals(user.getCstnetId(), umtUsr.getCstnetId());
		Assert.assertEquals(user.getName(), umtUsr.getTruename());
		Assert.assertEquals(user.getUmtId(), umtUsr.getUmtId());
	}
	@Test
	public void testBatchu2l(){
		List<UMTUser> result=new ArrayList<UMTUser>();
		UMTUser umtUser1=new UMTUser();
		umtUser1.setCstnetId("test1@test.com");
		umtUser1.setTruename("测试均1");
		umtUser1.setUmtId("100001");
		UMTUser umtUser2=new UMTUser();
		umtUser2.setCstnetId("test2@test.com");
		umtUser2.setTruename("测试均2");
		umtUser2.setUmtId("100002");
		result.add(umtUser1);
		result.add(umtUser2);
		List<LdapUser> users=UmtUser2LdapUserAdapter.convert(result, "vmt-symbol=ss,ou=org", LdapUser.STATUS_ACTIVE);
		Assert.assertEquals(2, users.size());
		LdapUser user1=users.get(0);
		Assert.assertEquals(user1.getCstnetId(), umtUser1.getCstnetId());
		Assert.assertEquals(user1.getName(), umtUser1.getTruename());
		Assert.assertEquals(user1.getUmtId(), umtUser1.getUmtId());
		Assert.assertEquals(user1.getPinyin(), PinyinUtils.getPinyin(umtUser1.getTruename()));
		Assert.assertEquals("vmt-symbol=ss,ou=org", user1.getRoot());
		Assert.assertEquals(LdapUser.STATUS_ACTIVE, user1.getStatus());
		LdapUser user2=users.get(1);
		Assert.assertEquals(user2.getCstnetId(), umtUser2.getCstnetId());
		Assert.assertEquals(user2.getName(), umtUser2.getTruename());
		Assert.assertEquals(user2.getUmtId(), umtUser2.getUmtId());
		Assert.assertEquals(user2.getPinyin(), PinyinUtils.getPinyin(umtUser2.getTruename()));
		Assert.assertEquals("vmt-symbol=ss,ou=org", user2.getRoot());
		Assert.assertEquals(LdapUser.STATUS_ACTIVE, user2.getStatus());
	}
	@Test
	public void testSingleu2l(){
		UMTUser umtUser1=new UMTUser();
		umtUser1.setCstnetId("test1@test.com");
		umtUser1.setTruename("测试均1");
		umtUser1.setUmtId("100001");
		LdapUser user=UmtUser2LdapUserAdapter.convert(umtUser1, "vmt-symbol=ss,ou=org",LdapUser.STATUS_ACTIVE);
		Assert.assertEquals("c", user.getCode());
		Assert.assertEquals(umtUser1.getCstnetId(), user.getCstnetId());
		Assert.assertEquals("ceshijun1", user.getPinyin());
		Assert.assertEquals(40, user.getRandom().length());
		Assert.assertEquals("vmt-symbol=ss,ou=org", user.getRoot());
		Assert.assertEquals(user.getStatus(), LdapUser.STATUS_ACTIVE);
		Assert.assertEquals(user.getUmtId(), umtUser1.getUmtId());
		Assert.assertEquals(user.getName(), umtUser1.getTruename());
	}
	

}
