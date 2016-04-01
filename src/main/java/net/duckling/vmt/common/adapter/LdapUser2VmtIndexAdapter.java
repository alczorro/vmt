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
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapUser;

import org.apache.log4j.Logger;

/**
 * @author lvly
 * @since 2013-7-16
 */
public final class LdapUser2VmtIndexAdapter {
	private static final Logger LOG=Logger.getLogger(LdapUser2VmtIndexAdapter.class);
	private LdapUser2VmtIndexAdapter (){
		
	}
	/**
	 * 转换用户为索引
	 * @param users
	 * @return
	 */
	public static List<VmtIndex> convert(List<LdapUser> users){
		List<VmtIndex> result=new ArrayList<VmtIndex>();
		if(users==null){
			return result;
		}
		for(LdapUser user:users){
			VmtIndex index=new VmtIndex();
			index.setTeamDN(user.getRoot());
			if(CommonUtils.isNull(user.getRoot())){
				user.setRoot(LdapUtils.getTeamDN(user.getDn()));
			}
			if(LdapUtils.isOrgSub(user.getRoot())){
				index.setType(VmtIndex.TYPE_ORG);
			}else if(LdapUtils.isGroupSub(user.getRoot())){
				index.setType(VmtIndex.TYPE_GROUP);
			}
			index.setUmtId(user.getUmtId());
			index.setStatus(user.getStatus());
			index.setSymbol(LdapUtils.getLastValue(user.getRoot()));
			String[] cps=user.getCurrentDisplay().split(",");
			if(!CommonUtils.isNull(cps)){
				index.setTeamName(cps[0]);
			}else{
				LOG.error("the user["+user.getDn()+"]'s index is error,current display name is "+user.getCurrentDisplay());
			}
			index.setUserName(user.getName());
			index.setUserDN(user.getDn());
			index.setVisible(user.isVisible());
			index.setUserCstnetId(user.getCstnetId());
			result.add(index);
		}
		return result;
	}
}
