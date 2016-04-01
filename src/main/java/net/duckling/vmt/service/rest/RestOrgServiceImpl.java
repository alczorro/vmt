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
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.api.IRestOrgService;
import net.duckling.vmt.api.domain.ErrorCode;
import net.duckling.vmt.api.domain.TreeNode;
import net.duckling.vmt.api.domain.VmtDepart;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.api.domain.VmtOrgDomain;
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.api.util.NameRuleUtils;
import net.duckling.vmt.common.adapter.VmtDepart2LdapDepartAdapter;
import net.duckling.vmt.common.adapter.VmtOrg2LdapOrgAdapter;
import net.duckling.vmt.common.adapter.VmtUser2LdapUserAdapter;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.index.OrgDetail;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ICommonService;
import net.duckling.vmt.service.IDepartmentService;
import net.duckling.vmt.service.INodeService;
import net.duckling.vmt.service.IOrgDomainMappingService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.ISearchService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.server.annotation.RestMethod;
/**
 * The Service of the Org Object
 * @author lvly
 * @since 2013-5-20
 */
public class RestOrgServiceImpl implements IRestOrgService{
	private IOrgService orgService;
	private IAttributeService attrService;
	private ICommonService commonService;
	private INodeService nodeService;
	private IUserService userService;
	private IDepartmentService departService;
	private ISearchService searchService;
	private IVmtIndexService indexService;
	private MQMessageSenderExt sender;
	private IOrgDomainMappingService domainService;
	/**
	 * 
	 */
	public RestOrgServiceImpl(){
		orgService=BeanFactory.getBean(IOrgService.class);
		attrService=BeanFactory.getBean(IAttributeService.class);
		commonService=BeanFactory.getBean(ICommonService.class);
		nodeService=BeanFactory.getBean(INodeService.class);
		userService=BeanFactory.getBean(IUserService.class);
		departService=BeanFactory.getBean(IDepartmentService.class);
		searchService=BeanFactory.getBean(ISearchService.class);
		indexService=BeanFactory.getBean(IVmtIndexService.class);
		sender=BeanFactory.getBean(MQMessageSenderExt.class);
		domainService=BeanFactory.getBean(IOrgDomainMappingService.class);
	}
	@Override
	@RestMethod("create")
	public String create(VmtOrg org)throws ServiceException{
		if(org==null){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"can not create null org");
		}
		if(CommonUtils.isNull(org.getName())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"the orgName must be not null");
		}
		if(CommonUtils.isNull(org.getCreator())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"thr org creator must be not null");
		}
		if(CommonUtils.isNull(org.getSymbol())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"the orgSymbol must be not null");
		}
		if(hasSymbolUsed(org.getSymbol())){
			throw new ServiceException(ErrorCode.SYMBOL_USED,"the org symbol used!:"+org.getSymbol());
		}
		if(!NameRuleUtils.isNodeIDMatch(org.getName())){
			throw new ServiceException(ErrorCode.PATTERN_ERROR,"the orgname is not match to nodeIdRule");
		}
		if(!NameRuleUtils.isSymbolMatch(org.getSymbol())){
			throw new ServiceException(ErrorCode.PATTERN_ERROR,"the ortsymbol is not match to symbol rule");
		}
		LdapOrg ldapOrg=VmtOrg2LdapOrgAdapter.convert(org);
		orgService.createOrg(ldapOrg,true);
		sender.sendCreateOrgMessage(ldapOrg);
		return ldapOrg.getDn();
	}
	@Override
	@RestMethod("rename")
	public void rename(String orgDn,String newName) throws ServiceException{
		String decodeDN=LdapUtils.decode(orgDn);
		checkIsOrg(decodeDN);
		if(!commonService.isExist(orgDn)){
			throw new ServiceException(ErrorCode.DN_NOT_EXISTS,"this org's dn is not exists");
		}
		String beforeName=CommonUtils.first(attrService.get(decodeDN, "vmt-name"));
		attrService.update(decodeDN, "vmt-name", newName);
		sender.sendUpdateMessage(decodeDN,beforeName);
		nodeService.updateSonAndSelfDisplayName(decodeDN, newName);
	}
	
	@Override
	@RestMethod("delete")
	public void delete(String orgDN)throws ServiceException{
		String decodeDN=LdapUtils.decode(orgDN);
		checkIsOrg(decodeDN);
		if(!commonService.isExist(orgDN)){
			throw new ServiceException(ErrorCode.DN_NOT_EXISTS,"this dn is not exists");
		}
		sender.sendUnbindMessage(decodeDN);
		commonService.unbind(decodeDN);
		indexService.deleteIndex(decodeDN);
	}
	@Override
	@RestMethod("addAdmin")
	public void addAdmin(String orgDN,String umtId) throws ServiceException{
		String decodeDN=LdapUtils.decode(orgDN);
		checkIsOrg(decodeDN);
		if(!userService.isExistsSubTree(decodeDN, umtId,true)){
			throw new ServiceException(ErrorCode.USER_NOT_EXISTS,"user not exits in this org:"+umtId);
		}
		userService.addAdmin(decodeDN, umtId);
		sender.sendUpdateMessage(decodeDN);
	}
	@Override
	@RestMethod("getSbOrg")
	public List<VmtOrg> getSbOrg(String umtId) throws ServiceException {
		
		return VmtOrg2LdapOrgAdapter.convert(orgService.getMyOrgsAll(umtId));
	}
	
	@Override
	@RestMethod("hasSymbolUsed")
	public boolean hasSymbolUsed(String symbol) throws ServiceException {
		return orgService.isExists(symbol);
	}
	@Override
	@RestMethod("addDepartment")
	public String addDepartment(String pdn,VmtDepart depart) throws ServiceException {
		String decodeDN=LdapUtils.decode(pdn);
		checkIsOrg(decodeDN);
		if(CommonUtils.isNull(depart.getCreator())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED, "the depart's creator required");
		}
		if(CommonUtils.isNull(depart.getName())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"the depart's name required");
		}
		if(CommonUtils.isNull(depart.getSymbol())){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"the depart's symbol required");
		}
		if(hasDepartNameUsed(pdn, depart.getName())){
			throw new ServiceException(ErrorCode.NAME_USED,"the depart's name ["+depart.getName()+"] is used");
		}
		if(hasDepartSymbolUsed(pdn, depart.getSymbol())){
			throw new ServiceException(ErrorCode.SYMBOL_USED,"the depart's symbol ["+depart.getSymbol()+"] is used");
		}
		if(!NameRuleUtils.isNodeIDMatch(depart.getName())){
			throw new ServiceException(ErrorCode.PATTERN_ERROR,"the name is not match to nodeIdRule");
		}
		if(!NameRuleUtils.isSymbolMatch(depart.getSymbol())){
			throw new ServiceException(ErrorCode.PATTERN_ERROR,"the ortsymbol is not match to symbolrule");
		}
		
		LdapDepartment ldapDepart=VmtDepart2LdapDepartAdapter.convert(depart);
		departService.create(decodeDN,ldapDepart );
		sender.sendCreateDeptMessage(ldapDepart, decodeDN);
		return ldapDepart.getDn();
	}
	private void checkIsOrg(String decodeDN)throws ServiceException{
		if(decodeDN==null){
			throw new ServiceException(ErrorCode.FILELD_REQUIRED,"dn is Null");
		}
		if(!LdapUtils.isOrgSub(decodeDN)){
			throw new ServiceException(ErrorCode.NOT_A_ORG, "this dn is not a org");
		}
		if(!commonService.isExist(decodeDN)){
			throw new ServiceException(ErrorCode.DN_NOT_EXISTS,"this dn is not exists");
		}
	}
	@Override
	@RestMethod("removeDepartment")
	public int removeDepartment(String departDN) throws ServiceException {
		String decodeDN=LdapUtils.decode(departDN);
		checkIsOrg(decodeDN);
		int count=userService.getSubUserCount(decodeDN);
		sender.sendUnbindMessage(decodeDN);
		commonService.unbind(decodeDN);
		userService.plusCount(decodeDN, -1*count);
		
		return count;
	}
	@Override
	@RestMethod("renameDepartment")
	public void renameDepartment(String departDN, String newName) throws ServiceException {
		String decodeDN=LdapUtils.decode(departDN);
		checkIsOrg(decodeDN);
		if(hasDepartNameUsed(LdapUtils.getParent(decodeDN), newName)){
			throw new ServiceException(ErrorCode.NAME_USED,"the name["+newName+"] is used");
		}
		String beforeName=CommonUtils.first(attrService.get(decodeDN, "vmt-current-display"));
		attrService.update(decodeDN, "vmt-name", newName);
		nodeService.updateSonAndSelfDisplayName(decodeDN, newName);
		sender.sendUpdateMessage(decodeDN,beforeName);
	}
	@Override
	@RestMethod("isDepartNameUsed")
	public boolean hasDepartNameUsed(String pdn, String departName) throws ServiceException {
		String decodeDN=LdapUtils.decode(pdn);
		checkIsOrg(decodeDN);
		return departService.isNameExists(decodeDN, departName);
	}
	
	@Override
	@RestMethod("isDepartSymbolUsed")
	public boolean hasDepartSymbolUsed(String pdn, String symbol) throws ServiceException {
		String decodeDN=LdapUtils.decode(pdn);
		checkIsOrg(decodeDN);
		return nodeService.isSymbolUsed(LdapUtils.getDN(decodeDN, 2), symbol);
		
	}
	@Override
	@RestMethod("move")
	public void move(String pdn, String[] dns) throws ServiceException {
		String decodeDN=LdapUtils.decode(pdn);
		String userDn[]=LdapUtils.decode(dns);
		checkIsOrg(decodeDN);
		sender.sendMoveUserMessage(userDn, decodeDN);
		userService.move(userDn,decodeDN,true);
	}
	
	@RestMethod("getAllUsers")
	@Override
	public List<VmtUser> getAllUsers(String orgDN) throws ServiceException {
		String decodeDN=LdapUtils.decode(orgDN);
		checkIsOrg(decodeDN);
		LdapOrg org=orgService.getOrgByDN(decodeDN);
		
		if(!commonService.isExist(decodeDN)){
			throw new ServiceException(ErrorCode.DN_NOT_EXISTS,"this dn"+decodeDN+" is not exists");
		}
		List<LdapUser> searchedUsers=userService.searchUsersByAll(new String[]{decodeDN},null);
		if(org.isHideMobile()){
			setTelephoneNull(searchedUsers);
		}
		return VmtUser2LdapUserAdapter.convert(searchedUsers);
	}
	protected void setTelephoneNull(List<LdapUser> users){
		if(CommonUtils.isNull(users)){
			for(LdapUser u:users){
				u.setTelephone(null);
			}
		}
	}
	@Override
	@RestMethod("getTree")
	public TreeNode getTree(String targetDN) throws ServiceException {
		String decodeDN=LdapUtils.decode(targetDN);
		checkIsOrg(decodeDN);
		LdapOrg org=orgService.getOrgByDN(decodeDN);
		TreeNode node=new TreeNode(VmtOrg2LdapOrgAdapter.convert(org));
		generateTree(node, org.getDn(),org.isHideMobile());
		return node;
	}
	private void generateTree(TreeNode treeNode,String nextDn,boolean hideMobile){
		List<?> result=searchService.searchByListLocalData(nextDn);
		if(CommonUtils.isNull(result)){
			return;
		}else{
			for(Object obj:result){
				if(obj instanceof LdapUser){
					
					VmtUser vu=VmtUser2LdapUserAdapter.convert((LdapUser)(obj));
					if(vu!=null){
						if(hideMobile){
							vu.setTelephone(null);
						}
						treeNode.addChildren(new TreeNode(vu));
					}
				}else if(obj instanceof LdapDepartment){
					VmtDepart dept=VmtDepart2LdapDepartAdapter.convert((LdapDepartment)(obj));
					TreeNode node=new TreeNode(dept);
					treeNode.addChildren(node);
					generateTree(node,dept.getDn(),hideMobile);
				}
			}
		}
	}

	@Override
	@RestMethod("getChild")
	public List<?> getChild(String dn) throws ServiceException {
		String decodeDN=LdapUtils.decode(dn);
		checkIsOrg(decodeDN);
		List<?> result=searchService.searchByListLocalData(decodeDN);
		if(CommonUtils.isNull(result)){
			return result;
		}
		LdapOrg org=orgService.getOrgByDN(decodeDN);
		List<Object> searchResult=new ArrayList<Object>();
		for(Object obj:result){
			if(obj instanceof LdapDepartment){
				VmtDepart dept=VmtDepart2LdapDepartAdapter.convert((LdapDepartment)obj);
				searchResult.add(dept);
			}else if(obj instanceof LdapUser){
				VmtUser user=VmtUser2LdapUserAdapter.convert((LdapUser)obj);
				if(user!=null){
					if(org.isHideMobile()){
						user.setTelephone(null);
					}
					searchResult.add(user);
				}
			}
		}
		return searchResult;
	}
	@Override
	@RestMethod("getOrgByDomain")
	public VmtOrg getOrgByDomain(String domain) throws ServiceException {
		return VmtOrg2LdapOrgAdapter.convert(orgService.getOrgByDomain(CommonUtils.trim(domain)));
	}
	@RestMethod("getAllDomains")
	@Override
	public List<VmtOrgDomain> getAllDomains() throws ServiceException {
		List<OrgDetail> details= domainService.findAllOrgDetails();
		if(CommonUtils.isNull(details)){
			return null;
		}
		List<VmtOrgDomain> result=new ArrayList<VmtOrgDomain>();
		for(OrgDetail detail:details){
			VmtOrgDomain domain=new VmtOrgDomain();
			domain.setCas(detail.isCas());
			domain.setCoreMail(detail.isCoreMail());
			domain.setDomains(detail.getDomainsByArray());
			domain.setOrgName(detail.getName());
			domain.setOrgSymbol(detail.getSymbol());
			domain.setType(detail.getType());
			result.add(domain);
		}
		return result;
	}
	
	@RestMethod("searchUserAttribute")
	@Override
	public Map<String, String> searchUserAttribute(String dn,
			String attributeName) {
		return attrService.search(dn, attributeName);
	}
}
