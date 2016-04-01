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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.duckling.vmt.domain.ldap.LdapUser;
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
 * @since 2013-6-18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class UserDAOLdapImplTest {
	@Autowired
	private IUserDAO userDAO;
	
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
	public void test_getUser_addUsertoNode(){
		List<LdapUser> users=new ArrayList<LdapUser>();
		LdapUser user1=new LdapUser();
		user1.setCode("h");
		user1.setCstnetId("haha@cstnet.cn");
		user1.setName("haha君");
		user1.setPinyin("hahajun");
		user1.setRandom("oiuoijeslkjrlkndsfijdsof");
		user1.setRoot(dn);
		user1.setStatus(LdapUser.STATUS_ACTIVE);
		user1.setUmtId("10002079");
		user1.setCurrentDisplay("JUNIT测试用群组勿动,部门1");
		users.add(user1);
		LdapUser user2=new LdapUser();
		user2.setCode("h");
		user2.setCstnetId("haha@cstnet.cn");
		user2.setName("haha君");
		user2.setPinyin("hahajun");
		user2.setRandom("oiuoijeslkjrlkndsfijdsof");
		user2.setRoot(dn);
		user2.setStatus(LdapUser.STATUS_ACTIVE);
		user2.setUmtId("10021079");
		user2.setCurrentDisplay("JUNIT测试用群组勿动,部门1");
		users.add(user2);
		userDAO.addUserToNode(dn, users);
		
		
		Assert.assertTrue(commonDAO.isExist(user1.getDn()));
		Assert.assertTrue(commonDAO.isExist(user2.getDn()));
		
		LdapUser cuser1=userDAO.getUser(user1.getDn());
		LdapUser cuser2=userDAO.getUser(user2.getDn());
		Assert.assertEquals(cuser1.getCstnetId(), user1.getCstnetId());
		Assert.assertEquals(cuser1.getCurrentDisplay(), user1.getCurrentDisplay());
		Assert.assertEquals(cuser1.getDn(), user1.getDn());
		Assert.assertEquals(cuser1.getName(), user1.getName());
		Assert.assertEquals(cuser1.getOid(),user1.getOid());
		Assert.assertEquals(cuser1.getPinyin(), user1.getPinyin());
		Assert.assertEquals(cuser1.getRandom(), user1.getRandom());
		Assert.assertEquals(cuser1.getRoot(), user1.getRoot());
		Assert.assertEquals(cuser1.getStatus(), user1.getStatus());
		Assert.assertEquals(cuser1.getUmtId(), user1.getUmtId());
		
		Assert.assertEquals(cuser2.getCstnetId(), user2.getCstnetId());
		Assert.assertEquals(cuser2.getCurrentDisplay(), user2.getCurrentDisplay());
		Assert.assertEquals(cuser2.getDn(), user2.getDn());
		Assert.assertEquals(cuser2.getName(), user2.getName());
		Assert.assertEquals(cuser2.getOid(),user2.getOid());
		Assert.assertEquals(cuser2.getPinyin(), user2.getPinyin());
		Assert.assertEquals(cuser2.getRandom(), user2.getRandom());
		Assert.assertEquals(cuser2.getRoot(), user2.getRoot());
		Assert.assertEquals(cuser2.getStatus(), user2.getStatus());
		Assert.assertEquals(cuser2.getUmtId(), user2.getUmtId());
		
	}
	
	@Test
	public void test_checkRandomOK(){
		LdapUser user=userDAO.checkRandomOK("","oiuoijeslkjrlkndsfijdsof", "10000079");
		Assert.assertEquals("haha君", user.getName());
		Assert.assertEquals("oiuoijeslkjrlkndsfijdsof", user.getRandom());
		Assert.assertEquals("10000079",user.getUmtId());
		user=userDAO.checkRandomOK("","oiuoijeslkjrlkndsfijdsof", "1000d0079");
		Assert.assertNull(user);
		user=userDAO.checkRandomOK("","oiuoijeslkdddjrlkndsfijdsof", "10000079");
		Assert.assertNull(user);
	}
	@Test
	public void test_getSubUserCount(){
		Assert.assertEquals(6,userDAO.getSubUserCount(dn));
		Assert.assertEquals(3,userDAO.getSubUserCount("vmt-symbol=bumen2,"+dn));
	}
	
	@Test
	public void test_isExistLevel(){
		Assert.assertTrue(userDAO.isExistLevel("vmt-symbol=bumen2,"+dn, "10000082"));
		Assert.assertFalse(userDAO.isExistLevel("vmt-symbol=bumen2,"+dn, "10000083"));
		Assert.assertFalse(userDAO.isExistLevel("vmt-symbol=bumen2,"+dn, "100dd00083"));
	}
	@Test 
	public void test_isExistsSubTree(){
		Assert.assertTrue(userDAO.isExistsSubTree(dn, "10000081", false));
		Assert.assertFalse(userDAO.isExistsSubTree(dn, "10000081", true));
		Assert.assertFalse(userDAO.isExistsSubTree(dn, "10000ddd081", true));
		
		Assert.assertTrue(userDAO.isExistsSubTree("vmt-symbol=bumen2,"+dn, "10000083",true));
		Assert.assertTrue(userDAO.isExistsSubTree("vmt-symbol=bumen2,"+dn, "10000083",false));
	}
	
	@Test
	public void test_move(){
		String userDn="vmt-umtId=10000082,vmt-symbol=bumen2,"+dn;
		userDAO.move(new String[]{userDn}, dn);
		LdapUser user=userDAO.getUser("vmt-umtId=10000082,"+dn);
		Assert.assertEquals("徐志坚君", user.getName());
		Assert.assertEquals("10000082", user.getUmtId());  
	}
	@Test
	public void test_searchUserByAll(){
		List<LdapUser> users=userDAO.searchUserByAll(new String[]{dn}, new boolean[]{true},true);
		Assert.assertEquals(6,users.size());
		users=userDAO.searchUserByAll(new String[]{dn}, new boolean[]{false},true);
		Assert.assertEquals(4,users.size());
	}
	@Test
	public void test_searchUserByDN(){
		List<LdapUser> users=userDAO.searchUserByDN(dn, "10000082");
		Assert.assertEquals(5,users.size());
	}
	@Test
	public void test_searchUserByKeyword(){
		List<LdapUser> users=userDAO.searchUserByKeyword("a", new String[]{dn},null, new boolean[]{false},true);
		Assert.assertEquals(3,users.size());
		users=userDAO.searchUserByKeyword("a", new String[]{dn},null, new boolean[]{true},true);
		Assert.assertEquals(5,users.size());
	}
	@Test
	public void test_searchUserByLetter(){
		List<LdapUser> users=userDAO.searchUsersByLetter("x", new String[]{dn}, new boolean[]{true},true);
		Assert.assertEquals(1,users.size());
		Assert.assertEquals("x", users.get(0).getCode());
	}
	@Test
	public void test_searchUsersByCstnetId(){
		List<LdapUser> users=userDAO.searchUsersByCstnetId(dn, new String[]{"aaa@cstnet.cn","yangxuantest111@cstnet.cn"});
		Assert.assertEquals(2,users.size());
		Assert.assertEquals("aaa@cstnet.cn", users.get(0).getCstnetId());
		Assert.assertEquals("yangxuantest111@cstnet.cn", users.get(1).getCstnetId());
	}
	@Test
	public void test_searchUsersByUmtId(){
		List<LdapUser> users=userDAO.searchUsersByUmtId(dn, new String[]{"10000080","10000081"});
		Assert.assertEquals(2,users.size());
		Assert.assertEquals("10000080", users.get(0).getUmtId());
		Assert.assertEquals("10000081", users.get(1).getUmtId());
	}
	@Test
	public void test_unbind(){
		String userDn="vmt-umtId=10000082,vmt-symbol=bumen2,"+dn;
		userDAO.unbind(new String[]{userDn});
		Assert.assertFalse(commonDAO.isExist(userDn));
	}
}
