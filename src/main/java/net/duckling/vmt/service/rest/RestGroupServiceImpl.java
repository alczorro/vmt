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

import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.falcon.api.serialize.JSONMapper;
import net.duckling.vmt.api.IRestGroupService;
import net.duckling.vmt.api.domain.ErrorCode;
import net.duckling.vmt.api.domain.TreeNode;
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.api.util.NameRuleUtils;
import net.duckling.vmt.common.adapter.VmtGroup2LdapGroupAdapter;
import net.duckling.vmt.common.adapter.VmtUser2LdapUserAdapter;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ICommonService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.INodeService;
import net.duckling.vmt.service.ISearchService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;

import org.apache.log4j.Logger;

import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.server.annotation.RestMethod;
/**
 * The Service of the Group Object
 * @author lvly
 * @since 2013-5-20
 */
public class RestGroupServiceImpl implements IRestGroupService{
	private static final Logger LOG=Logger.getLogger(RestGroupServiceImpl.class);
	private IGroupService groupService;
	private IAttributeService attrService;
	private ICommonService commonService;
	private INodeService nodeService;
	private IUserService userService;
	private UserService umtService;
	private ISearchService searchService;
	private IVmtIndexService indexService;
	private MQMessageSenderExt sender;
	
	/**
	 * 
	 */
	public RestGroupServiceImpl(){
		groupService=BeanFactory.getBean(IGroupService.class);
		attrService=BeanFactory.getBean(IAttributeService.class);
		commonService=BeanFactory.getBean(ICommonService.class);
		nodeService=BeanFactory.getBean(INodeService.class);
		userService=BeanFactory.getBean(IUserService.class);
		umtService=BeanFactory.getBean(UserService.class);
		searchService=BeanFactory.getBean(ISearchService.class);
		indexService=BeanFactory.getBean(IVmtIndexService.class);
		sender=BeanFactory.getBean(MQMessageSenderExt.class);
	}
	@Override
	@RestMethod("create")
	public String create(VmtGroup group) throws ServiceException{
		if(group==null){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"can not create null group");
		}
		if(CommonUtils.isNull(group.getCreator())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"must be specify a creator like[10000201]");
		}
		if(CommonUtils.isNull(group.getName())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"the group name required");
		}
		if(CommonUtils.isNull(group.getSymbol())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"the group symbol required");
		}
		if(hasSymbolUsed(group.getSymbol())){
			throw new ServiceException(ErrorCode.SYMBOL_USED, "the symbol ["+group.getSymbol()+"] is used");
		}
		if(!NameRuleUtils.isDDLNameMatch(group.getName())){
			throw new ServiceException(ErrorCode.PATTERN_ERROR,"the name is not match to ddlNameRule");
		}
		if(!NameRuleUtils.isSymbolMatch(group.getSymbol())){
			throw new ServiceException(ErrorCode.PATTERN_ERROR,"the ortsymbol is not match to symbolrule");
		}
		group.setAdmins(new String[]{group.getCreator()});
		LdapGroup ldapGroup=VmtGroup2LdapGroupAdapter.convert(group);
		groupService.createGroup(ldapGroup,true);
		LOG.info("API-Group.create:"+JSONMapper.getJSONString(group));
		sender.sendCreateGroupMessage(ldapGroup);
		indexService.buildAIndexSynchronous(ldapGroup.getDn());
		return ldapGroup.getDn();
	}
	protected void setTelephoneNull(List<LdapUser> users){
		if(CommonUtils.isNull(users)){
			for(LdapUser u:users){
				u.setTelephone(null);
			}
		}
	}
	@Override
	@RestMethod("getAdmins")
	public List<VmtUser> getAdmins(String groupDn) throws ServiceException {
		String decodeDN =LdapUtils.decode(groupDn);
		checkGroup(decodeDN);
		LdapGroup group=groupService.getGroupByDN(decodeDN);
		List<LdapUser> users=userService.searchUsersByUmtId(groupDn, group.getAdmins());
		if(group.isHideMobile()){
			setTelephoneNull(users);
		}
		return VmtUser2LdapUserAdapter.convert(users);
	}
	private void checkGroup(String decodeDN)throws ServiceException{
		if(CommonUtils.isNull(decodeDN)){
			throw new ServiceException(ErrorCode.DN_NOT_EXISTS,"this dn["+decodeDN+"] is null");
		}
		if(!LdapUtils.isGroupSub(decodeDN)){
			throw new ServiceException(ErrorCode.NOT_A_GROUP,"this dn is not a group");
		}
		if(!commonService.isExist(decodeDN)){
			throw new ServiceException(ErrorCode.DN_NOT_EXISTS,"this dn["+decodeDN+"] is not exists");
		}
	}
	@Override
	@RestMethod("rename")
	public void rename(String groupDN,String newName) throws ServiceException{
		String decodeDN=LdapUtils.decode(groupDN);
		checkGroup(decodeDN);
		String beforeName=CommonUtils.first(attrService.get(decodeDN, "vmt-name"));
		attrService.update(decodeDN, "vmt-name", newName);
		sender.sendUpdateMessage(groupDN,beforeName);
		nodeService.updateSonAndSelfDisplayName(decodeDN, newName);
		LOG.info("API-Group.rename:"+groupDN+","+newName);
	}
	
	@Override
	@RestMethod("delete")
	public void delete(String groupDN)throws ServiceException{
		String decodeDN=LdapUtils.decode(groupDN);
		checkGroup(decodeDN);
		if(!commonService.isExist(groupDN)){
			throw new ServiceException(ErrorCode.DN_NOT_EXISTS,"this dn is not exists");
		}
		sender.sendUnbindMessage(groupDN);
		commonService.unbind(decodeDN);
		indexService.deleteIndex(decodeDN);
		LOG.info("API-Group.delete:"+groupDN);
	}
	@Override
	@RestMethod("addAdminByUmtId")
	public void addAdminByUmtId(String groupDN,String umtId) throws ServiceException{
		String decodeDN=LdapUtils.decode(groupDN);
		checkGroup(decodeDN);
		
		if(!userService.isExistsSubTree(decodeDN, umtId,true)){
			throw new ServiceException(ErrorCode.USER_NOT_EXISTS,"user not exits in this group:"+umtId);
		}
		LdapGroup ldapGroup=groupService.getGroupByDN(decodeDN);
		if(!CommonUtils.isEqualsContain(ldapGroup.getAdmins(), umtId)){
			userService.addAdmin(decodeDN, umtId);
			sender.sendUpdateMessage(groupDN);
		}
		
		
	}
	@Override
	@RestMethod("addAdminByCstnetId")
	public void addAdminByCstnetId(String groupDN, String cstnetId) throws ServiceException {
		String[] umtIds=umtService.generateUmtId(new String[]{cstnetId});
		addAdminByUmtId(groupDN, umtIds[0]);
	}
	@Override
	@RestMethod("removeAdminByUmtId")
	public void removeAdminByUmtId(String groupDN,String umtId) throws ServiceException{
		String decodeDN=LdapUtils.decode(groupDN);
		checkGroup(decodeDN);
		LdapGroup ldapGroup=groupService.getGroupByDN(decodeDN);
		if(CommonUtils.isEqualsContain(ldapGroup.getAdmins(), umtId)){
			userService.removeAdmin(decodeDN, umtId);
			sender.sendUpdateMessage(groupDN);
		}
		
	}  
	@Override
	@RestMethod("removeAdminByCstnetId")
	public void removeAdminByCstnetId(String groupDN, String cstnetId) throws ServiceException {
		String[] umtIds=umtService.generateUmtId(new String[]{cstnetId});
		removeAdminByUmtId(groupDN, umtIds[0]);
	}
	@Override
	@RestMethod("getSbGroup")
	public List<VmtGroup> getSbGroup(String umtId) throws ServiceException {
		return VmtGroup2LdapGroupAdapter.convert(groupService.getMyGroups(umtId));
	}
	@Override
	@RestMethod("hasSymbolUsed")
	public boolean hasSymbolUsed(String symbol) throws ServiceException {
		return groupService.isSymbolUsed(symbol);
	}
	@Override
	@RestMethod("getMyThirdPartyGroupByCstnetId")
	public List<VmtGroup> getMyThirdPartyGroupByCstnetId(String from,
			String cstnetId) throws ServiceException {
		String umtId=umtService.generateUmtId(new String[]{cstnetId})[0];
		return getMyThirdPartyGroupByUmtId(from,umtId);
	}
	@Override
	@RestMethod("getMyThirdPartyGroupByUmtId")
	public List<VmtGroup> getMyThirdPartyGroupByUmtId(String from, String umtId)
			throws ServiceException {
		checkFrom(from);
		if(CommonUtils.isNull(umtId)){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"umtId required!");
		}
		return VmtGroup2LdapGroupAdapter.convert(groupService.getThirdPartyGroupByUmtId(from,umtId));
	}
	@Override
	@RestMethod("update")
	public void update(VmtGroup group) throws ServiceException {
		if(CommonUtils.isNull(group.getSymbol())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"symbol required");
		}
		LdapGroup lg=groupService.getGroupBySymbol(group.getSymbol(),null);
		if(lg==null){
			throw new ServiceException(ErrorCode.SYMBOL_NOT_FOUND,"group not found,that symbol is "+group.getSymbol());
		}
		attrService.update(lg.getDn(),"vmt-description", group.getDescription());
		sender.sendUpdateMessage(lg.getDn());
	}
	private void checkFrom(String from) throws ServiceException{
		if(!VmtGroup.FROM_CORE_MAIL.equals(from)&&
		   !VmtGroup.FROM_DDL.equals(from)&&
		   !VmtGroup.FROM_DCHAT.equals(from)){
			throw new ServiceException(ErrorCode.FILELD_EXPECT,"unexpected from value["+from+"]");
		}
	}
	@Override
	@RestMethod("hasNameUsed")
	public boolean hasNameUsed(String groupName) throws ServiceException {
		return groupService.isNameUsed(groupName);
	}
	@Override
	@RestMethod("getGroupBySymbol")
	public VmtGroup getGroupBySymbol(String symbol) throws ServiceException {
		if(CommonUtils.isNull(symbol)){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"the symbol is required");
		}
		return VmtGroup2LdapGroupAdapter.convert(groupService.getGroupBySymbol(symbol,null));
	}
	@Override
	@RestMethod("getMember")
	public TreeNode getMember(String groupDn) throws ServiceException {
		String decodeDN=LdapUtils.decode(groupDn);
		checkGroup(decodeDN);
		LdapGroup group=groupService.getGroupByDN(decodeDN);
		List<?> result=searchService.searchByListLocalData(decodeDN);
		TreeNode node=new TreeNode(VmtGroup2LdapGroupAdapter.convert(group));
		if(CommonUtils.isNull(result)){
			return null;
		}else{
			for(Object obj:result){
				if(obj instanceof LdapUser){
					VmtUser vu=VmtUser2LdapUserAdapter.convert((LdapUser)(obj));
					if(vu!=null){
						if(group.isHideMobile()){
							vu.setTelephone(null);
						}
						node.addChildren(new TreeNode(vu));
					}
				}
			}
		}
		return node;
	}
	
	@Override
	@RestMethod("searchUserAttribute")
	public Map<String, String> searchUserAttribute(String dn,
			String attributeName) {
		return this.attrService.search(dn, attributeName);
	}
}
