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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.view.SearchLetterData;

/**
 * LdapUser和SearchLetterData相互转换类
 * LdapUser，本地数据库持久化对象
 * SearchLetterData 用字母视图搜索的搜索结果，分别以拼音的首字母排序，如果首字母不是字母，则归类于‘#’号
 * @author lvly
 * @since 2013-5-7
 */
/**
 * @author lvly
 * @since 2013-6-4
 */
public final class LdapUser2SearchLetterDataAdapter{
	private Map<String,List<LdapUser>> cache=new HashMap<String,List<LdapUser>>();
	private List<SearchLetterData> data=new ArrayList<SearchLetterData>();
	private static final int LETTER_A=97;
	private static final int LETTER_Z=123;
	
	/**
	 * 构造，这里发生归类
	 * @param users
	 */
	public LdapUser2SearchLetterDataAdapter(List<LdapUser> users){
		for(char i=LETTER_A;i<LETTER_Z;i++){
			cache.put(String.valueOf(i), new ArrayList<LdapUser>());
		}
		cache.put("#", new ArrayList<LdapUser>());
		if(CommonUtils.isNull(users)){
			return;
		}
		for(LdapUser user:users){
			add(user.getCode(), user);
		}
		for(String key:this.cache.keySet()){
			this.data.add(new SearchLetterData(cache.get(key),key));
		}
	}
	/**
	 * 返回归类好的字母
	 * @param letter， 字母，前台传的，如果是非字母则会是other，其他是a-z
	 * */
	public List<SearchLetterData> getData(String letter){
		String key=letter.equals("other")?"#":letter;
		List<SearchLetterData> result=new ArrayList<SearchLetterData>();
		for(SearchLetterData tmp:data){
			if(tmp.getLetter().equals(key)){
				result.add(tmp);
				return result;
			}
		}
		return result;
		
	}
	
	/**
	 * 构造时组织数据，调用此方法，返回组织好的数据
	 * @return
	 */
	public List<SearchLetterData> getData(){
		return this.data;
	}
	private void add(String letter,LdapUser user){
		List<LdapUser> users=cache.get(letter);
		users.add(user);
	}
	
}
