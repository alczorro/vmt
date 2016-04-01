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

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ISearchService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.web.helper.IndexHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2013-7-31
 */
@Controller
@RequestMapping("/user/mobile")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class MobileController {
	@Autowired
	private IndexHelper index;
	@Autowired
	private ISearchService searchService;
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private IUserService userService;
	
	@RequestMapping
	public ModelAndView display(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		return index.getModelView(user, "mobileIndex", null);
	}
	@SecurityMapping
	@RequestMapping("detail")
	public ModelAndView detail(@RequestParam("dn")String dn,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(CommonUtils.isNull(dn)){
			return display(user);
		}
		String decodeDN=LdapUtils.decode(dn);
		ModelAndView mv=new ModelAndView("mobileDetail");
		addModelAttribute(mv, decodeDN,false);
		mv.addObject("views",searchService.searchByList(decodeDN, user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId()));
		
		return mv;
	}
	private void addModelAttribute(ModelAndView mv,String dn,boolean isUser){
		mv.addObject("dn",dn);
		if(new DistinguishedName(dn).size()>2){
			mv.addObject("prev",LdapUtils.minus(dn, 1));
		}
		String[] display=attrService.get(dn, "vmt-current-display")[0].split(",");
		mv.addObject("parentDisplay",getParentDisplay(display,isUser));
		mv.addObject("currentDisplay",getCurrentDisplay(dn,display,isUser));
		mv.addObject("isGroup",LdapUtils.isGroupSub(dn));
	}
	@SecurityMapping
	@RequestMapping("detailUser")
	public ModelAndView detailUser(@RequestParam("dn")String dn,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		String decodeDN=LdapUtils.decode(dn);
		LdapUser detailUser=userService.getUserByDN(decodeDN);
		ModelAndView mv=new ModelAndView("mobileUserDetail");
		mv.addObject("user",detailUser);
		addModelAttribute(mv, decodeDN,true);
		return mv;
	}
	@RequestMapping("createTeam")
	public ModelAndView createTeam(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		ModelAndView mv=new ModelAndView("mobileCreate");
		return mv;
	}
	
	
	private String getCurrentDisplay(String dn,String[] displays,boolean isUser){
		if(isUser){
			return attrService.get(dn, "vmt-name")[0];
		}else{
			return displays[displays.length-1];
		}
		
	}
	private String getParentDisplay(String[] displays,boolean isUser){
		if(!isUser&&displays.length<2){
			return "首页";
		}
		return displays[displays.length-(isUser?1:2)];
	}
}
