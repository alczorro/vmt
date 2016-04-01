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
package net.duckling.vmt.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.adapter.LdapUser2CoreMailUserAdapter;
import net.duckling.vmt.common.adapter.UmtUser2LdapUserAdapter;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.util.CharUtils;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.PinyinUtils;
import net.duckling.vmt.common.util.RenderUtils;
import net.duckling.vmt.dao.IUserDAO;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.SimpleEmail;
import net.duckling.vmt.domain.coremail.CoreMailUser;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ICommonService;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IDepartmentService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.INodeService;
import net.duckling.vmt.service.IOrgDomainMappingService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IPrivilegeService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.thread.EmailSendJob;
import net.duckling.vmt.service.thread.JobThread;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.umt.oauth.UserInfo;

/**
 * @author lvly
 * @since 2013-5-2
 */
@Service
public class UserServiceImpl implements IUserService{
	private static final Logger LOGGER=Logger.getLogger(UserServiceImpl.class);
	private static final int PASS_LENGTH=40;
	@Autowired
	private VmtConfig vmtConfig;
	@Autowired
	private IUserDAO userDAO;
	@Autowired
	private IAttributeService attributeService;
	@Autowired
	private IPrivilegeService privilegeService;
	@Autowired
	private INodeService nodeService;
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private UserService umtService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private ICoreMailService coreMailService;
	@Autowired
	private IDepartmentService deptService;
	@Autowired
	private IOrgDomainMappingService domainService;
	@Override
	public void batchUpdateUser(LdapUser user,String[] needUpdate) {
		LdapUser orgUser=getUserByDN(user.getDn());
		if(CommonUtils.isEqualsContain(needUpdate,"disableDchat")){
			attributeService.update(user.getDn(), "vmt-disable-dchat",user.isDisableDchat());
		}
		if(CommonUtils.isEqualsContain(needUpdate,"sex")){
			ifNotNullThenUpdate(user.getDn(), "vmt-sex",orgUser.getSex(), user.getSex());
		}
		if(CommonUtils.isEqualsContain(needUpdate,"office")){
			ifNotNullThenUpdate(user.getDn(),"vmt-office",orgUser.getOffice(),user.getOffice());
		}
		if(CommonUtils.isEqualsContain(needUpdate,"title")){
			ifNotNullThenUpdate(user.getDn(), "vmt-title",orgUser.getTitle(), user.getTitle());
		}
		
		if(CommonUtils.isEqualsContain(needUpdate,"visible")){
			attributeService.update(user.getDn(), "vmt-visible", user.isVisible());
		}
		
		if(CommonUtils.isEqualsContain(needUpdate,"listRank")){
			attributeService.update(user.getDn(), "vmt-list-rank", user.getListRank());
		}
		
		if(CommonUtils.isEqualsContain(needUpdate,"accountStatus")){
			if(isCoreMailUserInCoreMailOrg(orgUser)){
				ifNotNullThenUpdate(user.getDn(), "vmt-account-status",orgUser.getAccountStatus(), user.getAccountStatus());
				coreMailService.updateAccount(orgUser.getCstnetId(), user.getAccountStatus(),null);    
			}
		}
		
		
		if(CommonUtils.isEqualsContain(needUpdate,"expireTime")){
			if(isCoreMailUserInCoreMailOrg(orgUser)){
				ifNotNullThenUpdate(user.getDn(), "vmt-expire-time",orgUser.getExpireTime(), user.getExpireTime());
				coreMailService.updateAccount(orgUser.getCstnetId(),null, user.getExpireTime());    
			}
		}
		if(CommonUtils.isEqualsContain(needUpdate,"custom1")){
			ifNotNullThenUpdate(user.getDn(), "vmt-custom-1",orgUser.getCustom1(), user.getCustom1());
		}
		if(CommonUtils.isEqualsContain(needUpdate,"custom2")){
			ifNotNullThenUpdate(user.getDn(), "vmt-custom-2",orgUser.getCustom2(), user.getCustom2());
		}
	}
	@Override
	public void updateUser(LdapUser user) {
		LdapUser orgUser=getUserByDN(user.getDn());
		attributeService.update(user.getDn(), "vmt-disable-dchat", user.isDisableDchat());
		attributeService.update(user.getDn(), "vmt-name", CommonUtils.trim(user.getName()));
		attributeService.update(user.getDn(), "vmt-visible", user.isVisible());
		attributeService.update(user.getDn(), "vmt-list-rank", user.getListRank());
		
		ifNotNullThenUpdate(user.getDn(),"vmt-office",orgUser.getOffice(),user.getOffice());
		ifNotNullThenUpdate(user.getDn(),"vmt-office-phone",orgUser.getOfficePhone(),user.getOfficePhone());
		ifNotNullThenUpdate(user.getDn(),"vmt-telephone",orgUser.getTelephone(),user.getTelephone());
		ifNotNullThenUpdate(user.getDn(), "vmt-title",orgUser.getTitle(), user.getTitle());
		ifNotNullThenUpdate(user.getDn(), "vmt-sex",orgUser.getSex(), user.getSex());
		ifNotNullThenUpdate(user.getDn(), "vmt-custom-1",orgUser.getCustom1(), user.getCustom1());
		ifNotNullThenUpdate(user.getDn(), "vmt-custom-2",orgUser.getCustom2(), user.getCustom2());
		if(isCoreMailUserInCoreMailOrg(orgUser)){
			ifNotNullThenUpdate(user.getDn(), "vmt-account-status",orgUser.getAccountStatus(), user.getAccountStatus());
			ifNotNullThenUpdate(user.getDn(), "vmt-expire-time",orgUser.getExpireTime(), user.getExpireTime());
			coreMailService.updateAccount(orgUser.getCstnetId(), user.getAccountStatus(),user.getExpireTime());    
		}
		
	}
	private void ifNotNullThenUpdate(String dn,String key,String objBefore,String objAfter){
		if(CommonUtils.isNull(objAfter)){
			if(!CommonUtils.isNull(objBefore)){
				attributeService.delete(dn, key, new String[]{objBefore});
			}
		}else{
			attributeService.update(dn, key, objAfter);
		}
	}
	public void sendTempMail(String destDn,LdapUser user,UserInfo userInfo){
		String rootDN=LdapUtils.getNodeName(nodeService.getNode(user.getDn()).getCurrentDisplay(),0);
		String teamDn=LdapUtils.getTeamDN(user.getDn());
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("${loginName}", user.getCstnetId());
		map.put("${userName}", user.getName()+"["+user.getCstnetId()+"]");
		if(userInfo!=null){
			map.put("${adminName}", userInfo.getTrueName()+"["+userInfo.getCstnetId()+"]");
		}
		map.put("${teamName}", rootDN);
		map.put("${baseUrl}", vmtConfig.getMyBaseUrl());
		map.put("${activeUrl}", vmtConfig.getMyBaseUrl()+"/user/active/add/"+user.getRandom()+"/"+user.getUmtId()+"?teamDn="+LdapUtils.encode(teamDn) );
		JobThread.addJobThread(new EmailSendJob(new SimpleEmail(user.getCstnetId(),RenderUtils.getHTML(map, RenderUtils.ADD_USER_TEMP),"接受团队邀请")));
	}
	@Override
	public String addUserToNode(String destDn, LdapUser user,UserInfo userInfo) {
		String teamDN=LdapUtils.getTeamDN(destDn);
		user.setPinyin(PinyinUtils.getPinyin(user.getName()));
		user.setRoot(teamDN);
		user.setCode(LdapUser.getCode(user.getPinyin()));
		List<LdapUser> u=new ArrayList<LdapUser>();
		u.add(user);
		setCurrentDisplay(u,destDn);
		if(userDAO.addUserToNode(destDn, u)[0]){
			String dn="vmt-umtId="+user.getUmtId()+","+destDn;
			user.setDn(dn);
			//需要发邮件
			if(!user.getStatus().equals(LdapUser.STATUS_ACTIVE)){
				resend(user, userInfo);
			}
			plusCount(teamDN, 1);
			return dn;
		}
		return null;
	}
	@Override
	public boolean isCoreMailUserInCoreMailOrg(LdapUser user) {
		if(CommonUtils.isNull(user.getDn())){
			return false;
		}
		if(CommonUtils.isNull(user.getCstnetId())){
			return false;
		}
		if(!LdapUtils.isOrgSub(user.getDn())){
			return false;
		}
		LdapOrg org=orgService.getOrgByDN(LdapUtils.getTeamDN(user.getDn()));
		if(!org.getIsCoreMail()){
			return false;
		}
		if(CommonUtils.isNull(org.getDomains())){
			return true;
		}else{
			return CommonUtils.isEqualsContain(org.getDomains(), EmailUtils.getDomain(user.getCstnetId()));
		}
		
	}
	
