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
package net.duckling.vmt.common.adapter;

import java.util.ArrayList;
import java.util.List;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.domain.ldap.LdapOrg;

/**VmtOrg和LdapOrg相互转换对象
 * VmtOrg，用于提供给外部的组织机构数据对象
 * LdapOrg 用户本地数据持久化的组织机构对象
 * @author lvly
 * @since 2013-5-21
 */
public final class VmtOrg2LdapOrgAdapter {
	private VmtOrg2LdapOrgAdapter(){}
	/**转换
	 * @param org
	 * @return
	 */
	public static  LdapOrg convert(VmtOrg org){
		LdapOrg ldapOrg=new LdapOrg();
		ldapOrg.setAdmins(org.getAdmins());
		ldapOrg.setCreator(org.getCreator());
		ldapOrg.setCount(org.getCount());
		ldapOrg.setDn(org.getDn());
		ldapOrg.setName(org.getName());
		ldapOrg.setSymbol(org.getSymbol());
		ldapOrg.setCurrentDisplay(org.getCurrentDisplay());
		ldapOrg.setOpenDchat(org.isOpenDchat());
		return ldapOrg;
	}
	/**
	 * @param org
	 * @return
	 */
	public static VmtOrg convert(LdapOrg org){
		if(org==null){
			return null;
		}
		VmtOrg vmtOrg=new VmtOrg();
		vmtOrg.setAdmins(org.getAdmins());
		vmtOrg.setCount(org.getCount());
		vmtOrg.setCreator(org.getCreator());
		vmtOrg.setDn(org.getDn());
		vmtOrg.setName(org.getName());
		vmtOrg.setSymbol(org.getSymbol());
		vmtOrg.setCurrentDisplay(org.getCurrentDisplay());
		vmtOrg.setLogo(org.getLogoId()==0?null:"/logo/"+org.getLogoId());
		vmtOrg.setMobileLogo(org.getMobileLogo()==0?null:"/logo/"+org.getMobileLogo());
		vmtOrg.setPcLogo(org.getPcLogo()==0?null:"/logo/"+org.getPcLogo());
		vmtOrg.setPrivilege(org.getPrivilege());
		vmtOrg.setCas(org.getIsCas());
		vmtOrg.setCoreMail(org.getIsCoreMail());
		vmtOrg.setType(org.getType());
		vmtOrg.setDomain(org.getDomains());
		vmtOrg.setOpenDchat(org.isOpenDchat());
//		vmtOrg.setAppOpen(org.isAppsOpen());
//		setApps(vmtOrg,org);
		return vmtOrg;
	}
//	private static void setApps(VmtOrg vOrg,LdapOrg org){
//		List<net.duckling.vmt.domain.VmtApp> list=org.getApps();
//		if(CommonUtils.isNull(list)){
//			return;
//		}
//		List<VmtApp> apiApps=new ArrayList<VmtApp>();
//		for(net.duckling.vmt.domain.VmtApp app:list){
//			VmtApp apiApp=new VmtApp();
//			BeanUtils.copyProperties(app, apiApp);
//			String baseUrl=null;
//			if(VmtApp.APP_TYPE_OAUTH.equals(apiApp.getAppType())){
//				 baseUrl=GlobalConfig.get("duckling.umt.site");
//			}else{
//				 baseUrl=GlobalConfig.get("my.base.url")+"/logo/";
//			}
//			if(!CommonUtils.isNull(apiApp.getLogo100Url())){
//				apiApp.setLogo100Url(baseUrl+apiApp.getLogo100Url());
//			}
//			if(!CommonUtils.isNull(apiApp.getLogo64Url())){
//				apiApp.setLogo64Url(baseUrl+apiApp.getLogo64Url());
//			}
//			if(!CommonUtils.isNull(apiApp.getLogo32Url())){
//				apiApp.setLogo32Url(baseUrl+apiApp.getLogo32Url());
//			}
//			if(!CommonUtils.isNull(apiApp.getLogo16Url())){
//				apiApp.setLogo16Url(baseUrl+apiApp.getLogo16Url());
//			}
//			apiApps.add(apiApp);
//		}
//		vOrg.setApps(apiApps);
//	}
	/**
	 * 批量转换
	 * @param orgs
	 * @return
	 */
	public static List<VmtOrg> convert(List<LdapOrg> orgs){
		List<VmtOrg> result=new ArrayList<VmtOrg>();
		if(CommonUtils.isNull(orgs)){
			return result;
		}
		for(LdapOrg org:orgs){
			result.add(convert(org));
		}
		return result;
	}
}
