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
package net.duckling.vmt.common.ldap;


import junit.framework.Assert;
import net.duckling.vmt.common.domain.Foo;

import org.junit.Test;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;

/**
 * @author lvly
 * @since 2013-6-5
 */
public class LdapMappingResolverTest {
	private LdapMappingResolver<Foo> resolver=new LdapMappingResolver<Foo>(Foo.class);
	private Foo foo=new Foo();
	{
		foo.setA("a");
		foo.setB(2);
		foo.setC(false);
		foo.setD(new Integer[]{1,2,3});
		foo.setE(new String[]{"a",null,"b","c"});
	}
	@Test
	public void testBuild()throws Exception{
		DirContextOperations opera=resolver.build(foo, "dc=1");
		Assert.assertEquals(opera.getDn().toString(), "rdn=2,dc=1");
		Assert.assertEquals(opera.getStringAttribute("c"), "false");
		Assert.assertEquals(opera.getAttributes().get("d").size(),3);
		Assert.assertEquals(opera.getAttributes().get("d").get(0), 1);
		Assert.assertEquals(opera.getAttributes().get("d").get(1), 2);
		Assert.assertEquals(opera.getAttributes().get("d").get(2), 3);
		
		Assert.assertEquals(opera.getAttributes().get("e").size(),3);
		Assert.assertEquals(opera.getAttributes().get("e").get(0), "a");
		Assert.assertEquals(opera.getAttributes().get("e").get(1), "b");
		Assert.assertEquals(opera.getAttributes().get("e").get(2), "c");
		
	}
	
	@Test
	public void testContextMapper(){
		ContextMapper mapping=resolver.getContextMapper();
		DirContextAdapter adp=new DirContextAdapter();
		adp.setAttributeValue("rdn",2);
		adp.setDn(new DistinguishedName("dn=d,dc=c"));
		adp.setAttributeValue("c", true);
		adp.setAttributeValues("d", foo.getD(), false);
		adp.setAttributeValues("e", new String[]{"a1","a2"}, false);
		adp.setAttributeValue("userPassword", "ldappassword".getBytes());
		Foo foo=(Foo)mapping.mapFromContext(adp);
		
		Assert.assertEquals(foo.getA(), "dn=d,dc=c");
		Assert.assertEquals(foo.getB(), 2);
		Assert.assertTrue(foo.isC());
		
		Assert.assertEquals(foo.getD().length,3);
		Assert.assertEquals(foo.getD()[0].toString(), "1");
		Assert.assertEquals(foo.getD()[1].toString(), "2");
		Assert.assertEquals(foo.getD()[2].toString(), "3");
		
		Assert.assertEquals(foo.getE().length,2);
		Assert.assertEquals(foo.getE()[0].toString(), "a1");
		Assert.assertEquals(foo.getE()[1].toString(), "a2");
		Assert.assertEquals(foo.getG(), "ldappassword");
	}
	@Test
	public void testSmall(){
		Assert.assertEquals("rdn=2,pdn=1", resolver.getDN(foo, "pdn=1").toString());
		Assert.assertEquals("rdn=nima,pdn=1", resolver.getDN("nima", "pdn=1").toString());
		Assert.assertEquals("rdn", resolver.getRdnKey());
		Assert.assertEquals("2", resolver.getRdnValue(foo));
	}
}
