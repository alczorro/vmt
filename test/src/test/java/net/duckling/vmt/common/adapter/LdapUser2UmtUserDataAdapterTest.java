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
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.view.UmtUserData;

import org.junit.Test;

/**
 * @author lvly
 * @since 2013-6-3
 */
public class LdapUser2UmtUserDataAdapterTest {
	@Test
	public void test() {
		List<LdapUser> users=new ArrayList<LdapUser>();
		LdapUser user=new LdapUser();
		user.setCstnetId("test@test.com");
		user.setName("测试均");
		user.setUmtId("10001");
		users.add(user);
		List<UmtUserData> datas=LdapUser2UmtUserDataAdapter.convert(users);
		Assert.assertEquals(1, datas.size());
		UmtUserData data=datas.get(0);
		Assert.assertEquals(data.getCstnetId(), user.getCstnetId());
		Assert.assertEquals(data.getTruename(), user.getName());
		Assert.assertEquals(data.getUmtId(), user.getUmtId());
	}
}
