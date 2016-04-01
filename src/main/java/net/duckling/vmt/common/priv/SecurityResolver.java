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
package net.duckling.vmt.common.priv;

import javax.servlet.http.HttpServletRequest;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.service.ICommonService;
import net.duckling.vmt.service.IPrivilegeService;
import net.duckling.vmt.web.helper.SessionHelper;

/**
 * 实现该url是否可以访问的的具体逻辑
 * @author lvly
 * @since 2013-5-23
 */
public class SecurityResolver {
	private HttpServletRequest request;
	private String dn;
	private SecurityMapping security;
	private IPrivilegeService privService;
	private ICommonService commonService;
	/**
	 * Constructor
	 * @param request HttpServletRequest
	 * @param security 修饰在Controller方法上面的配置
	 */
	public SecurityResolver(HttpServletRequest request,SecurityMapping security){
		this.request=request;
		if(security!=null){
			String requestDN=request.getParameter(security.dnParam());
			this.dn=LdapUtils.decode(CommonUtils.isNull(requestDN)?"":requestDN);
			this.security=security;
			this.privService=BeanFactory.getBean(IPrivilegeService.class);
			this.commonService=BeanFactory.getBean(ICommonService.class);
		}
	}
	/**
	 * 使用此方法的前提条件是用户已登录，请先验证用户是否已登录
	 * @return boolean 是否可以访问
	 * */
	public boolean canAccess(){
		VmtSessionUser user=SessionHelper.getUserInfo(request);
		if(security.level()==SecurityLevel.SUPER_ADMIN){
			return user.getIsSuperAdmin();
		}
		String umtId=user.getUserInfo().getUmtId();
		if(user.getIsSuperAdmin()||this.security.expect().equals(dn)){
			return true;
		}
		String nodeDn=LdapUtils.getTeamDN(dn);
		if(!commonService.isExist(nodeDn)){
			return false;
		}
		return judiceByLevel(nodeDn,umtId,user);
		
	}
	private boolean judiceByLevel(String nodeDn,String umtId,VmtSessionUser user){
		switch(security.level()){
		case ADD_PRIV:{
			return privService.canAdd(nodeDn, umtId);
		}
		case VIEW:{
			return privService.canLook(nodeDn, umtId);
		}
		case ADMIN:{
			return privService.isAdmin(nodeDn, umtId);
		}
		case SELF_OR_ADMIN:{
			String wishUmtId=LdapUtils.getLastValue(dn);
			return wishUmtId.equals(umtId)||privService.isAdmin(nodeDn, umtId);
		}case SUPER_ADMIN:{
			return user.getIsSuperAdmin();
		}
	}
	return false;
	}
}
