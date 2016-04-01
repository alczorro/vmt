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
package net.duckling.vmt.web;

import java.util.ArrayList;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.adapter.LdapUser2UmtUserDataAdapter;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.view.ListView;
import net.duckling.vmt.domain.view.UmtUserData;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgDomainMappingService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IPrivilegeService;
import net.duckling.vmt.service.ISearchService;
import net.duckling.vmt.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.vlabs.umt.oauth.UserInfo;

/**
 * 按DN也就是定位到部门以后的搜索
 * @author lvly
 * @since 2013-5-9
 */
@Controller
@RequestMapping("/user/search/dn")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class SearchDNController {
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ISearchService searchService;
	@Autowired
	private IPrivilegeService priService;
	@Autowired
	private IOrgDomainMappingService domainService;
	
	/**
	 * 迭代的查询树形目录，用户
	 * @param user
	 * @param keyword
	 * @param dn
	 * @return
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<UmtUserData> searchTree(
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,
			@RequestParam("q")String keyword,
			@RequestParam("dn")String dn,
			@RequestParam("from")String from){
		
		String decodeDN=LdapUtils.decode(dn);
		String teamDN=LdapUtils.getTeamDN(decodeDN);
		if(!userService.isExistsSubTree(teamDN, user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId(),true)){
			return null;
		}
		if(!user.getIsSuperAdmin()&&!priService.canLook(teamDN, user.getUserInfo().getUmtId())){
			return null;
		}
		String umtId= user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId();
		
		if(LdapUtils.isOrgDN(decodeDN)){
			LdapOrg org=orgService.getOrgByDN(decodeDN);
			if(org.getIsCoreMail()){
				String[] domains=org.getDomains();
				if("addMember".equals(from)){
					domains=null;
				}
				return LdapUser2UmtUserDataAdapter.convert(userService.searchUsersByKeyword(keyword, new String[]{decodeDN}, domains, umtId));
			}
			
		}
		return LdapUser2UmtUserDataAdapter.convert(userService.searchUsersByKeyword(keyword,new String[]{decodeDN},umtId));
		
	}
	
	/**
	 * 主页展示用
	 * */
	@RequestMapping("/list")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.VIEW)
	public List<ListView> searchList(@RequestParam("dn")String dn,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		String decodeDN=LdapUtils.decode(dn);
		if(decodeDN.equals("all")){
			return new ArrayList<ListView>();
		}
		return searchService.searchByList(decodeDN,user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId());
	}
	/**
	 * 移动人的时候用
	 * */
	@RequestMapping("/list/depart")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.VIEW)
	public List<ListView> searchDepartList(
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,
			@RequestParam("dn")String dn){
		if(dn.equals("all")){
			return (getRootListView(user.getUserInfo()));
		}
		String decodeDN=LdapUtils.decode(dn);
		return (searchService.searchDepartByList(decodeDN));
	}
	
	private List<ListView> getRootListView(UserInfo userInfo){
		List<ListView> result=new ArrayList<ListView>();
		List<LdapOrg> orgs=orgService.getAdminOrgs(userInfo.getUmtId());
		if(!CommonUtils.isNull(orgs)){
			for(LdapOrg org:orgs){
				ListView view=new ListView();
				view.setDn(org.getDn());
				view.setName(org.getName());
				view.setType(ListView.TYPE_DEPART);
				result.add(view);
			}
		}
		List<LdapGroup> groups=groupService.getAdminGroups(userInfo.getUmtId());
		if(!CommonUtils.isNull(groups)){
			for(LdapGroup group:groups){
				ListView view=new ListView();
				view.setDn(group.getDn());
				view.setName(group.getName());
				view.setType(ListView.TYPE_DEPART);
				result.add(view);
			}
		}
		return result;
	}
}