	@Override
	public boolean[] addUserToNodeUsed(String dn, String[] umtIds,String userStatus,boolean checkIsExists) {
		List<String> umtIdsList=new ArrayList<String>();
		Collections.addAll(umtIdsList, umtIds);
		List<UMTUser> umtUsers=umtService.getUMTUsers(umtIdsList);
		List<LdapUser> ldapUsers=UmtUser2LdapUserAdapter.convert(umtUsers,dn,userStatus);
		if(checkIsExists&&LdapUtils.isOrgSub(dn)){
			checkCanAdd(ldapUsers);
		}
		setCurrentDisplay(ldapUsers, dn);
		setVisible(ldapUsers,dn);
		boolean[] result=userDAO.addUserToNode(dn, ldapUsers);
		plusCount(dn, getSuccessFromResult(result));
		return result;
	}
	private void setVisible(List<LdapUser> users,String pdn){
		if(LdapUtils.isOrgSub(pdn)){
			LdapDepartment dept=deptService.getDepartByDN(pdn);
			for(LdapUser user:users){
				if(user!=null){
					user.setVisible(dept.isVisible());
				}
			}
		}
	}
	private String[] getCstnetIds(String[] cstnetIdAndName){
		String[] cstnetIds=new String[cstnetIdAndName.length];
		int index=0;
		for(String cstnetId:cstnetIdAndName){
			cstnetIds[index++]=cstnetId.split(KEY.GLOBAL_DATA_SPLIT)[0].toLowerCase();
		}
		return cstnetIds;
	}
	private String[] getCstnetIds(List<CoreMailUser> users){
		String[] cstnetIds=new String[users.size()];
		int index=0;
		for(CoreMailUser user:users){
			cstnetIds[index++]=user.getEmail();
		}
		return cstnetIds;
		
	}
	private String[] getTrueNames(String[] cstnetIdAndName){
		String[] trueNames=new String[cstnetIdAndName.length];
		int index=0;
		for(String cstnetId:cstnetIdAndName){
			String value="";
			if(cstnetId.contains(KEY.GLOBAL_DATA_SPLIT)){
				value=cstnetId.split(KEY.GLOBAL_DATA_SPLIT)[1];
			}else{
				value=EmailUtils.getNameFromEmail(cstnetId);
			}
			trueNames[index++]=value;
		}
		return trueNames;
	}
	@Override
	public void deleteUserBatch(String dn, String[] cstnetId) {
		List<LdapUser> users=searchUsersByCstnetId(dn, cstnetId);
		if(!CommonUtils.isNull(users)){
			for(LdapUser user:users){
				commonService.unbind(user.getDn());
			}
		}
		plusCount(dn, -1*users.size());
	}
	@Override
	public List<LdapUser> searchUsersAll() {
		return userDAO.searchUserAll();
	}

