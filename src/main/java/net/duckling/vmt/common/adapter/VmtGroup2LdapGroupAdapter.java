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
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.domain.ldap.LdapGroup;

/**
 * VmtGroup转换为LdapGroup的工具类
 * VmtGroup 提供给外部api里面的群组数据对象
 * LdapGroup 本地用户数据持久化的群租 数据对象
 * @author lvly
 * @since 2013-5-21
 */
public final class VmtGroup2LdapGroupAdapter {
	private VmtGroup2LdapGroupAdapter(){}
	/**
	 * 转换
	 * @param group VmtGroup实体类
	 * @return LdapGroup，vmt内部的group
	 * */
	public static LdapGroup convert(VmtGroup group){
		if(group==null){
			return null;
		}
		LdapGroup ldapGroup=new LdapGroup();
		ldapGroup.setCount(group.getCount());
		ldapGroup.setCurrentDisplay(group.getName());
		ldapGroup.setDn(group.getDn());
		ldapGroup.setSymbol(group.getSymbol());
		ldapGroup.setName(group.getName());
		ldapGroup.setAdmins(group.getAdmins());
		ldapGroup.setCreator(group.getCreator());
		ldapGroup.setFrom(group.getFrom());
		ldapGroup.setOpenDchat(group.isOpenDchat());
		ldapGroup.setDescription(group.getDescription());
		return ldapGroup;
	}
	/**
	 * 转换
	 * @param group
	 * @return
	 */
	public static VmtGroup convert(LdapGroup group){
		if(group==null){
			return null;
		}
		VmtGroup vmtGroup=new VmtGroup();
		vmtGroup.setCount(group.getCount());
		vmtGroup.setDn(group.getDn());
		vmtGroup.setName(group.getName());
		vmtGroup.setSymbol(group.getSymbol());
		vmtGroup.setAdmins(group.getAdmins());
		vmtGroup.setCreator(group.getCreator());
		vmtGroup.setFrom(group.getFrom());
		vmtGroup.setLogo(group.getLogoId()==0?null:"/logo/"+group.getLogoId());
		vmtGroup.setMobileLogo(group.getMobileLogo()==0?null:"/logo/"+group.getMobileLogo());
		vmtGroup.setPcLogo(group.getPcLogo()==0?null:"/logo/"+group.getPcLogo());
		vmtGroup.setPrivilege(group.getPrivilege());
		vmtGroup.setOpenDchat(group.getOpenDchat());
		vmtGroup.setDescription(group.getDescription());
//		vmtGroup.setAppOpen(group.isAppsOpen());
//		setApps(vmtGroup,group);
		return vmtGroup;
	}
//	private static void setApps(VmtGroup vGroup,LdapGroup group){
//		List<net.duckling.vmt.domain.VmtApp> list=group.getApps();
//		if(CommonUtils.isNull(list)){
//			return;
//		}
//		List<VmtApp> apiApps=new ArrayList<VmtApp>();
//		for(net.duckling.vmt.domain.VmtApp app:list){
//			VmtApp apiApp=new VmtApp();
//			BeanUtils.copyProperties(app, apiApp);
//			String baseUrl=null;
//			if(VmtApp.APP_TYPE_OAUTH.equals(app.getAppType())){
//				 baseUrl=GlobalConfig.get("duckling.umt.site");
//			}else{
//				 baseUrl=GlobalConfig.get("my.base.url")+"/logo/";
//			}
//			if(!CommonUtils.isNull(app.getLogo100Url())){
//				apiApp.setLogo100Url(baseUrl+app.getLogo100Url());
//			}
//			if(!CommonUtils.isNull(app.getLogo64Url())){
//				apiApp.setLogo64Url(baseUrl+app.getLogo64Url());
//			}
//			if(!CommonUtils.isNull(app.getLogo32Url())){
//				apiApp.setLogo32Url(baseUrl+app.getLogo32Url());
//			}
//			if(!CommonUtils.isNull(app.getLogo16Url())){
//				apiApp.setLogo16Url(baseUrl+app.getLogo16Url());
//			}
//			apiApps.add(apiApp);
//		}
//		vGroup.setApps(apiApps);
//	}
	
	/**批量转换
	 * @param groups
	 * @return
	 */
	public static List<VmtGroup> convert(List<LdapGroup> groups){
		List<VmtGroup> result=new ArrayList<VmtGroup>();
		if(CommonUtils.isNull(groups)){
			return result;
		}
		for(LdapGroup ldapGroup:groups){
			result.add(convert(ldapGroup));
		}
		return result;
	}
	
}
