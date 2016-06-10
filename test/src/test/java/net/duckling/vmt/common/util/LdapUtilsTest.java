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
package net.duckling.vmt.common.util;

import java.net.URLEncoder;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import net.duckling.vmt.domain.KEY;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.ldap.core.DistinguishedName;

/**
 * @author lvly
 * @since 2013-6-21
 */
public class LdapUtilsTest {
	@Test
	public void test_attr2StringArray() throws Exception {
		Attribute attribute = new BasicAttribute("a");
		attribute.add("haha");
		String[] result = LdapUtils.attr2StringArray(attribute);
		Assert.assertArrayEquals(new String[] { "haha" }, result);
		attribute.add("haha1");
		attribute.add("haha2");
		result = LdapUtils.attr2StringArray(attribute);
		Assert.assertArrayEquals(new String[] { "haha", "haha1", "haha2" }, result);
		result = LdapUtils.attr2StringArray(null);
		Assert.assertArrayEquals(new String[] {}, result);
	}
	
	@Test
	public void test_decode()throws Exception{
		String result="a=1,b=中文,c=3";
		for(int i=0;i<50;i++){
			result=URLEncoder.encode(result,KEY.GLOBAL_ENCODE);
		}
		
		Assert.assertNotSame("a=1,b=中文,c=3",LdapUtils.decode(result));
		
		result="a=1,b=中文,c=3";
		for(int i=0;i<5;i++){
			result=URLEncoder.encode(result,KEY.GLOBAL_ENCODE);
		}
		Assert.assertEquals("a=1,b=中文,c=3",LdapUtils.decode(result));
	}
	
	@Test
	public void test_decode2()throws Exception{
		String[] result=LdapUtils.decode(new String[]{URLEncoder.encode("a=1,b=2,c=3",KEY.GLOBAL_ENCODE),URLEncoder.encode("a=1,c=3",KEY.GLOBAL_ENCODE)});
		Assert.assertArrayEquals(new String[]{"a=1,b=2,c=3","a=1,c=3"}, result);
	}
	
	@Test
	public void test_encode()throws Exception{
		Assert.assertEquals(URLEncoder.encode("a=1,b=2,c=3",KEY.GLOBAL_ENCODE), LdapUtils.encode("a=1,b=2,c=3"));
	}
	@Test
	public void test_getDN()throws Exception{
		Assert.assertEquals("b=2,c=3", LdapUtils.getDN(new DistinguishedName("a=1,b=2,c=3"),2));
	}
	@Test
	public void test_getDN2(){
		Assert.assertEquals("b=2,c=3", LdapUtils.getDN("a=1,b=2,c=3",2));
	}
	@Test
	public void test_getLast(){
		Assert.assertEquals("a=1",LdapUtils.getLast("a=1,b=2,c=3"));
	}
	@Test
	public void test_getLastValue(){
		Assert.assertEquals("1",LdapUtils.getLastValue("a=1,b=2,c=3"));
	}
	@Test
	public void test_getNodeName(){
		Assert.assertEquals("c=3",LdapUtils.getNodeName("a=1,b=2,c=3", 2));
		Assert.assertEquals("b=2",LdapUtils.getNodeName("a=1,b=2,c=3", 1));
	}
	@Test
	public void test_getParent(){
		Assert.assertEquals("b=2,c=3",LdapUtils.getParent("a=1,b=2,c=3"));
	}
	@Test
	public void test_getValue(){
		Assert.assertEquals("1", LdapUtils.getValue("a=1"));
	}
	
	@Test
	public void test_isGroup(){
		Assert.assertTrue(LdapUtils.isGroupSub("a=xxx,ou=group"));
		Assert.assertFalse(LdapUtils.isGroupSub("a=xxx,ou=grdoup"));
	}
	@Test
	public void test_isOrg(){
		Assert.assertTrue(LdapUtils.isOrgSub("a=xxx,ou=org"));
		Assert.assertFalse(LdapUtils.isOrgSub("a=xxx,ou=grdoup"));
	}
	@Test
	public void test_less(){
		Assert.assertEquals("c=4,d=5",LdapUtils.less("a=1,b=2,c=4,d=5", 2));
	}
	@Test
	public void test_updateNameValue(){
		Assert.assertEquals("a=1",LdapUtils.updateNameValue("a=3", "1"));
	}
	@Test
	public void test_removeDangerous(){
		Assert.assertEquals("a({})11b2",LdapUtils.removeDangerous("a=@#$%^&*({})11,b=2"));
	}

}
