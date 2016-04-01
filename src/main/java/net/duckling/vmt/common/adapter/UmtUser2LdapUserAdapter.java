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

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.util.CharUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.PinyinUtils;
import net.duckling.vmt.domain.ldap.LdapUser;
import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;

/**
 * UmtUser和LdapUser相互转换器
 * UmtUser umt api提供的用户数据封装的对象
 * LdapUser 本地数据持久化User对象
 * @author lvly
 * @since 2013-5-3
 */
public final class UmtUser2LdapUserAdapter {
	private static final int PASS_LENGTH=40;
	private UmtUser2LdapUserAdapter(){};
	/**
	 * 批量转换，
	 * @param users 数据
	 * @param dn 初始化的dn，
	 * @param status LdapUser.STATUS_XX 用户创建后的是否激活状态
	 * @param listlink 
	 * @return
	 */
	public static List<LdapUser> convert(List<UMTUser> users,String dn, String status){
		List<LdapUser> result=new ArrayList<LdapUser>();
		if(CommonUtils.isNull(users)){
			return result;
		}
		String root=LdapUtils.getTeamDN(dn);
		for(UMTUser user:users){
			result.add(convert(user,root,status));
		}
		return result;
	}
	/**
	 * 单个转换
	 * @param user 数据
	 * @param root 用户所属部门
	 * @param status LdapUser.STATUS_XX  用户激活状态
	 * @return
	 */
	public static LdapUser convert(UMTUser user,String root,String status){
		LdapUser ldapUser=new LdapUser();
		ldapUser.setCstnetId(user.getCstnetId());
		ldapUser.setName(user.getTruename());
		ldapUser.setUmtId(user.getUmtId());
		ldapUser.setPinyin(PinyinUtils.getPinyin(user.getTruename()));
		ldapUser.setRoot(root);
		ldapUser.setCode(LdapUser.getCode(ldapUser.getPinyin()));
		ldapUser.setRandom(CharUtils.random(PASS_LENGTH));
		ldapUser.setStatus(status);
		ldapUser.setUserFrom(fromWhere(user));
		return ldapUser;
	}
	private static String fromWhere(UMTUser user){
		if("uc".equals(user.getAuthBy())||"coreMail".equals(user.getAuthBy())){
			return LdapUser.USER_FROM_CORE_MAIL;
		}
		return LdapUser.USER_FROM_UMT;
	}
	/**
	 * 转换
	 * @param users 数据
	 * @return
	 */
	public static List<UMTUser> convert(List<LdapUser> users){
		List<UMTUser> result=new ArrayList<UMTUser>();
		if(CommonUtils.isNull(users)){
			return result;
		}
		for(LdapUser user:users){
			result.add(convert(user));
		}
		return result;
	}
	/**
	 * 把本地
	 * @param user
	 * @return
	 */
	public static UMTUser convert(LdapUser user){
		UMTUser umtUser=new UMTUser();
		umtUser.setCstnetId(user.getCstnetId());
		umtUser.setTruename(user.getName());
		umtUser.setUmtId(user.getUmtId());
		return umtUser;
	}
	

}
