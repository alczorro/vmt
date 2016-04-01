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
package net.duckling.vmt.domain.view;

import java.io.Serializable;
import java.util.List;

import net.duckling.vmt.domain.ldap.LdapUser;

/**
 * 用于用字母定位的视图，index1
 * @author lvly
 * @since 2013-5-7
 */
public class SearchLetterData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2955815976554930467L;
	/**
	 * 
	 */
	
	/**
	 * 用户数据
	 */
	private List<LdapUser> data;
	/**
	 * 当前定位到的字母
	 */
	private String letter;
	
	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	/**
	 * @param searchAllUserByLetter
	 */
	public SearchLetterData(List<LdapUser> data,String letter) {
		this.data=data;
		this.letter=letter;
	}

	public List<LdapUser> getData() {
		return data;
	}

	public void setData(List<LdapUser> data) {
		this.data = data;
	}

	
}
