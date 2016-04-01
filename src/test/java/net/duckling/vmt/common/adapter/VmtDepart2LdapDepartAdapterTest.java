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

import junit.framework.Assert;
import net.duckling.vmt.api.domain.VmtDepart;
import net.duckling.vmt.domain.ldap.LdapDepartment;

import org.junit.Test;

/**
 * @author lvly
 * @since 2013-6-5
 */
public class VmtDepart2LdapDepartAdapterTest {
	@Test
	public void test1(){
		VmtDepart depart=new VmtDepart();
		depart.setCreator("100001");
		depart.setCurrentDisplay("1,2,3");
		depart.setDn("a=1,b=3");
		depart.setName("测试部门");
		depart.setSymbol("symbol");
		
		LdapDepartment ldapDepart=VmtDepart2LdapDepartAdapter.convert(depart);
		Assert.assertEquals(depart.getCreator(), ldapDepart.getCreator());
		Assert.assertEquals(depart.getCurrentDisplay(), ldapDepart.getCurrentDisplay());
		Assert.assertEquals(depart.getDn(), ldapDepart.getDn());
		Assert.assertEquals(depart.getName(), ldapDepart.getName());
		Assert.assertEquals(depart.getSymbol(), ldapDepart.getSymbol());
		
	}

}
