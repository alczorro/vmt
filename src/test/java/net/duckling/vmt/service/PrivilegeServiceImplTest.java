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
package net.duckling.vmt.service;

import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.test.BuildData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class PrivilegeServiceImplTest {
	@Autowired
	private IPrivilegeService privService;

	@Autowired
	private IAttributeService attrService;

	private String dn;

	// 每次做完一个test，需要恢复成原来的数据
	@After
	public void reset() {
		BuildData.remove(dn);
	}

	@Before
	public void before() {
		dn = BuildData.resetOrg();
	}

	@Test
	public void test_canAdd() {
		// isAdmin
		Assert.assertTrue(privService.canAdd(dn, "10000080"));
		// notAAdmin
		Assert.assertFalse(privService.canAdd(dn, "10000083"));
		// not a member
		Assert.assertFalse(privService.canAdd(dn, "10000ddd083"));

		// update team priv
		attrService.update(dn, "vmt-privilege",
				LdapOrg.PRIVILEGE_PRIVATE_ALLOW_ADD);
		// isAdmin
		Assert.assertTrue(privService.canAdd(dn, "10000080"));
		// notAAdmin
		Assert.assertTrue(privService.canAdd(dn, "10000083"));
		// not a member
		Assert.assertFalse(privService.canAdd(dn, "10000ddd083"));
	}

	@Test
	public void test_canLook() {
		// isAdmin
		Assert.assertTrue(privService.canLook(dn, "10000080"));
		// notAAdmin
		Assert.assertTrue(privService.canLook(dn, "10000083"));
		// not a member
		Assert.assertFalse(privService.canAdd(dn, "10000ddd083"));

		// update team priv
		attrService.update(dn, "vmt-member-visible", "false");

		// isAdmin
		Assert.assertTrue(privService.canLook(dn, "10000080"));
		// notAAdmin
		Assert.assertFalse(privService.canLook(dn, "10000083"));
		// not a member
		Assert.assertFalse(privService.canAdd(dn, "10000ddd083"));
	}

	@Test
	public void test_canLookUnConfirm() {
		// isAdmin
		Assert.assertTrue(privService.canLookUnConfirm(dn, "10000080"));
		// notAAdmin
		Assert.assertFalse(privService.canLookUnConfirm(dn, "10000083"));
		// not a member
		Assert.assertFalse(privService.canLookUnConfirm(dn, "10000ddd083"));
		// update team priv
		attrService.update(dn, "vmt-unconfirm-visible", "true");
		// isAdmin
		Assert.assertTrue(privService.canLookUnConfirm(dn, "10000080"));
		// notAAdmin
		Assert.assertTrue(privService.canLookUnConfirm(dn, "10000083"));
		// not a member
		Assert.assertFalse(privService.canLookUnConfirm(dn, "10000ddd083"));
	}
	
	@Test
	public void test_isAdmin(){
		Assert.assertTrue(privService.isAdmin(dn, "10000080"));
		Assert.assertTrue(privService.isAdmin(dn, "10000079"));
		Assert.assertFalse(privService.isAdmin(dn, "10000081"));
		Assert.assertFalse(privService.isAdmin(dn, "unkownumtId"));
	}
	
}
