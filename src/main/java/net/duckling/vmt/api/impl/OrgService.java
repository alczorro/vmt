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

import net.duckling.vmt.api.IRestOrgService;
import net.duckling.vmt.api.domain.TreeNode;
import net.duckling.vmt.api.domain.VmtDepart;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.api.domain.VmtOrgDomain;
import net.duckling.vmt.api.domain.VmtUser;
import cn.vlabs.duckling.api.umt.rmi.exception.LoginFailed;
import cn.vlabs.rest.ServiceException;

/**
 * 操作组织机构的服务类
 * @author lvly
 * @since 2013-5-21
 */
@SuppressWarnings("unchecked")
public class OrgService extends BaseService implements IRestOrgService{
	/**
	 * Constructor
	 * @param baseUrl umt的serviceurl，如http://vmt.escience.cn/services
	 * @throws LoginFailed 与vmt发生通讯错误
	 * */
	public OrgService(String baseUrl)  {
		super(baseUrl);
	}
	@Override
	public List<VmtOrg> getSbOrg(String umtId)throws ServiceException {
		return (List<VmtOrg>) sendService("Org.getSbOrg", umtId);
	}

	@Override
	public String create(VmtOrg org)throws ServiceException {
		return (String)sendService("Org.create", org);
	}

	@Override
	public void rename(String orgDN, String newName)throws ServiceException {
		sendService("Org.rename", new Object[]{orgDN,newName});
		
	}

	@Override
	public void delete(String orgDN) throws ServiceException{
		sendService("Org.delete", new Object[]{orgDN});
		
	}

	@Override
	public void addAdmin(String orgDN, String umtId) throws ServiceException{
		sendService("Org.addAdmin", new Object[]{orgDN,umtId});
		
	}

	@Override
	public boolean hasSymbolUsed(String symbol) throws ServiceException {
		return (boolean)sendService("Org.hasSymbolUsed", new Object[]{symbol});
	}

	@Override
	public String addDepartment(String pdn,VmtDepart depart) throws ServiceException{
		return (String)sendService("Org.addDepartment",new Object[]{pdn, depart});
	}

	@Override
	public int removeDepartment(String departDN)throws ServiceException {
		return (int)sendService("Org.removeDepartment", departDN);
		
	}
	
	@Override
	public void renameDepartment(String departDN, String newName)throws ServiceException {
		sendService("Org.renameDepartment", new Object[]{departDN,newName});
	}
	@Override
	public boolean hasDepartNameUsed(String pdn, String departName)throws ServiceException {
		return (boolean)sendService("Org.isDepartNameUsed", new Object[]{pdn,departName});
	}
	@Override
	public boolean hasDepartSymbolUsed(String pdn, String symbol)throws ServiceException {
		return (boolean)sendService("Org.isDepartSymbolUsed", new Object[]{pdn,symbol});
	}
	@Override
	public void move(String pdn, String[] dns)throws ServiceException{
		sendService("Org.move", new Object[]{pdn,dns});
	}
	@Override
	public List<VmtUser> getAllUsers(String orgDN)throws ServiceException {
		return (List<VmtUser>)sendService("Org.getAllUsers",orgDN);
	}
	@Override
	public TreeNode getTree(String targetDN) throws ServiceException {
		return (TreeNode)sendService("Org.getTree",targetDN);
	}
	@Override
	public List<?> getChild(String dn) throws ServiceException {
		return (List<?>)sendService("Org.getChild",dn);
	}
	@Override
	public VmtOrg getOrgByDomain(String domain) throws ServiceException {
		return (VmtOrg)sendService("Org.getOrgByDomain",domain);
	}
	@Override
	public List<VmtOrgDomain> getAllDomains() throws ServiceException {
		return (List<VmtOrgDomain>)sendService("Org.getAllDomains", null);
	}
	@Override
	public Map<String, String> searchUserAttribute(String dn,
			String attributeName) throws ServiceException {
		return (Map<String,String>)sendService("Org.searchUserAttribute", new Object[]{dn, attributeName});
	}
}
