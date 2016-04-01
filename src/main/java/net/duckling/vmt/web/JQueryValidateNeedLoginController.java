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

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.PinyinUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.index.OrgDetail;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IDepartmentService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.INodeService;
import net.duckling.vmt.service.IOrgDomainMappingService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;


/**
 * 针对一些jquery validator远程校验的接口
 * @author lvly
 * @since 2013-6-21
 */
@Controller
@SessionAttributes(KEY.SESSION_LOGIN_USER)
@RequestMapping("/user/jq/")
public class JQueryValidateNeedLoginController {
	/**
	 * 组织服务类
	 */
	@Autowired
	private IOrgService orgService;
	/**
	 * 群组服务类
	 */
	@Autowired
	private IGroupService groupService;
	/**
	 * 部门服务类
	 */
	@Autowired
	private IDepartmentService departService;
	/**
	 *节点服务类 
	 */
	@Autowired
	private INodeService nodeService;
	@Autowired
	private ICoreMailService coreMailService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrgDomainMappingService domainService;
	@Autowired
	private IVmtIndexService indexService;

	/**
	 * 创建团队的时候判断是否已存在
	 * 
	 * @param type
	 *            群组还是组织
	 * @param symbol
	 *            期望的名字
	 * */
	@RequestMapping("/canTeamSymbolUse")
	@ResponseBody
	public boolean calNameUse(@RequestParam("type") String type,
			@RequestParam("teamSymbol") String symbol) {
		if (CommonUtils.isNull(symbol)) {
			return false;
		}
		symbol=symbol.replaceAll(" ", "").toLowerCase();
		if ("org".equals(type)) {
			return !orgService.isExists(symbol);
		} else if ("group".equals(type)) {
			return !groupService.isSymbolUsed(symbol);
		} else {
			return true;
		}
	}
	/**
	 * 判断部门名字可否使用，一个层级下，只允许有一个名字
	 * @param departName
	 * @param pdn
	 * @return
	 */
	@RequestMapping(value="/canDepartNameUse")
	@ResponseBody
	public boolean canDepartmentNameUse(
			@RequestParam("departName") String departName,@RequestParam("dn")String dn){
		if(CommonUtils.isNull(departName)){
			return false;
		}
		String decodeDn=LdapUtils.decode(dn);
		LdapDepartment dept=departService.getDepartByDN(decodeDn);
		if(dept==null){
			return false;
		}
		if(departName.equals(dept.getName())){
			return true;
		}
		String pdn=LdapUtils.minus(decodeDn, 1);
		return !departService.isNameExists(pdn, departName);
	}
	/**
	 * 部门标识是否可以使用，在一个组织结构下唯一
	 * @param pdn
	 * @param symbol
	 * @return
	 */
	@RequestMapping("/canDepartSymbolUse")
	@ResponseBody
	public boolean canSymbolUsed(@RequestParam("pdn")String pdn,@RequestParam("departSymbol")String symbol){
		if(CommonUtils.isNull(symbol)){
			return false;
		}
		return !nodeService.isSymbolUsed(LdapUtils.getDN(LdapUtils.decode(pdn), 2), symbol.replaceAll(" ", "").toLowerCase());
	}
	@RequestMapping("/getPinyin")
	@ResponseBody
	public String getPinyin(@RequestParam("word")String word){
		if(CommonUtils.isNull(word)){
			return "";
		}
		return PinyinUtils.getPinyin(word).trim();
	} 
	@RequestMapping("/canEmailUse")
	@ResponseBody
	public boolean canEmaiUse(@RequestParam("domain")String domain,@RequestParam("email")String email){
		return !userService.isGlobalLoginNameExists(email+domain);
	}
	@RequestMapping("/iamDomainAdmin")
	@ResponseBody
	public boolean iamDomainAdmin(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		String domain=EmailUtils.getDomain(user.getUserInfo().getCstnetId());
		OrgDetail od=domainService.getOrgByDomain(domain);
		if(od==null){
			return false;
		}
		LdapOrg org=orgService.getOrgBySymbol(od.getSymbol());
		return org.isAdmin(user.getUserInfo().getUmtId());
	}
	@RequestMapping("isUseableVmtMember")
	@ResponseBody
	public boolean isUseableVmtMember(@RequestParam("ownnerCstnetId")String email){
		return indexService.isUseableVmtMember(CommonUtils.trim(email));
	}
}
