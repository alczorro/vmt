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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.vmt.domain.ApplyMessage;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapUser;

/**
 * @author lvly
 * @since 2013-9-22
 */
public class VmtIndex2ApplyMessageAdapter {
	public static final Collection<ApplyMessage> convert(List<VmtIndex> indexs){
		Map<String,ApplyMessage> map=new HashMap<String,ApplyMessage>();
		if(indexs==null){
			return map.values();
		}
		for(VmtIndex index:indexs){
			ApplyMessage msg=map.get(index.getTeamDN());
			LdapUser user=new LdapUser();
			user.setDn(index.getUserDN());
			user.setName(index.getUserName());
			user.setUmtId(index.getUmtId());
			if(msg==null){
				msg=new ApplyMessage();
				msg.setGroupDn(index.getTeamDN());
				msg.setGroupName(index.getTeamName());
				map.put(msg.getGroupDn(), msg);
			}
			msg.addUsers(user);
		}
		return map.values();
	}

}