	private void checkCanAdd(List<LdapUser> users){
//		int i=0;
//		for(LdapUser user:users){
//			if(indexService.isUserExistsOtherOrg(user.getUmtId())){
//				users.set(i, null);
//			}
//			i++;
//		}
		
	}
	@Override
	public boolean[] addUserToNodeCoreMail(String dn, List<CoreMailUser> users) {
		String [] cstnetIds=getCstnetIds(users);
		try {
			String[] umtIds=umtService.generateUmtId(cstnetIds);
			List<LdapUser> lUsers=LdapUser2CoreMailUserAdapter.convert(dn,users,umtIds);
			setCurrentDisplay(lUsers,dn);
			setVisible(lUsers,dn);
			boolean[] result=userDAO.addUserToNode(dn, lUsers);
			plusCount(dn, getSuccessFromResult(result));
			return result;
		} catch (ServiceException e) {
			LOGGER.error(e.getMessage(),e);
			return null;
		}
	}
	private List<UMTUser> sortByPage(String[] cstnetIds,List<UMTUser> users){
		UMTUser[] results=new UMTUser[users.size()];
		int index=0;
		for(String cstnetId:cstnetIds){
			for(UMTUser u:users){
				if(u==null||cstnetId.equals(u.getCstnetId())){
					results[index++]=u;
					break;
				}
			}
		}
		return Arrays.asList(results);
	}
	private Map<String,String> getCstnetIdToNameMap(String[] cstnetIds,String[] trueNames){
		Map<String,String> nameMap=new HashMap<String,String>();
		if(CommonUtils.isNull(cstnetIds)||CommonUtils.isNull(trueNames)){
			return nameMap;
		}
		int index=0;
		for(String cstnetId:cstnetIds){
			if(!cstnetId.split("@")[0].equals(trueNames[index])){
				nameMap.put(cstnetId, trueNames[index]);
			}
			index++;
		}
		return nameMap;
	}
	private void judiceTrueName(List<UMTUser> umtUser,Map<String,String> nameMap){
		if(CommonUtils.isNull(umtUser)){
			return;
		}
		for(UMTUser uu:umtUser){
			String nameMapName=nameMap.get(uu.getCstnetId());
			if(nameMapName!=null){
				uu.setTruename(nameMapName);
			}
		}
		
	}
	@Override
	public boolean[] addUserToNodeUnUsed(String dn, String[] cstnetIdAndTrueName,UserInfo userInfo,String status,boolean sendMail,boolean checkIsExists){
		try {
			String [] cstnetIds=getCstnetIds(cstnetIdAndTrueName);
			String [] trueNames=getTrueNames(cstnetIdAndTrueName);
			String[] umtIds=umtService.generateUmtId(cstnetIds);
			Map<String,String> nameMap = getCstnetIdToNameMap(cstnetIds,trueNames);
			
			List<UMTUser> umtUsers=new ArrayList<UMTUser>();
			umtUsers.addAll(addUnRegistUser(umtIds, cstnetIds,trueNames));
			umtUsers.addAll(addExistsUser(umtIds));
			umtUsers=sortByPage(cstnetIds,umtUsers);
			judiceTrueName(umtUsers,nameMap);
			List<LdapUser> users=UmtUser2LdapUserAdapter.convert(umtUsers,dn,status);
			if(checkIsExists&&LdapUtils.isOrgSub(dn)){
				checkCanAdd(users);
			}
			setCurrentDisplay(users,dn);
			setVisible(users,dn);
			boolean[] result=userDAO.addUserToNode(dn, users);
			plusCount(dn, getSuccessFromResult(result));
			sendEmail(result, umtUsers, users, userInfo, dn,sendMail);
			return result;
		} catch (ServiceException e) {
			LOGGER.error(e.getMessage(),e);
			return null;
		}
	}
	private void setCurrentDisplay(List<LdapUser> users,String pdn){
		LdapNode node=nodeService.getNode(pdn);			
		for(LdapUser user:users){
			if(user!=null){
				user.setCurrentDisplay(node.getCurrentDisplay());
			}
		}
	}
	private List<UMTUser> addUnRegistUser(String umtIds[],String[] cstnetIds,String[] trueNames){
		List<UMTUser> needRegistUser=new ArrayList<UMTUser>();
		int index=0;
		for(String umtId:umtIds){
			if(CommonUtils.isNull(umtId)){
				UMTUser user=new UMTUser();
				user.setCstnetId(cstnetIds[index]);
				user.setTruename(trueNames[index]);
				user.setPassword(CharUtils.random());
				LOGGER.info("need regist user:"+user.getCstnetId());
				needRegistUser.add(user);
			}
			index++;
			
		}
		needRegistUser=umtService.createUsers(needRegistUser);
		return needRegistUser;
	}
	private List<UMTUser> addExistsUser(String umtIds[]){
		List<String> umtIdsList=new ArrayList<String>();
		Collections.addAll(umtIdsList, umtIds);
		return umtService.getUMTUsers(umtIdsList);
	}
	@Override
	public LdapUser getUserByDN(String dn) {
		return userDAO.getUser(dn);
	}
	@Override
	public void resend(LdapUser user,UserInfo userInfo) {
		String teamDn=LdapUtils.getTeamDN(user.getDn());
		String random=CharUtils.random(PASS_LENGTH);
		String rootDN=LdapUtils.getNodeName(nodeService.getNode(user.getDn()).getCurrentDisplay(),0);
		attributeService.update(user.getDn(), "vmt-random", random);
		attributeService.update(user.getDn(), "vmt-status", LdapUser.STATUS_TEMP);
		indexService.updateStatus(teamDn, userInfo.getUmtId(), LdapUser.STATUS_TEMP);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("${loginName}", user.getCstnetId());
		map.put("${userName}", user.getName()+"["+user.getCstnetId()+"]");
		map.put("${adminName}", userInfo.getTrueName()+"["+userInfo.getCstnetId()+"]");
		map.put("${teamName}", rootDN);
		map.put("${baseUrl}", vmtConfig.getMyBaseUrl());
		map.put("${activeUrl}", vmtConfig.getMyBaseUrl()+"/user/active/add/"+random+"/"+user.getUmtId()+"?teamDn="+LdapUtils.encode(teamDn));
		JobThread.addJobThread(new EmailSendJob(new SimpleEmail(user.getCstnetId(),RenderUtils.getHTML(map, RenderUtils.ADD_USER_TEMP),"接受团队邀请")));
	}
	private void sendEmail(boolean[] result,List<UMTUser> umtUsers,List<LdapUser> users,UserInfo userInfo,String dn,boolean sendMail){
		int index=0;
		String rootDN=LdapUtils.getNodeName(nodeService.getNode(dn).getCurrentDisplay(),0);
		for(boolean flag:result){
			if(flag){
				UMTUser umtUser=umtUsers.get(index);
				LdapUser user=users.get(index);
				String teamDn=LdapUtils.getTeamDN(dn);
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("${loginName}", umtUser.getCstnetId());
				map.put("${userName}", umtUser.getTruename()+"["+umtUser.getCstnetId()+"]");
				if(userInfo!=null){
					map.put("${adminName}", userInfo.getTrueName()+"["+userInfo.getCstnetId()+"]");
				}
				map.put("${teamName}", rootDN);
				map.put("${baseUrl}", vmtConfig.getMyBaseUrl());
				map.put("${activeUrl}", vmtConfig.getMyBaseUrl()+"/user/active/add/"+user.getRandom()+"/"+umtUser.getUmtId()+"?teamDn="+LdapUtils.encode(teamDn) );
				map.put("${password}",umtUser.getPassword());
				if(CommonUtils.isNull(umtUser.getPassword())&&sendMail){
					JobThread.addJobThread(new EmailSendJob(new SimpleEmail(umtUser.getCstnetId(),RenderUtils.getHTML(map, RenderUtils.ADD_USER_TEMP),"接受团队邀请")));
				}else if(!CommonUtils.isNull(umtUser.getPassword())){
					JobThread.addJobThread(new EmailSendJob(new SimpleEmail(umtUser.getCstnetId(),RenderUtils.getHTML(map, RenderUtils.ADD_USER_SAY_PASSWD),"注册成功")));
				}else if(!CommonUtils.isNull(umtUser.getPassword())&&sendMail){
					JobThread.addJobThread(new EmailSendJob(new SimpleEmail(umtUser.getCstnetId(),RenderUtils.getHTML(map, RenderUtils.ADD_USER_REGIST_TEMP),"接受团队邀请")));
				}
			}
			index++;
		}
	}
	private int getSuccessFromResult(boolean[] result){
		int index=0;
		for(boolean b:result){
			if(b){
				index++;
			}
		}
		return index;
	}
	@Override
	public void plusCount(String dn,int plus){
		String orgDN=LdapUtils.getDN(dn,2);
		int count=Integer.valueOf(attributeService.get(orgDN, "vmt-count")[0]);
		attributeService.update(orgDN, "vmt-count", (count+plus)+"");
	}
	@Override
	public List<LdapUser> searchUsersByDN(String dn,String expectMeUmtId) {
		return sort(userDAO.searchUserByDN(dn,expectMeUmtId));
	}
	@Override
	public List<LdapUser> searchUsersByAll(String[] scope,String umtId) {
		String teamDN=LdapUtils.getTeamDN(CommonUtils.first(scope));
		boolean isAdmin=CommonUtils.isNull(umtId)?true:privilegeService.isAdmin(teamDN, umtId);
		return sort(userDAO.searchUserByAll(scope,canLookUnConfirm(scope, umtId),isAdmin));
	}
	private boolean[] canLookUnConfirm(String[] scopes,String umtId){
		boolean[] result=new boolean[scopes.length];
		boolean all=CommonUtils.isNull(umtId);
		int index=0;
		for(String scope:scopes){
			result[index++]=all?all:privilegeService.canLookUnConfirm(scope, umtId);
		}
		return result;
	}
	private boolean canLookUnConfirm(String scope,String umtId){
		if(CommonUtils.isNull(umtId)){
			return true;
		}
		String teamDN=LdapUtils.getTeamDN(scope);
		return canLookUnConfirm(new String[]{teamDN},umtId)[0];
	}
	@Override
	public List<LdapUser> searchUsersByLetter(String letter, String[] scope,String umtId) {
		if(CommonUtils.isNull(scope)){
			return null;
		}
		String teamDN=LdapUtils.getTeamDN(CommonUtils.first(scope));
		boolean isAdmin=CommonUtils.isNull(umtId)?true:privilegeService.isAdmin(teamDN, umtId);
		return sort(userDAO.searchUsersByLetter(letter, scope,canLookUnConfirm(scope, umtId),isAdmin));
	}
	@Override
	public List<LdapUser> searchUsersByKeyword(String keyword,String[] scope,String umtId){
		return searchUsersByKeyword(keyword,scope,null,umtId);
	}
	@Override
	public List<LdapUser> searchUsersByKeyword(String keyword, String[] scope,
			String[] domain, String umtId) {
		String teamDN=LdapUtils.getTeamDN(CommonUtils.first(scope));
		boolean isAdmin=CommonUtils.isNull(umtId)?true:privilegeService.isAdmin(teamDN, umtId);
		return sort(userDAO.searchUserByKeyword(keyword,scope,domain,canLookUnConfirm(scope, umtId),isAdmin));
	}
	private List<LdapUser> sort(List<LdapUser> users){
		if(!CommonUtils.isNull(users)){
			Collections.sort(users);
		}
		return users;
	}
	@Override
	public void unbind(String[] dns) {
		if(CommonUtils.isNull(dns)){
			return;
		}
		userDAO.unbind(dns);
		String teamDn=LdapUtils.getTeamDN(dns[0]);
		LdapGroup group=groupService.getGroupByDN(teamDn);
		for(String dn:dns){
			String umtId=LdapUtils.getLastValue(dn);
			if(CommonUtils.isEqualsContain(group.getAdmins(), umtId)){
				removeAdmin(group.getDn(), umtId);
			}
		}
		plusCount(teamDn, -1*dns.length);
	}
	@Override
	public void move(String[] users, String moveto,boolean isExtendDest) {
		boolean[] result=userDAO.move(users,moveto);
		int index=0;
		LdapDepartment wantDest=null;
		LdapOrg org=orgService.getOrgByDN(LdapUtils.getTeamDN(moveto));
		boolean isCoreMail=LdapNode.FROM_CORE_MAIL.equals(org.getFrom());
		//判定目标不是根,且要继承目标的可见属性的话
		if(!LdapUtils.isOrgDN(moveto)&&isExtendDest){
			wantDest=deptService.getDepartByDN(moveto);
		}

		for(String userDN:users){
			if(result[index++]){
				String userNewDN=LdapUtils.getLast(userDN)+","+moveto;
				attributeService.update(userNewDN, "vmt-root", LdapUtils.getDN(moveto, 2));
				if(wantDest!=null){
					attributeService.update(userNewDN, "vmt-visible",wantDest.isVisible());
				}
				String cstnetId=attributeService.get(userNewDN, "vmt-id")[0];
				if(isCoreMail&&CommonUtils.isEqualsContain(org.getDomains(), EmailUtils.getDomain(cstnetId))){
					coreMailService.moveUser(cstnetId, LdapUtils.getLastValue(moveto));
				}
				
				nodeService.updateMove(userNewDN, moveto);
			}
		}
	}
	@Override
	public boolean  addAdmin(String dn, String umtId) {
			if(attributeService.isExists(dn, "vmt-admin", umtId)){
				return false;
			}
			attributeService.insert(dn, "vmt-admin", new String[]{umtId});
			return true;
	}
	@Override
	public void removeAdmin(String dn, String umtId) {
		attributeService.delete(dn, "vmt-admin",new String[]{umtId});
	}
	@Override
	public LdapUser checkRandomOK(String teamDn,String random, String umtId) {
		return userDAO.checkRandomOK(teamDn,random,umtId);
	}
	@Override
	public boolean isExistsSubTree(String dn, String umtId,boolean getRefuse) {
		return userDAO.isExistsSubTree(dn, umtId,getRefuse);
	}
	@Override
	public int getSubUserCount(String baseDN) {
		return userDAO.getSubUserCount(baseDN);
	}
	@Override
	public int getSubUserCount(String decodeDN, String umtId) {
		return userDAO.getSubUserCount(decodeDN,canLookUnConfirm(decodeDN,umtId),privilegeService.isAdmin(decodeDN, umtId)||CommonUtils.isNull(umtId));
	}
	
