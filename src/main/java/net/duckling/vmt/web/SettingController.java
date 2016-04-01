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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.adapter.Collection2ArrayAdapter;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.view.AdminData;
import net.duckling.vmt.domain.view.SettingNodeData;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.web.helper.IsUpgradeHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;

/**
 * 设置页面的逻辑都在这里
 * @author lvly
 * @since 2013-5-10
 */
@Controller
@RequestMapping("/user/setting")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class SettingController {
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAttributeService attributeService;
	@Autowired
	private UserService umtService;
	@Autowired
	private MQMessageSenderExt sender;
	
	@Autowired
	private IsUpgradeHelper upgradeHelper;
	/**
	 * 显示设置
	 * @param userInfo 登录用户信息
	 * */
	@RequestMapping
	public ModelAndView display(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,HttpServletRequest request){
		String viewName=request.getParameter("view");
		if(CommonUtils.isNull(viewName)){
			viewName="/user/setting";
		}else if("appmanage".equalsIgnoreCase(viewName)){
			viewName="/user/setting_appmanage";
		}else if("ldap".equalsIgnoreCase(viewName)){
			viewName="/user/setting_ldap";
		}else if("prev".equals(viewName)){
			viewName="/user/setting_prev";
		}else if("share".equals(viewName)){
			viewName="/user/setting_share";
		}else if("batch".equals(viewName)){
			viewName="/user/setting_batch";
		}else{
			viewName="/user/setting";
		}
		List<LdapOrg> orgs=orgService.getAdminOrgs(user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId());
		List<LdapGroup> groups=groupService.getAdminGroups(user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId());
		ModelAndView mv=new ModelAndView(viewName);
		getSettingData(mv,orgs,groups,user.getUserInfo().getUmtId());
		return mv;
	}
	/**
	 * 增加管理员
	 * @param 
	 * */
	@RequestMapping("admin/add/{umtId}")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.ADMIN)
	public boolean addAdmin(@RequestParam("dn")String dn,@PathVariable("umtId")String umtId){
		String decodeUser=LdapUtils.decode(dn);
		boolean flag= userService.addAdmin(decodeUser,umtId);
		sender.sendUpdateMessage(decodeUser);
		return flag;
	}
	/**
	 * 删除管理员
	 * @param
	 * */
	@RequestMapping("admin/delete/{id}")
	@SecurityMapping(level=SecurityLevel.ADMIN)
	@ResponseBody
	public boolean deleteAdmin(@RequestParam("dn")String dn,@PathVariable("id")String umtId,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(umtId.equals(user.getUserInfo().getUmtId())){
			return false;
		}
		String decodeDn=LdapUtils.decode(dn);
		userService.removeAdmin(decodeDn,umtId);
		sender.sendUpdateMessage(decodeDn);
		return true;
	}
	/**
	 * 更改权限
	 * @param dn
	 * @param attrName
	 * @param value
	 * @return
	 */
	@RequestMapping("privilege/{attrName}/{value}")
	@SecurityMapping(level=SecurityLevel.ADMIN)
	@ResponseBody
	public boolean changePrivilege(@RequestParam("dn")String dn,@PathVariable("attrName")String attrName,@PathVariable("value")String value) {
		String decode=LdapUtils.decode(dn);
		attributeService.update(decode, attrName, value);
		sender.sendUpdateMessage(decode);
		if ("vmt-hide-mobile".equals(attrName)){
			sender.sendToggleMobileMessage(decode, !Boolean.parseBoolean(value));
		}
		return true;
	}
	/**
	 * 一次请求所有的管理员的账户密码键值对
	 * @param orgs
	 * @param groups
	 * @return
	 */
	private Map<String,String> arrangeUmtId(List<LdapOrg> orgs,List<LdapGroup> groups){
		Set<String> result=new HashSet<String>();
		if(!CommonUtils.isNull(orgs)){
			for(LdapOrg org:orgs){
				if(CommonUtils.isNull(org.getAdmins())){
					continue;
				}
				for(String umtId:org.getAdmins()){
					result.add(umtId);
				}
			}
		}
		if(!CommonUtils.isNull(groups)){
			for(LdapGroup group:groups){
				if(CommonUtils.isNull(group.getAdmins())){
					continue;
				}
				for(String umtId:group.getAdmins()){
					result.add(umtId);
				}
			}
		}
		Map<String,String> map=new HashMap<String,String>();
		List<UMTUser> users=umtService.getUMTUsers(Collection2ArrayAdapter.convert(result));
		for(UMTUser user:users){
			map.put(user.getUmtId(), user.getTruename());
		}
		return map;
	}
	private List<SettingNodeData> getSettingData(ModelAndView mv,List<LdapOrg> orgs,List<LdapGroup> groups,String umtId){
		List<SettingNodeData> result=new ArrayList<SettingNodeData>();
		List<SettingNodeData> iamAdminResult=new ArrayList<SettingNodeData>();
		Map<String,String> map=arrangeUmtId(orgs, groups);
		if(!CommonUtils.isNull(orgs)){
			for(LdapOrg org:orgs){
				SettingNodeData data=new SettingNodeData();
				data.setAdmins(getAdmins(org.getAdmins(),map));
				data.setDn(org.getDn());
				data.setName(org.getName());
				data.setPassword(org.getPassword());
				data.setUnconfirmVisible(org.isUnconfirmVisible());
				data.setPrivilege(org.getPrivilege());
				data.setMemberVisible(org.isMemberVisible());
				data.setFrom(org.getFrom());
				data.setSymbol(org.getSymbol());
				data.setLogoId(org.getLogoId());
				data.setUpgraded(upgradeHelper.isUpgraded(org.getFromDate(),org.getToDate()));
				data.setMobileLogo(org.getMobileLogo());
				data.setPcLogo(org.getPcLogo());
				data.setData(org);
				result.add(data);
				if(org.isAdmin(umtId)){
					iamAdminResult.add(data);
				}
			}
		}
		if(!CommonUtils.isNull(groups)){
			for(LdapGroup group:groups){
				SettingNodeData data=new SettingNodeData();
				data.setAdmins(getAdmins(group.getAdmins(),map));
				data.setDn(group.getDn());
				data.setName(group.getName());
				data.setPassword(group.getPassword());
				data.setUnconfirmVisible(group.isUnconfirmVisible());
				data.setPrivilege(group.getPrivilege());
				data.setMemberVisible(group.isMemberVisible());
				data.setFrom(group.getFrom());
				data.setSymbol(group.getSymbol());
				data.setLogoId(group.getLogoId());
				data.setGroup(true);
				data.setUpgraded(upgradeHelper.isUpgraded(group.getFromDate(),group.getToDate()));
				data.setMobileLogo(group.getMobileLogo());
				data.setPcLogo(group.getPcLogo());
				data.setData(group);
				result.add(data);
				if(group.isAdmin(umtId)){
					iamAdminResult.add(data);
				}
			}
		}
		mv.addObject("nodes",result);
		mv.addObject("iamAdminResult",iamAdminResult);
		return result;
	}
	private AdminData[] getAdmins(String[] umtIds,Map<String,String> map){
		if(CommonUtils.isNull(umtIds)){
			return null;
		}
		AdminData[] datas=new AdminData[umtIds.length];
		int index=0;
		for(String umtId:umtIds){
			datas[index++]=new AdminData(umtId,map.get(umtId));
		}
		return datas;
	}
}
