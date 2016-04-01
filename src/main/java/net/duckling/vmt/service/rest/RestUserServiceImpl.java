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
package net.duckling.vmt.service.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.api.IRestUserService;
import net.duckling.vmt.api.domain.ErrorCode;
import net.duckling.vmt.api.domain.VmtApiApp;
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.common.adapter.UmtUser2LdapUserAdapter;
import net.duckling.vmt.common.adapter.VmtApiApp2VmtAppAdapter;
import net.duckling.vmt.common.adapter.VmtUser2LdapUserAdapter;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.PinyinUtils;
import net.duckling.vmt.domain.VmtApp;
import net.duckling.vmt.domain.VmtAppProfile;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ICommonService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtAppProfileService;
import net.duckling.vmt.service.IVmtAppService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.server.annotation.RestMethod;

/**
 * @author lvly
 * @since 2013-5-21
 */
public class RestUserServiceImpl implements IRestUserService{
	private IAttributeService attrService;
	private IUserService userService;
	private ICommonService commonService;
	private UserService umtService;
	private IVmtIndexService indexService;
	private MQMessageSenderExt sender;
	private IOrgService orgService;
	private IGroupService groupService;
	private IVmtAppService appService;
	private IVmtAppProfileService profileService;
	/**
	 * 构造方法，无用，让框架初始化的时候拿到spring的对象
	 */
	public RestUserServiceImpl(){
		attrService=BeanFactory.getBean(IAttributeService.class);
		userService=BeanFactory.getBean(IUserService.class);
		commonService=BeanFactory.getBean(ICommonService.class);
		umtService=BeanFactory.getBean(UserService.class);
		indexService=BeanFactory.getBean(IVmtIndexService.class);
		sender=BeanFactory.getBean(MQMessageSenderExt.class);
		orgService=BeanFactory.getBean(IOrgService.class);
		groupService=BeanFactory.getBean(IGroupService.class);
		appService=BeanFactory.getBean(IVmtAppService.class);
		profileService=BeanFactory.getBean(IVmtAppProfileService.class);
	}
	/**
	 * 判断dn是否存在
	 * @param dn
	 * @throws ServiceException
	 */
	public void checkDN(String dn) throws ServiceException{
		if(!commonService.isExist(dn)){
			throw new ServiceException(ErrorCode.DN_NOT_EXISTS,"this dn["+dn+"] is not exists");
		}
	}
	@Override
	@RestMethod("addKnownUserToDN")
	public boolean[] addKnownUserToDN(String pdn, String[] umtIds,boolean isActive) throws ServiceException {
		String decodeDN=LdapUtils.decode(pdn);
		checkDN(decodeDN);
		boolean[] result= userService.addUserToNodeUsed(decodeDN, umtIds,isActive?LdapUser.STATUS_ACTIVE:LdapUser.STATUS_TEMP,true);
		if(!LdapUtils.isGroupDN(decodeDN)||isActive){
			sender.sendCreateUserMessage(umtIds, decodeDN);
		}
		indexService.addIndexByUmtId(decodeDN, result, umtIds);
		return result;
	}
	@Override
	@RestMethod("addUnkownUserToDN")
	public boolean[] addUnkownUserToDN(String pdn, String[] cstnetIds,boolean isActive) throws ServiceException {
		String decodeDN=LdapUtils.decode(pdn);
		checkDN(decodeDN);
		boolean[] result= userService.addUserToNodeUnUsed(decodeDN, cstnetIds,null,isActive?LdapUser.STATUS_ACTIVE:LdapUser.STATUS_TEMP,false,true);
		if(!LdapUtils.isGroupDN(decodeDN)||isActive){
			sender.sendCreateUserMessage(cstnetIds, result, pdn);
		}
		indexService.addIndexByCstnetId(decodeDN, result, cstnetIds);
		return result;
	}

	@Override
	@RestMethod("removeUser")
	public void removeUser(String[] dns) throws ServiceException {
		String[] decodesDn=LdapUtils.decode(dns);
		sender.sendUnbindMessage(dns);
		userService.unbind(decodesDn);
		indexService.deleteUser(decodesDn);
	}

	@Override
	@RestMethod("rename")
	public void rename(String dn, String newName) throws ServiceException {
		String decodeDn=LdapUtils.decode(dn);
		attrService.update(decodeDn, "vmt-name", newName);
		sender.sendUpdateMessage(decodeDn);
	}
	@Override
	@RestMethod("searchSbUser")
	public List<VmtUser> searchSbUser(String[] scopes, String umtId, String keyword) throws ServiceException {
		List<LdapUser> users= userService.searchUsersByKeyword(keyword, LdapUtils.decode(scopes),umtId);
		return VmtUser2LdapUserAdapter.convert(users);
	}

