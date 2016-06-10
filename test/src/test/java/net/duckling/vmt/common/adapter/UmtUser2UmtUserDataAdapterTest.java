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
import net.duckling.vmt.domain.view.UmtUserData;

import org.junit.Test;

import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;

/**
 * @author lvly
 * @since 2013-6-4
 */
public class UmtUser2UmtUserDataAdapterTest {
	@Test
	public void test(){
		List<UMTUser> umtUsers=new ArrayList<UMTUser>();
		UMTUser user1=new UMTUser();
		user1.setCstnetId("test@1.com");
		user1.setTruename("测试均1");
		user1.setUmtId("100001");
		UMTUser user2=new UMTUser();
		user2.setCstnetId("test@1.com");
		user2.setTruename("测试均1");
		user2.setUmtId("100001");
		umtUsers.add(user1);
		umtUsers.add(user2);
		List<UmtUserData> datas=UmtUser2UmtUserDataAdapter.convert(umtUsers);
		Assert.assertEquals(2, datas.size());
		UmtUserData data1=datas.get(0);
		Assert.assertEquals(user1.getCstnetId(), data1.getCstnetId());
		Assert.assertEquals(user1.getTruename(), data1.getTruename());
		Assert.assertEquals(user1.getUmtId(), data1.getUmtId());
		UmtUserData data2=datas.get(1);
		Assert.assertEquals(user2.getCstnetId(), data2.getCstnetId());
		Assert.assertEquals(user2.getTruename(), data2.getTruename());
		Assert.assertEquals(user2.getUmtId(), data2.getUmtId());
	}
	@Test
	public void testNull(){
		Assert.assertTrue(CommonUtils.isNull(UmtUser2UmtUserDataAdapter.convert(null)));
		Assert.assertTrue(CommonUtils.isNull(UmtUser2UmtUserDataAdapter.convert(new ArrayList<UMTUser>())));
	}

}
