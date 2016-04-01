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


import java.util.HashMap;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.RenderUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.SimpleEmail;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.view.ListView;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ICommonService;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.INodeService;
import net.duckling.vmt.service.IOrgDomainMappingService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.service.thread.EmailSendJob;
import net.duckling.vmt.service.thread.JobThread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 一些跟数据对象无关的操作
 * @author lvly
 * @since 2013-5-15
 */
@Controller
@RequestMapping("/user/common")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class CommonController {
	public static final String HCOLUMN_FOLDER="folder";
	public static final String HCOLUMN_LINK="link";
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAttributeService attributeService;
	@Autowired
	private INodeService nodeService;
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private MQMessageSenderExt sender;
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private ICoreMailService coreMailService;
	@Autowired
	private IOrgDomainMappingService mappingService;
	@Autowired
	private VmtConfig config;
	/**
	 * 提交申请科信请求
	 * 
	 * */
	@RequestMapping("/applyDchat")
	@SecurityMapping(level=SecurityLevel.ADMIN)
	@ResponseBody
	public boolean applyDchat(@RequestParam("dn")String dn,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		String decodeDN=LdapUtils.decode(dn);
		if(!LdapUtils.isTeamDN(decodeDN)){
			return false;
		}
		String domains=null;
		String teamName=null;
		if(LdapUtils.isGroupDN(decodeDN)){
			LdapGroup group=groupService.getGroupByDN(decodeDN);
			domains="无";
			teamName=group.getName();
		}else if(LdapUtils.isOrgDN(decodeDN)){
			LdapOrg org=orgService.getOrgByDN(decodeDN);
			domains=CommonUtils.isNull(org.getDomains())?"无":CommonUtils.format(org.getDomains());
			teamName=org.getName();
		}
		attributeService.update(decodeDN, "vmt-apply-open-dchat", LdapOrg.APPLY_OPEN_DCHAT_APPLY);
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("${userName}", user.getUserInfo().getTrueName());
		params.put("${cstnetId}", user.getUserInfo().getCstnetId());
		params.put("${url}",config.getMyBaseUrl()+"/user/gm");
		params.put("${teamName}", teamName);
		params.put("${domains}", domains);
		String content=RenderUtils.getHTML(params, RenderUtils.APPLY_OPEN_DCHAT);
		SimpleEmail email=new SimpleEmail(config.getSuperAdmin(),content, "用户["+user.getUserInfo().getTrueName()+"]申请开通科信");
		JobThread.addJobThread(new EmailSendJob(email));
		return true;
	}
	@RequestMapping("/openDchat")
	@ResponseBody
	@SecurityMapping(level=SecurityLevel.ADMIN)
	public boolean openOrCloseDchat(@RequestParam("dn")String dn,@RequestParam("isOpen")boolean status){
		String decodeDN=LdapUtils.decode(dn);
		String appyStatus="";
		if(LdapUtils.isOrgDN(decodeDN)){
			LdapOrg org=orgService.getOrgByDN(decodeDN);
			appyStatus=org.getApplyOpenDchat();
		}else if(LdapUtils.isGroupDN(decodeDN)){
			LdapGroup group=groupService.getGroupByDN(decodeDN);
			appyStatus=group.getApplyOpenDchat();
		}else{
			return false;
		}
		if(!CommonUtils.isNull(appyStatus)){
			attributeService.delete(decodeDN, "vmt-apply-open-dchat",new String[]{appyStatus});
		}
		
		attributeService.update(decodeDN, "vmt-open-dchat", status);
		sender.sendOpenDchatMessage(dn, status);
		return true;
	}
	
	/**
	 * 删除实体
	 * @param dn 要删除的对象的dn
	 * @param user 已登录用户
	 * @param type 删除的类型，团队>部门>用户
	 * @return
	 */
	@SecurityMapping(level=SecurityLevel.ADMIN)
	@RequestMapping("/unbind")
	@ResponseBody
	public int unbind(
			 @RequestParam("dn") String dn
			,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user
			,@RequestParam("type") String type){
		String decodeDN=LdapUtils.decode(dn);
		if(ListView.TYPE_DEPART.equals(type)){
			if(userService.isExistsSubTree(dn, user.getUserInfo().getUmtId(),false)){
				return -1;
			}
			LdapOrg org=orgService.getOrgByDN(LdapUtils.getTeamDN(decodeDN));
			if(!org.isAdmin(user.getUserInfo().getUmtId())&&!user.getIsSuperAdmin()){
				return -1;
			}
			boolean fromCoreMail=LdapNode.FROM_CORE_MAIL.equals(org.getFrom());
			if(fromCoreMail){
				if(userService.checkHasCoreMailUser(decodeDN)){
					return -2;
				}
			}
			int count=userService.getSubUserCount(decodeDN);
			sender.sendUnbindMessage(dn);
			commonService.unbind(decodeDN);
			if(fromCoreMail){
				coreMailService.deleteUnit(org.getSymbol(), LdapUtils.getLastValue(decodeDN));
			}
			userService.plusCount(decodeDN, -1*count);
			indexService.buildAIndexJob(LdapUtils.getTeamDN(decodeDN));
			
			return count;
		}else if(ListView.TYPE_USER.equals(type)){
			if(!user.getIsSuperAdmin()&&decodeDN.startsWith("vmt-umtid="+user.getUserInfo().getUmtId())){
				return -1;
			}
			sender.sendUnbindMessage(dn);
			commonService.unbind(decodeDN);
			userService.plusCount(decodeDN, -1);
			indexService.buildAIndexJob(LdapUtils.getTeamDN(decodeDN));
			return 1;
		}else{
			sender.sendUnbindMessage(dn);
			commonService.unbind(decodeDN);
			indexService.deleteIndex(LdapUtils.getTeamDN(decodeDN));
		}
		return 1;
		
	}
	/**
	 * 重命名
	 * @param dn 要重命名的dn
	 * @param name 要改名改成什么样？
	 * @param type 部门，或者人
	 * @return
	 */
	@SecurityMapping(level=SecurityLevel.SELF_OR_ADMIN)
	@RequestMapping("/rename")
	@ResponseBody
	public boolean rename(@RequestParam("dn") String dn,@RequestParam("name")String name,@RequestParam("type")String type){
		String decodeDn=LdapUtils.decode(dn);
		if(HCOLUMN_FOLDER.equals(type)){
			String beforeName=CommonUtils.first(attributeService.get(decodeDn, "vmt-current-display"));
			attributeService.update(decodeDn, "vmt-name", CommonUtils.trim(name));
			nodeService.updateSonAndSelfDisplayName(decodeDn, CommonUtils.trim(name));
			indexService.updateTeamName(decodeDn, CommonUtils.trim(name));
			String teamSymbol=LdapUtils.getLastValue(LdapUtils.getTeamDN(decodeDn));
			if(LdapUtils.isDeptDN(decodeDn)){
				coreMailService.updateUnitName(teamSymbol, LdapUtils.getLastValue(decodeDn), name);
			}else{
				mappingService.updateOrgName(teamSymbol, CommonUtils.trim(name));
			}
			sender.sendUpdateMessage(decodeDn,beforeName);
		}else if(HCOLUMN_LINK.equals(type)){
			attributeService.update(decodeDn,"vmt-name",CommonUtils.trim(name));
			indexService.updateUserName(decodeDn, CommonUtils.trim(name));
			sender.sendUpdateMessage(decodeDn);
		}
		return true;
	}
	/**
	 * 通过dn获得该显示什么的信息
	 * @param dn 实体的dn
	 * @return
	 */
	@SecurityMapping
	@RequestMapping("/getDisplay")
	@ResponseBody
	public String getName(@RequestParam("dn")String dn){
		String decodeDn=LdapUtils.decode(dn);
		return LdapUtils.encode(nodeService.getNode(decodeDn).getCurrentDisplay());
	}

}
