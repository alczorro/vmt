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
package net.duckling.vmt.web;

import java.util.ArrayList;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.falcon.api.cache.ICacheService;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtApp;
import net.duckling.vmt.domain.VmtAppProfile;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IVmtAppProfileService;
import net.duckling.vmt.service.IVmtAppService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/profile")
@SecurityMapping
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class ProfileController {
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IVmtAppService appService;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private IVmtAppProfileService proService;
	
	private static final String CACHE_KEY="my.tmp.apps.";
	@RequestMapping
	public ModelAndView display(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user) {
		String umtId=user.getUserInfo().getUmtId();
		List<VmtApp> apps=appService.searchAppByTeamDnsNoDistinct(getDNs(umtId));
		cacheService.set(CACHE_KEY+umtId, apps);
		ModelAndView mv=new ModelAndView("/user/profile");
		mv.addObject("apps",appService.distinctApp(apps));
		return mv;
	}
	@RequestMapping("change")
	@ResponseBody
	public JsonResult change(@RequestParam("isDisplay")boolean isDisplay,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,@RequestParam("appType")String appType,@RequestParam("appKey")String appKey){
		VmtAppProfile pro=new VmtAppProfile();
		pro.setAppType(appType);
		pro.setFkId(appKey);
		pro.setUmtId(user.getUserInfo().getUmtId());
		pro.setCstnetId(user.getUserInfo().getCstnetId());
		pro.setValue(isDisplay?VmtAppProfile.VALUE_SHOW:VmtAppProfile.VALUE_HIDE);
		proService.update(pro);
		return new JsonResult();
	}
	@RequestMapping("getAppInfo")
	@ResponseBody
	public JsonResult getTeamsAndStatus(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,@RequestParam("appType")String appType,@RequestParam("appKey")String appKey){
		List<VmtApp> apps=(List<VmtApp>)cacheService.get(CACHE_KEY+user.getUserInfo().getUmtId());
		String umtId=user.getUserInfo().getUmtId();
		if(apps==null){
			apps=appService.distinctApp(appService.searchAppByTeamDnsNoDistinct(getDNs(umtId)));
			cacheService.set(CACHE_KEY+umtId,apps);
		}
		boolean isOauth=VmtApp.APP_TYPE_OAUTH.equals(appType);
		
		List<String> checkedApps=new ArrayList<String>();
		for(VmtApp app:apps){
			if(isOauth){
				if(appKey.equals(app.getAppClientId())){
					checkedApps.add(app.getTeamDn());
				}
			}else{ 
				if(appKey.equals(app.getId()+"")){
					checkedApps.add(app.getTeamDn());
				}
			}
		}
		List<String> teamName=new ArrayList<String>();
		for(String teamDN:checkedApps){
			teamName.add(CommonUtils.first(attrService.get(teamDN, "vmt-name")));
		}
		JSONObject obj=new JSONObject();
		obj.put("teamName", teamName);
		obj.put("status", proService.getStatus(umtId, appKey, appType));
		JsonResult result=new JsonResult();
		result.setData(obj);
		return result;
	}
	
	private List<String> getDNs(String umtId){
		List<LdapOrg> orgs=orgService.getMyOrgs(umtId);
		List<String> orgDNS=LdapOrg.extractAppOpenOrgId(orgs);
		List<LdapGroup> groups=groupService.getMyGroups(umtId);
		List<String> groupDNS=LdapGroup.extractAppOpenGroupId(groups);
		List<String> dns= merge(orgDNS,groupDNS);
		cacheService.set(CACHE_KEY+umtId,dns);
		return dns;
	}
	private List<String> merge(List<String> orgDns,List<String> groupDns){
		List<String> result=new ArrayList<String>();
		if(!CommonUtils.isNull(orgDns)){
			for(String dn:orgDns){
				result.add(dn);
			}
		}
		if(!CommonUtils.isNull(groupDns)){
			for(String dn:groupDns){
				result.add(dn);
			}
		}
		return result;
	}
}
