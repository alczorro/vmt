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
import net.duckling.cloudy.common.DateUtils;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.SearchOrgDomainMappingCondition;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.index.OrgDetail;
import net.duckling.vmt.domain.index.OrgDomain;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.view.SettingNodeData;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IIPFilterService;
import net.duckling.vmt.service.IOrgDomainMappingService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.sms.ISmsService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2013-7-16
 */

@Controller
@RequestMapping("user/gm")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
@SecurityMapping(level=SecurityLevel.SUPER_ADMIN)
public class GMController {
	private static final Logger LOGGER=Logger.getLogger(GMController.class);
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private IIPFilterService ipService;
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private MQMessageSenderExt sender;
	@Autowired
	private IOrgDomainMappingService mappingService;
	@Autowired
	private ISmsService smsService;
	/**
	 * 重建索引，全部
	 * @return
	 */
	@RequestMapping("/buildIndex")
	@ResponseBody
	public boolean rebuildIndex(){
		indexService.buildIndexJob();
		return true;
	}
	
	/**
	 * 重建索引，指定某个
	 * @param dn
	 * @return
	 */
	@RequestMapping(value="/buildAIndex",params={"dn"})
	@ResponseBody
	public boolean rebuildIndex(@RequestParam("dn")String dn){
		indexService.buildAIndexJob(LdapUtils.decode(dn));
		return true;
	}
	

