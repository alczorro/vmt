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
package net.duckling.vmt.service.thread;

import java.util.ArrayList;
import java.util.List;

import net.duckling.vmt.domain.coremail.CoreMailUser;

/**
 * @author lvly
 * @since 2013-8-6
 */
public class BatchCreateUser{
	private String pdn;
	private List<CoreMailUser> users=new ArrayList<CoreMailUser>();
	public String getPdn() {
		return pdn;
	}
	public void setPdn(String pdn) {
		this.pdn = pdn;
	}
	public List<CoreMailUser> getUsers() {
		return users;
	}
	public void addUser(CoreMailUser user) {
		this.users .add(user);
	}
	
}