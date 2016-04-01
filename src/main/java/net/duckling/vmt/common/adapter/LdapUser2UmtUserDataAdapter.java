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
import java.util.Collection;
import java.util.List;

import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.view.UmtUserData;

/**
 * LdapUser和UmtUserData相互转换器
 * LdapUser，本地数据持久化的User对象
 * UmtUserData，从Umt搜索出来的对象
 * @author lvly
 * @since 2013-5-9
 */
public final class LdapUser2UmtUserDataAdapter {
	private LdapUser2UmtUserDataAdapter(){}
	
	/**
	 * 批量转换，用户显示，统一用
	 * @param users
	 * @return
	 */
	public static List<UmtUserData> convert(Collection<LdapUser> users){
		if(users==null){
			return null;
		}
		List<UmtUserData> result=new ArrayList<UmtUserData>();
		for(LdapUser user:users){
			UmtUserData data=new UmtUserData();
			data.setCstnetId(user.getCstnetId());
			data.setTruename(user.getName());
			data.setUmtId(user.getUmtId());
			data.setMobile(user.getTelephone());
			result.add(data);
		}
		return result;
	}

}