	@Override
	@RestMethod("searchSbAllUser")
	public List<VmtUser> searchSbAllUser(String umtId, String keyword) throws ServiceException {
		String scopes[]=userService.getMyAccessableTeam(umtId);
		return searchSbUser(scopes, umtId, keyword);
	}
	@Override
	@RestMethod("searchUserByCstnetId")
	public List<VmtUser> searchUserByCstnetId(String dn, String[] cstnetId)
			throws ServiceException {
		String decodeDN=LdapUtils.decode(dn);
		checkDN(decodeDN);
		return VmtUser2LdapUserAdapter.convert(userService.searchUsersByCstnetId(decodeDN,cstnetId));
	}
	@Override
	@RestMethod("searchUserByUmtId")
	public List<VmtUser> searchUserByUmtId(String dn, String[] umtId)
			throws ServiceException {
		String decodeDN=LdapUtils.decode(dn);
		checkDN(decodeDN);
		return  VmtUser2LdapUserAdapter.convert(userService.searchUsersByUmtId(decodeDN,umtId));
	}
	@Override
	@RestMethod("getUmtIdByCstnetId")
	public String[] getUmtIdByCstnetId(String[] cstnetIds) throws ServiceException {
		return umtService.generateUmtId(cstnetIds);
		
	}
	@Override
	@RestMethod("getUserByUmtId")
	public VmtUser getUserByUmtId(String umtId) throws ServiceException {
		UMTUser user=umtService.getUMTUser(umtId);
		if(user==null){
			throw new ServiceException(ErrorCode.USER_NOT_EXISTS, "the umtId["+umtId+"] can't find User");
		}
		return VmtUser2LdapUserAdapter.convert(UmtUser2LdapUserAdapter.convert(user,null,LdapUser.STATUS_ACTIVE));
	}
	@Override
	@RestMethod("getUsersByUmtIds")
	public List<VmtUser> getUsersByUmtIds(String[] umtIds) throws ServiceException {
		List<UMTUser> users=umtService.getUMTUsers(CommonUtils.asList(umtIds));
		if(users==null){
			throw new ServiceException(ErrorCode.USER_NOT_EXISTS, "the umtId["+Arrays.toString(umtIds)+"] can't find User");
		}
		return VmtUser2LdapUserAdapter.convert(UmtUser2LdapUserAdapter.convert(users,null,LdapUser.STATUS_ACTIVE));
	}

	
	@Override
	@RestMethod("getAppsByUmtId")
	public List<VmtApiApp> getAppsByUmtId(String umtId) throws ServiceException {
		List<VmtApp> apps=appService.searchAppByTeamDnsNoDistinct(getDNs(umtId));
		apps=appService.distinctApp(apps);
		filterProfile(apps, umtId);
		return VmtApiApp2VmtAppAdapter.convert(apps);
	}
	private void filterProfile(List<VmtApp> apps,String umtId){
		List<VmtAppProfile> pros=profileService.getProfiles(umtId);
		if(CommonUtils.isNull(pros)){
			return;
		}
		Set<String> fkIds=new HashSet<String>();
		for(VmtAppProfile pro:pros){
			if(VmtAppProfile.VALUE_HIDE.equals(pro.getValue())){
				fkIds.add(pro.getAppType()+"."+pro.getFkId());
			}
		}
		for(Iterator<VmtApp> it=apps.iterator();it.hasNext();){
			VmtApp app=it.next();
			if(fkIds.contains(app.getAppType()+"."+app.getFkId())){
				it.remove();
			}
		}
	}
	private List<String> getDNs(String umtId){
		List<LdapOrg> orgs=orgService.getMyOrgs(umtId);
		List<String> orgDNS=LdapOrg.extractAppOpenOrgId(orgs);
		List<LdapGroup> groups=groupService.getMyGroups(umtId);
		List<String> groupDNS=LdapGroup.extractAppOpenGroupId(groups);
		List<String> dns= merge(orgDNS,groupDNS);
		return dns;
	}
	private List<String> merge(List<String> orgDns,List<String> groupDns){
		List<String> result=new ArrayList<String>();
		if(!CommonUtils.isNull(orgDns)){
			for(String dn:orgDns){
				result.add(dn);
			}
		}
		if(!CommonUtils.isNull(groupDns)){
			for(String dn:groupDns){
				result.add(dn);
			}
		}
		return result;
	}
	@Override
	@RestMethod("updateByUmtId")
	public void updateByUmtId(String teamDN,VmtUser u) throws ServiceException {
		if(u==null){
			throw new ServiceException(ErrorCode.USER_NOT_EXISTS,"update:user is null");
		}
		if(CommonUtils.isNull(u.getUmtId())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"update:umtId is required");
		}
		if(CommonUtils.isNull(teamDN)){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"update:team DN is not found");
		}
		LdapUser ldapUser=userService.searchUserByUmtId(teamDN, u.getUmtId());
		if(ldapUser==null){
			throw new ServiceException(ErrorCode.USER_NOT_EXISTS,String.format("update:user not found dn:[%s],umtId:[%s]", teamDN,u.getUmtId()));
		}
		ldapUser.setCustom1(u.getCustom1());
		ldapUser.setCustom2(u.getCustom2());
		ldapUser.setTelephone(u.getTelephone());
		ldapUser.setOffice(u.getOffice());
		ldapUser.setOfficePhone(u.getOfficePhone());
		ldapUser.setListRank(u.getListRank());
		ldapUser.setName(u.getName());
		ldapUser.setPinyin(PinyinUtils.getPinyin(u.getName()));
		ldapUser.setTitle(u.getTitle());
		ldapUser.setSex(u.getSex());
		userService.updateUser(ldapUser);
		sender.sendUpdateUser(ldapUser);
	}
	
}
