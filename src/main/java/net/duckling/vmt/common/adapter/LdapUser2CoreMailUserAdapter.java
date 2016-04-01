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
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.util.CharUtils;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.PinyinUtils;
import net.duckling.vmt.domain.coremail.CoreMailUser;
import net.duckling.vmt.domain.ldap.LdapUser;

/**
 * @author lvly
 * @since 2013-8-8
 */
public class LdapUser2CoreMailUserAdapter {
	
	public static List<LdapUser> convert(String pdn,List<CoreMailUser> users,String[] umtIds){
		List<LdapUser> result=new ArrayList<LdapUser>();
		if(CommonUtils.isNull(users)){
			return result;
		}
		int index=0;
		String root=LdapUtils.getTeamDN(pdn);
		for(CoreMailUser user:users){
			LdapUser lUser=convert(user,root);
			lUser.setUmtId(umtIds[index++]);
			result.add(lUser);
		}
		return result;
	}
	public static CoreMailUser convert(LdapUser user,String orgSymbol,String deptSymbol,String password){
		CoreMailUser c=new CoreMailUser();
		c.setDomain(EmailUtils.getDomain(user.getCstnetId()));
		c.setEmail(EmailUtils.getNameFromEmail(user.getCstnetId()));
		c.setExpireTime(user.getExpireTime());
		c.setListRank(user.getListRank());
		c.setOrgId(orgSymbol);
		c.setOuId(deptSymbol);
		
		c.setPassword(password);
		c.setVisible(user.isVisible());
		c.setStatus(user.getAccountStatus());
		c.setName(user.getName());
		return c;
	}
	public static LdapUser convert(CoreMailUser user,String root){
		if(user==null){
			return null;
		}
		LdapUser lUser=new LdapUser();
		lUser.setCstnetId(user.getEmail());
		lUser.setName(CommonUtils.isNull(user.getName())?user.getEmail().split("@")[0]:user.getName());
		lUser.setRoot(root);
		lUser.setListRank(user.getListRank());
		lUser.setVisible(user.isVisible());
		lUser.setPinyin(PinyinUtils.getPinyin(lUser.getName()));
		lUser.setCode(getCode(lUser.getPinyin()));
		lUser.setUserFrom(LdapUser.USER_FROM_CORE_MAIL);
		lUser.setStatus(LdapUser.STATUS_ACTIVE);
		lUser.setExpireTime(user.getExpireTime());
		return lUser;
	}
	private static String getCode(String pinyin){
		if(CharUtils.isFirstCharIsLetter(pinyin)){
			return pinyin.substring(0,1).toLowerCase();
		}
		return "#";
	}

}