	@Override
	public String[] getMyAccessableTeam(String umtId) {
		List<LdapOrg> orgs=orgService.getMyOrgs(umtId);
		List<String> result=new ArrayList<String>();
		if(!CommonUtils.isNull(orgs)){
			for(LdapOrg org:orgs){
				result.add(org.getDn());
			}
		}
		List<LdapGroup> groups=groupService.getMyGroups(umtId);
		if(!CommonUtils.isNull(groups)){
			for(LdapGroup group:groups){
				result.add(group.getDn());
			}
		}
		return list2Array(result);
	}
	private String[] list2Array(List<String> strs){
		if(CommonUtils.isNull(strs)){
			return new String[0];
		}
		String result[]=new String[strs.size()];
		int index=0;
		for(String str:strs){
			result[index++]=str;
		}
		return result;
	}
	@Override
	public List<LdapUser> searchUsersByCstnetId(String decodeDN, String[] cstnetId) {
		return sort(userDAO.searchUsersByCstnetId(decodeDN, cstnetId));
	}
	@Override
	public List<LdapUser> searchUsersByUmtId(String pdn,String[] umtId) {
		if(umtId==null){
			return null;
		}
		return sort(userDAO.searchUsersByUmtId(pdn, umtId));
	}
	@Override
	public boolean isNullOrSelf(String dn) {
		int count=Integer.valueOf(attributeService.get(dn,"vmt-count")[0]);
		String creator=attributeService.get(dn,"vmt-creator")[0];
		return (count==0||(count==1&&isExistsSubTree(dn, creator, false)));
	}
	@Override
	public LdapUser searchUserByUmtId(String baseDn, String umtId) {
		return CommonUtils.first(searchUsersByUmtId(baseDn, new String[]{umtId}));
	}
	@Override
	public boolean checkHasCoreMailUser(String pdn){
		return !CommonUtils.isNull(userDAO.searchCoreMailUser(pdn));
	}
	@Override
	public boolean isGlobalLoginNameExists(String loginName) {
		UMTUser user=umtService.getUMTUserByLoginName(loginName);
		boolean coreMailUserExits=coreMailService.isUserExists(loginName);
		if(coreMailUserExits){
			return true;
		}
		boolean coreMailUserDeleted=(user!=null&&!"umt".equals(user.getAuthBy()));
		if(coreMailUserDeleted){
			return false;
		}
		return user!=null;
	}
}
