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
package net.duckling.vmt.web.helper;

import java.util.Collections;
import java.util.List;

import net.duckling.common.util.CommonUtils;
import net.duckling.falcon.api.serialize.JSONMapper;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.view.AdminData;
import net.duckling.vmt.domain.view.SettingNodeData;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IPrivilegeService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2013-8-13
 */
@Service
public class IndexHelper {
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IPrivilegeService privilegeService;
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IsUpgradeHelper helper;
	
	
	private static final String CURRENT_COUNT="currentCount";
	@Autowired
	private IVmtIndexService indexService;
	
	private String getFirstDn(List<LdapOrg> orgs,List<LdapGroup> groups,ModelAndView mv,String dn){
 		String decodeDN=LdapUtils.decode(dn);
		if(decodeDN==null&&!CommonUtils.isNull(orgs)){
			LdapOrg org=CommonUtils.first(orgs);
			decodeDN=org.getDn();
			if(mv.getViewName().equals("/user/index")){
				mv.setViewName("/user/index2");
			}
			mv.addObject("currOrg",org);
			
		}
		if(decodeDN==null&&!CommonUtils.isNull(groups)){
			decodeDN=CommonUtils.first(groups).getDn();
		}
		return decodeDN;
	}
	//顺便拿出来当前选中组织
	private SettingNodeData getDetailInfo(List<LdapOrg> orgs,List<LdapGroup> groups,String dn,ModelAndView mv){
		SettingNodeData data=new SettingNodeData();
		if(!CommonUtils.isNull(orgs)){
			for(LdapOrg org:orgs){
				if(org.getDn().equals(dn)){
					mv.addObject("currOrg",org);
					mv.addObject("currTeam",org);
					mv.addObject("currTeamJson",JSONMapper.getJSONString(org));
					data.setGroup(false);
					data.setName(org.getName());
					data.setCount(org.getCount());
					data.setFrom(org.getFrom());
					data.setAdmins(setAdmins(org.getAdmins(),org.getDn()));
					return data;
				}
			}
		}
		if(!CommonUtils.isNull(groups)){
			for(LdapGroup group:groups){
				if(group.getDn().equals(dn)){
					mv.addObject("currTeam",group);
					mv.addObject("currTeamJson",JSONMapper.getJSONString(group)); 
					data.setGroup(true);
					data.setName(group.getName());
					data.setCount(group.getCount());
					data.setAdmins(setAdmins(group.getAdmins(),group.getDn()));
					data.setFrom(group.getFrom());
					return data;
				}
			}
		}
		return data;
	}
	private AdminData[] setAdmins(String[] umtIds,String dn){
		if(CommonUtils.isNull(umtIds)){
			return null;
		}
		List<LdapUser> users=userService.searchUsersByUmtId(dn, umtIds);
		AdminData[] result=new AdminData[users.size()];
		int index=0;
		for(LdapUser user:users){
			result[index++]=new AdminData(user.getUmtId(), user.getName(),user.getCstnetId());
		}
		return result;
		
	}
	
	
	/**
	 * 获得全部的个数
	 * */
	private int getAllCount(List<LdapOrg> orgs,List<LdapGroup> groups,String umtId,ModelAndView mv,String currentDN){
		indexService.setGroupCount(groups, umtId);
		indexService.setOrgCount(orgs, umtId);
		int count=getOrgCount(orgs, mv, currentDN)+getGroupCount(groups, mv, currentDN);
		if(mv.getModel().get(CURRENT_COUNT)==null){
			mv.addObject(CURRENT_COUNT,count);
		}
		return count;
	}
	private int getOrgCount(List<LdapOrg> orgs,ModelAndView mv,String currentDN){
		int count=0;
		if(!CommonUtils.isNull(orgs)){
			for(LdapOrg org:orgs){
				if(org.getDn().equals(currentDN)){
					mv.addObject(CURRENT_COUNT,org.getCount());
				}
				count+=org.getCount();
			}
		}
		return count;
	}
	private int getGroupCount(List<LdapGroup> groups,ModelAndView mv,String currentDN){
		int count=0;
		if(!CommonUtils.isNull(groups)){
			for(LdapGroup group:groups){
				if(group.getDn().equals(currentDN)){
					mv.addObject(CURRENT_COUNT,group.getCount());
				}
				count+=group.getCount();
			}
		}
		return count;
	}
	public ModelAndView getModelView(VmtSessionUser user,String viewName,String dn){
		boolean isSuperAdmin=user.getIsSuperAdmin();
		String umtId=isSuperAdmin?null:user.getUserInfo().getUmtId();
		List<LdapOrg> orgs=orgService.getMyOrgs(umtId);
		List<LdapGroup> groups=groupService.getMyGroups(umtId);
		if(orgs!=null){
			Collections.sort(orgs);
		}
		if(groups!=null){
			Collections.sort(groups);
		}
		ModelAndView mv=new ModelAndView(viewName);
		String decodeDN=getFirstDn(orgs, groups, mv, dn);
		mv.addObject("isAdmin",false);
		mv.addObject("canAdd",false);
		mv.addObject("isCurrAdmin",false);
		mv.addObject("isSuperAdmin",isSuperAdmin);
		if(decodeDN!=null){
			boolean isCurrAdmin=privilegeService.isAdmin(decodeDN, user.getUserInfo().getUmtId());
			boolean isAdmin=isSuperAdmin||isCurrAdmin;
			mv.addObject("isAdmin",isAdmin);
			mv.addObject("isCurrAdmin",isCurrAdmin);
			mv.addObject("canAdd",isSuperAdmin||privilegeService.canAdd(decodeDN, umtId));
		}
		
		mv.addObject("domain",user.getCoreMailDomain());
		mv.addObject("dn",decodeDN);
		
		mv.addObject("orgs",orgs);
		mv.addObject("groups",groups);
		mv.addObject("count",getAllCount(orgs, groups,umtId,mv,decodeDN));
		//mv.addObject("isUpgraded",helper.isUpgraded(decodeDN));
		mv.addObject("isUpgraded",true);
		mv.addObject("isGroup",decodeDN==null?"false":LdapUtils.isGroupDN(decodeDN));
		mv.addObject("teamInfo",getDetailInfo(orgs,groups,decodeDN,mv));
		return mv;
	}


}
