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

import java.util.List;

import net.duckling.vmt.api.domain.message.MQMoveDepartMessage;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.view.ListView;
import net.duckling.vmt.exception.LdapOpeException;
import net.duckling.vmt.service.IDepartmentService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.web.helper.CoreMailHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 
 * @author lvly
 * @since 2013-5-2
 */
@Controller
@RequestMapping("/user/depart")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class DepartmentController {
	private static final Logger LOG=Logger.getLogger(DepartmentController.class);
	private static final String DEST_DN="destDn";
	@Autowired
	private IDepartmentService departService;
	@Autowired
	private MQMessageSenderExt sender;
	@Autowired
	private CommonController comController;
	@Autowired
	private IUserService userService;
	@Autowired
	private CoreMailHelper coreMailHelper;
	
	/**
	 * 创建团队
	 * @param user
	 * @param pdn
	 * @param departName
	 * @param symbol
	 * @return
	 */
	@SecurityMapping(level=SecurityLevel.ADD_PRIV,dnParam="pdn")
	@RequestMapping("create")
	@ResponseBody
	public boolean createDepartment(
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,
			@RequestParam("pdn")String pdn,
			LdapDepartment depart){
			String decodePdn=LdapUtils.decode(pdn);
			depart.setCreator(user.getUserInfo().getUmtId());
			depart.setSymbol(LdapUtils.removeDangerous(depart.getSymbol()).toLowerCase());
		try {
			boolean flag= departService.create(decodePdn, depart);
			coreMailHelper.createDepart(decodePdn, depart);
			sender.sendCreateDeptMessage(depart,decodePdn);
			return flag;
		} catch (LdapOpeException e) {
			return false;
		}
	}
	/**
	 * 移动部门，移动orgDn到destDn
	 * @param destDn 目标dn
	 * @param orgDn  源dn
	 * @param sonOnly 包含自己
	 * @return flag
	 * */
	@RequestMapping("move")
	@SecurityMapping(dnParam=DEST_DN,level=SecurityLevel.ADMIN)
	@ResponseBody
	public boolean moveDepartment(@RequestParam(DEST_DN)String destDn,@RequestParam("orgDn")String orgDn,@RequestParam("sonOnly")boolean sonOnly){
		String decodeOrgDn=LdapUtils.decode(orgDn);
		String decodeDestDn=LdapUtils.decode(destDn);
		if(LdapUtils.less(decodeOrgDn, 1).equals(decodeDestDn)&&!sonOnly){
			LOG.warn("this operation is move to self,stop!"+decodeOrgDn+","+decodeDestDn);
			return true;
		}
		MQMoveDepartMessage deptMsg=sender.getMoveDeptMessage(decodeOrgDn, decodeDestDn, !sonOnly);
		boolean flag= departService.moveDepartment(decodeDestDn, decodeOrgDn,sonOnly,true);
		sender.send(deptMsg);
		return flag;
	}
	@RequestMapping("show")
	@SecurityMapping(dnParam=DEST_DN,level=SecurityLevel.VIEW)
	@ResponseBody
	public LdapDepartment showDept(@RequestParam(DEST_DN)String destDn,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		String decodeDN=LdapUtils.decode(destDn);
		LdapDepartment dept= departService.getDepartByDN(decodeDN);
		dept.setCount(userService.getSubUserCount(decodeDN,user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId()));
		return dept;
	}
	@RequestMapping("update")
	@ResponseBody
	@SecurityMapping(dnParam="dn",level=SecurityLevel.ADMIN)
	public boolean update(LdapDepartment dept,@RequestParam("orgName")String orgName,@RequestParam("orgVisible")boolean visible){
		LdapDepartment orgDept=departService.updateDepart(dept);
		if(!orgDept.getName().equals(dept.getName())){  
			sender.sendUpdateMessage(dept.getDn(),orgDept.getCurrentDisplay()); 
		}else{
			sender.sendUpdateMessage(dept.getDn());
		}
		return true;
	}
	/**
	 * 移动部门，会不会导致部门合并？
	 * @param destDn 目标dn
	 * @param orgDn  源dn
	 * @param sonOnly 包含自己
	 * @return flag
	 * */
	@RequestMapping("move/causeMerge")
	@SecurityMapping(dnParam=DEST_DN,level=SecurityLevel.ADMIN)
	@ResponseBody
	public List<ListView> canCauseMerge(@RequestParam(DEST_DN)String destDn,@RequestParam("orgDn")String orgDn,@RequestParam("sonOnly")boolean sonOnly){
		return departService.isMoveCauseMerge(LdapUtils.decode(destDn), LdapUtils.decode(orgDn),sonOnly);
	}
}
