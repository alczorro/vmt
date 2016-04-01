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
package net.duckling.vmt.api.impl;


import java.util.List;
import java.util.Map;

import net.duckling.vmt.api.IRestGroupService;
import net.duckling.vmt.api.domain.TreeNode;
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.api.domain.VmtUser;
import cn.vlabs.duckling.api.umt.rmi.exception.LoginFailed;
import cn.vlabs.rest.ServiceException;

/**
 * 
 * 操作群组用的Service
 * useage:<br>
 * 		IRestGroupService service=new GroupService("http://vmt.escience.cn/services");<br>
 *		VmtGroup group = new VmtGroup();
 *		group.setSymbol("123");
 *		group.setName("api test group");
 *		group.setCreator("10000201");
 *		service.create(group);
 *		....
 * @author lvly
 * @since 2013-5-20
 */
@SuppressWarnings("unchecked")
public class GroupService extends BaseService implements IRestGroupService{
	/**
	 * Constructor
	 * @param baseUrl umt的serviceurl，如http://vmt.escience.cn/services
	 * @throws LoginFailed 与vmt发生通讯错误
	 * */
	public GroupService(String baseUrl){
		super(baseUrl);
	}
	@Override
	public List<VmtUser> getAdmins(String groupDn)  throws ServiceException{
		return (List<VmtUser>)sendService("Group.getAdmins", new Object[]{groupDn});
	}
	
	@Override
	public String create(VmtGroup group)  throws ServiceException{
		return (String)sendService("Group.create", new Object[]{group});
	}

	@Override
	public void rename(String groupDN, String newName)  throws ServiceException{
		sendService("Group.rename", new Object[]{groupDN,newName});
	}

	@Override
	public void delete(String groupDN)  throws ServiceException{
		sendService("Group.delete", new Object[]{groupDN});
	}
	@Override
	public void addAdminByUmtId(String groupDN,String umtId)  throws ServiceException{
		sendService("Group.addAdminByUmtId", new Object[]{groupDN,umtId});
	}
	@Override
	public void addAdminByCstnetId(String groupDN, String cstnetId) throws ServiceException {
		sendService("Group.addAdminByCstnetId", new Object[]{groupDN,cstnetId});
		
	}
	@Override
	public void removeAdminByUmtId(String groupDN, String umtId) throws ServiceException {
		sendService("Group.removeAdminByUmtId", new Object[]{groupDN,umtId});
		
	}
	@Override
	public void removeAdminByCstnetId(String groupDN, String cstnetId) throws ServiceException {
		sendService("Group.removeAdminByCstnetId", new Object[]{groupDN,cstnetId});
	}
	
	@Override
	public List<VmtGroup> getSbGroup( String umtId)  throws ServiceException {
		return (List<VmtGroup>)sendService("Group.getSbGroup", new Object[]{umtId});
	}
	@Override
	public boolean hasSymbolUsed(String symbol) throws ServiceException {
		return (boolean)sendService("Group.hasSymbolUsed", new Object[]{symbol});
	}
	@Override
	public List<VmtGroup> getMyThirdPartyGroupByCstnetId(String from, String cstnetId) throws ServiceException{
		return (List<VmtGroup>)sendService("Group.getMyThirdPartyGroupByCstnetId", new Object[]{from,cstnetId});
	}
	@Override
	public List<VmtGroup> getMyThirdPartyGroupByUmtId(String from, String umtId)throws ServiceException {
		return (List<VmtGroup>)sendService("Group.getMyThirdPartyGroupByUmtId", new Object[]{from,umtId});
	}
	@Override
	public boolean hasNameUsed(String groupName) throws ServiceException {
		return (boolean)sendService("Group.hasNameUsed", new Object[]{groupName});
	}
	@Override
	public VmtGroup getGroupBySymbol(String symbol) throws ServiceException {
		return (VmtGroup)sendService("Group.getGroupBySymbol", new Object[]{symbol});
	}
	@Override
	public TreeNode getMember(String groupDn) throws ServiceException {
		return (TreeNode)sendService("Group.getMember", new Object[]{groupDn});
	}
	@Override
	public void update(VmtGroup group) throws ServiceException {
		sendService("Group.update", group);
	}
	@Override
	public Map<String, String> searchUserAttribute(String dn,
			String attributeName) throws ServiceException {
		return (Map<String,String>)sendService("Group.searchUserAttribute", new Object[]{dn, attributeName});
	}

}