	/**
	 * 显示同步页面
	 * @param user
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		ModelAndView mv = new ModelAndView("user/gm");
		List<LdapOrg> orgs = orgService.getThirdPartyOrgs(null);
		List<LdapGroup> groups = groupService.getThirdPartyGroups(null);
		mv.addObject("nodes", getSettingData(orgs, groups));
		
		return mv;
	}
	@RequestMapping(params="func=domainMapping")
	public ModelAndView domainMappingDisplay(SearchOrgDomainMappingCondition od){
		
		ModelAndView mv= new ModelAndView("user/gm_domainMapping");
		List<LdapOrg> orgs = orgService.getOrgsByOrgDomainMapping(od);
		mv.addObject("orgs", orgs);
		mv.addObject("condition",od);
		return mv;
	}
	@RequestMapping(params="func=smsManage")
	public ModelAndView smsManage(){
		ModelAndView mv=new ModelAndView("/user/gm_smsManage");
		mv.addObject("groups",smsService.getAllSmsGroup());
		return mv;
	}
	
	@RequestMapping(value="/addDomain")
	@ResponseBody
	public boolean addDomain(@RequestParam("dn")String dn,@RequestParam("domain")String domain,@RequestParam("symbol")String symbol){
		if(CommonUtils.isNull(domain)||CommonUtils.isNull(symbol)){
			return false;
		}
		if(mappingService.isDomainExists(CommonUtils.trim(domain))){
			return false;
		}
		String decodeDN=LdapUtils.decode(dn);
		attrService.insert(decodeDN, "vmt-domain", new String[]{CommonUtils.trim(domain)});
		if(!mappingService.isSymbolExists(CommonUtils.trim(symbol))){
			LdapOrg org=orgService.getOrgByDN(decodeDN);
			OrgDetail od=new OrgDetail();
			od.setSymbol(org.getSymbol());
			od.setName(org.getName());
			mappingService.addDetail(od);
			OrgDomain odo=new OrgDomain();
			odo.setOrgId(od.getId());
			odo.setDomain(domain);
			mappingService.addDomain(odo);
		}else{
			mappingService.addDomain(CommonUtils.trim(domain),CommonUtils.trim(symbol));
		}
		
		return true;
	}
	@RequestMapping(value="/removeDomain")
	@ResponseBody
	public boolean removeDomain(@RequestParam("dn")String dn,@RequestParam("domain")String domain,@RequestParam("symbol")String symbol){
		if(CommonUtils.isNull(domain)||CommonUtils.isNull(symbol)){
			return false;
		}
		attrService.delete(LdapUtils.decode(dn),"vmt-domain", new String[]{CommonUtils.trim(domain)});
		mappingService.deleteADomainByDomain(CommonUtils.trim(domain));
		return true;
	}
	
	
	@RequestMapping("/changeIsCas")
	@ResponseBody
	public boolean changeIsCas(@RequestParam("isCas")boolean isCas,@RequestParam("dn")String dn){
		String decodeDN=LdapUtils.decode(dn);
		String symbol=LdapUtils.getLastValue(decodeDN);
		if(!mappingService.isSymbolExists(CommonUtils.trim(symbol))){
			LdapOrg org=orgService.getOrgByDN(decodeDN);
			OrgDetail od=new OrgDetail();
			od.setSymbol(org.getSymbol());
			od.setName(org.getName());
			mappingService.addDetail(od);
		}
		attrService.update(decodeDN, "vmt-is-cas", isCas);
		mappingService.updateIsCas(isCas,symbol);
		return true;
	}
	
	@RequestMapping("/changeIsCoreMail")
	@ResponseBody
	public boolean changeIsCoreMail(@RequestParam("isCoreMail")boolean isCoreMail,@RequestParam("dn")String dn){
		String decodeDN=LdapUtils.decode(dn);
		String symbol=LdapUtils.getLastValue(decodeDN);
		if(!mappingService.isSymbolExists(CommonUtils.trim(symbol))){
			LdapOrg org=orgService.getOrgByDN(decodeDN);
			OrgDetail od=new OrgDetail();
			od.setSymbol(org.getSymbol());
			od.setName(org.getName());
			mappingService.addDetail(od);
		}
		
		attrService.update(decodeDN, "vmt-is-coremail", isCoreMail);
		mappingService.updateIsCoreMail(isCoreMail,symbol);
		return true;
	}
	
	@RequestMapping(params="func=ipList")
	public ModelAndView ipDisplay(){
		ModelAndView mv= new ModelAndView("user/gm_ipList");
		mv.addObject("ips",ipService.asList());
		return mv;
	}
	@RequestMapping(params="func=upgrade")
	public ModelAndView upgrade(){
		List<LdapOrg> orgs = orgService.getThirdPartyOrgs(null);
		List<LdapGroup> groups = groupService.getThirdPartyGroups(null);

		ModelAndView mv=new ModelAndView("user/gm_upgrade");
		mv.addObject("nodes", getSettingData(orgs, groups));
		return mv;
	}
	@RequestMapping("/removeIp")
	@ResponseBody
	public boolean removeIp(@RequestParam("ipId")int id){
		ipService.removeIp(id);
		return true;
	}
	@RequestMapping("/addIp")
	@ResponseBody
	public int addIp(@RequestParam("ip")String ip){
		return ipService.addIpFilter(ip);
	}
	private List<SettingNodeData> getSettingData(List<LdapOrg> orgs,List<LdapGroup> groups){
		List<SettingNodeData> result=new ArrayList<SettingNodeData>();
		if(!CommonUtils.isNull(orgs)){
			for(LdapOrg org:orgs){
				SettingNodeData data=new SettingNodeData();
				data.setData(org);
				data.setDn(org.getDn());
				data.setName(org.getName());
				data.setPassword(org.getPassword());
				data.setUnconfirmVisible(org.isUnconfirmVisible());
				data.setPrivilege(org.getPrivilege());
				data.setMemberVisible(org.isMemberVisible());
				data.setFrom(org.getFrom());
				data.setFromDate(org.getFromDate());
				data.setToDate(org.getToDate());
				data.setSymbol(org.getSymbol());
				data.setGroup(false);
				result.add(data);
			}
		}
		if(!CommonUtils.isNull(groups)){
			for(LdapGroup group:groups){
				SettingNodeData data=new SettingNodeData();
				data.setData(group);
				data.setDn(group.getDn());
				data.setName(group.getName());
				data.setPassword(group.getPassword());
				data.setUnconfirmVisible(group.isUnconfirmVisible());
				data.setPrivilege(group.getPrivilege());
				data.setMemberVisible(group.isMemberVisible());
				data.setFrom(group.getFrom());
				data.setSymbol(group.getSymbol());
				data.setFromDate(group.getFromDate());
				data.setToDate(group.getToDate());
				data.setGroup(true);
				result.add(data);
			}
		}
		return result;
	}
	@RequestMapping("upgrade/save")
	@ResponseBody
	public boolean save(@RequestParam("dn")String dn,@RequestParam("fromDate")String fromDate,@RequestParam("toDate")String toDate){
		updateEvenNull(dn,"vmt-from-date",fromDate);
		updateEvenNull(dn,"vmt-to-date",toDate);
		return true;
	}
	@RequestMapping(value="sendMsg",params="dn")
	@ResponseBody
	public boolean sendMsg(@RequestParam("dn")String dn){
		sender.sendRefreshMessage(dn);
		return true;
	}
	@RequestMapping(value="sendMsg/group/all")
	@ResponseBody
	public boolean sendGroupAll(){
		List<LdapGroup> groups=groupService.getGroupsByMemberVisible();
		sender.sendRefreshGroupMessage(groups);
		LOGGER.info("send message group all success!:"+groups.size());
		return true;
	}
	
	@RequestMapping(value="sendMsg/org/all")
	@ResponseBody
	public boolean sendOrgAll(){
		List<LdapOrg> orgs=orgService.getOrgsByMemberVisible();
		sender.sendRefreshOrgMessage(orgs);
		LOGGER.info("send message orgs all success!:"+orgs.size());
		return true;
	}
	
	private void updateEvenNull(String dn,String key,String value){
		String before=CommonUtils.first(attrService.get(dn, key));
		if(CommonUtils.isNull(value)){
			if(!CommonUtils.isNull(before)){
				attrService.delete(dn, key, new String[]{before});
			}
		}else{
			String newValue=DateUtils.getDate(value+" 00:00:00").getTime()+"";
			attrService.update(dn, key, newValue);
		}
	}

}
