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

import junit.framework.Assert;
import net.duckling.vmt.test.BuildData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lvly
 * @since 2013-6-7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-root-context.xml")
public class CommonDAOLdapImplTest {
	
	private String dn;
	@Before
	public void readyData(){
		dn=BuildData.resetGroup();
	}
	@Autowired
	private ICommonDAO commonDAO;
	
	@Test
	public void testIsExist(){
		//真的已存在的
		Assert.assertTrue(commonDAO.isExist(dn));
		commonDAO.unbind(dn);
		//随便写的，不存在的
		Assert.assertFalse(commonDAO.isExist(dn));
	}
	
}
