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
package net.duckling.vmt.api.impl;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import cn.vlabs.rest.ServiceException;

public class OrgServiceTest {

	@Test
	public void test() throws ServiceException {
		OrgService service = new OrgService("http://localhost/vmt7/services");
		Map<String,String> result=service.searchUserAttribute("vmt-symbol=cstnet,ou=org", "telephone");
		assertNotNull(result);
		assertTrue(result.size()>0);
	}

}
