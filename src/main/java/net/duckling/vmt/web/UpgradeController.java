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

import java.util.Date;

import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2013-8-9
 */
@RequestMapping("user/upgrade")
@Controller
@SecurityMapping(level=SecurityLevel.ADMIN)
public class UpgradeController {
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	@RequestMapping
	public ModelAndView show(@RequestParam("dn")String dn){
		String decodedDn=LdapUtils.decode(dn);
		decodedDn=LdapUtils.getTeamDN(decodedDn);
		ModelAndView mv=new ModelAndView("upgrade");
		isUpgraded(decodedDn, mv);
		return mv;
	}
	private void isUpgraded(String dn,ModelAndView mv){
		long before=0;
		long after=0;
//		long now=System.currentTimeMillis();
		if(LdapUtils.isGroupDN(dn)){
			LdapGroup group=groupService.getGroupByDN(dn);
			before=group.getFromDate();
			after=group.getToDate();
		}else if(LdapUtils.isOrgDN(dn)){
			LdapOrg org=orgService.getOrgByDN(dn);
			before=org.getFromDate();
			after=org.getToDate();
		}
		boolean isUpgrade=true;
//		if(before==0&&after==0){
//			isUpgrade= false;
//		}else if(now>before&&now<after){
//			isUpgrade=  true;
//		}else if(now>before&&after==0){
//			isUpgrade=  true;
//		}else{
//			isUpgrade=  false;
//		}
		mv.addObject("dn",dn);
		mv.addObject("from",before==0?null:new Date(before));
		mv.addObject("to",after==0?null:new Date(after));
		mv.addObject("isUpgrade",isUpgrade);
	}
}
