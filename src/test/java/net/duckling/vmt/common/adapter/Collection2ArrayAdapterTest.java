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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author lvly
 * @since 2013-6-4
 */
public class Collection2ArrayAdapterTest {
	@Test
	public void test1(){
		List<String> list=new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		String result[]=Collection2ArrayAdapter.convert(list);
		Assert.assertEquals(result.length, 3);
		Assert.assertEquals("1", result[0]);
		Assert.assertEquals("2", result[1]);
		Assert.assertEquals("3", result[2]);
	}
	@Test
	public void test2(){
		Set<String> set=new HashSet<String>();
		set.add("1");
		set.add("2");
		set.add("3");
		List<String> str=Collection2ArrayAdapter.convert(set);
		Assert.assertEquals(3, str.size());
		Assert.assertEquals("3", str.get(0));
		Assert.assertEquals("2", str.get(1));
		Assert.assertEquals("1", str.get(2));
	}
}
