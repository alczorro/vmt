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
import net.duckling.vmt.domain.view.SearchLetterData;

import org.junit.Test;

/**
 * @author lvly
 * @since 2013-6-3
 */
public class LdapUser2SearchLetterDataAdapterTest {
	
	@Test
	public void testConvert(){
		List<LdapUser> users=new ArrayList<LdapUser>();
		LdapUser user1=new LdapUser();
		user1.setCode("c");
		user1.setPinyin("ceshijun");
		user1.setName("测试君2");
		user1.setCstnetId("test1@test.com");
		users.add(user1);
		LdapUser user2=new LdapUser();
		user2.setCode("c");
		user2.setPinyin("ceshijun2");
		user2.setName("测试君2");
		user2.setCstnetId("test2@test.com");
		users.add(user2);
		LdapUser user3=new LdapUser();
		user3.setCode("s");
		user3.setPinyin("shijun2");
		user3.setName("试君2");
		user3.setCstnetId("test3@test.com");
		users.add(user3);
		LdapUser2SearchLetterDataAdapter adapter=new LdapUser2SearchLetterDataAdapter(users);
		List<SearchLetterData> searches=adapter.getData();
		for(SearchLetterData data:searches){
			if(data.getLetter().equals("c")){
				Assert.assertTrue(data.getData().size()==2);
			}else if(data.getLetter().equals("s")){
				Assert.assertTrue(data.getData().size()==1);
			}else{
				Assert.assertTrue(data.getData().size()==0);
			}
		}
	}
}
