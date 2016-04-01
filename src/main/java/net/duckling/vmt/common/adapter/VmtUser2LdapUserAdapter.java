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
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.domain.ldap.LdapUser;

/**
 * VmtUser和LdapUser相互转换类
 * VmtUser 用于api提供的数据封装类
 * LdapUser 用户本地数据持久化的用户对象
 * @author lvly
 * @since 2013-5-21
 */
public final class VmtUser2LdapUserAdapter {
	private VmtUser2LdapUserAdapter(){}
	/**
	 * 转换
	 * @param user
	 * @return
	 */
	public static VmtUser convert(LdapUser user){
		if(user==null||!LdapUser.STATUS_ACTIVE.equals(user.getStatus())){
			return null;
		}
		VmtUser vmtUser=new VmtUser();
		vmtUser.setCstnetId(user.getCstnetId());
		vmtUser.setCurrentDisplay(user.getCurrentDisplay());
		vmtUser.setDn(user.getDn());
		vmtUser.setName(user.getName());
		vmtUser.setStatus(user.getStatus());
		vmtUser.setUmtId(user.getUmtId());
		vmtUser.setListRank(user.getListRank());
		vmtUser.setUserFrom(user.getUserFrom());
		vmtUser.setAccountStatus(user.getAccountStatus());
		
		vmtUser.setOffice(user.getOffice());
		vmtUser.setOfficePhone(user.getOfficePhone());
		vmtUser.setSex(user.getSex());
		vmtUser.setTitle(user.getTitle());
		vmtUser.setTelephone(user.getTelephone());
		vmtUser.setVisible(user.isVisible());
		
		vmtUser.setDisableDchat(user.isDisableDchat());
		vmtUser.setCustom1(user.getCustom1());
		vmtUser.setCustom2(user.getCustom2());
		
		return vmtUser;
	}
	/**
	 * 批量转换
	 * @param users
	 * @return
	 */
	public static List<VmtUser> convert(List<LdapUser> users){
		List<VmtUser> result=new ArrayList<VmtUser>();
		if(CommonUtils.isNull(users)){
			return result;
		}
		for(LdapUser user:users){
			if(user==null||!LdapUser.STATUS_ACTIVE.equals(user.getStatus())){
				continue;
			}
			result.add(convert(user));
		}
		return result;
	}

}
