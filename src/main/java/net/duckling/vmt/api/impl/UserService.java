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

import net.duckling.vmt.api.IRestUserService;
import net.duckling.vmt.api.domain.VmtApiApp;
import net.duckling.vmt.api.domain.VmtUser;
import cn.vlabs.rest.ServiceException;

/**
 * 操作用户Service
 * @author lvly
 * @since 2013-5-21
 */
@SuppressWarnings("unchecked")
public class UserService extends BaseService implements IRestUserService{
	/**
	 * Constructor
	 * @param baseUrl umt的serviceurl，如http://vmt.escience.cn/services
	 * */
	public UserService(String baseUrl)  {
		super(baseUrl);
	}

	@Override
	public boolean[] addKnownUserToDN(String pdn, String[] umtIds,boolean isActive)throws ServiceException {
		return (boolean[])sendService("User.addKnownUserToDN", new Object[]{pdn,umtIds,isActive});
	}

	@Override
	public boolean[] addUnkownUserToDN(String pdn, String[] cstnetIds,boolean isActive)throws ServiceException {
		return (boolean[])sendService("User.addUnkownUserToDN", new Object[]{pdn,cstnetIds,isActive});
	}
	@Override
	public void removeUser(String[] dns)throws ServiceException {
		sendService("User.removeUser", new Object[]{dns});
	}

	@Override
	public void rename(String dn, String newName)throws ServiceException {
		sendService("User.rename", new Object[]{dn,newName});
		
	}
	@Override
	public List<VmtUser> searchSbUser(String[] scopes, String umtId, String keyword)throws ServiceException {
		return (List<VmtUser>)sendService("User.searchSbUser", new Object[]{scopes,umtId,keyword});
	}
	
	@Override
	public List<VmtUser> searchSbAllUser(String umtId, String keyword)throws ServiceException {
		return (List<VmtUser>)sendService("User.searchSbAllUser", new Object[]{umtId,keyword});
	}
	@Override
	public List<VmtUser> searchUserByCstnetId(String dn, String[] cstnetId) throws ServiceException {
		return (List<VmtUser>)sendService("User.searchUserByCstnetId",new Object[]{dn,cstnetId});
	}@Override
	public List<VmtUser> searchUserByUmtId(String dn, String[] umtId) throws ServiceException {
		return ( List<VmtUser>)sendService("User.searchUserByUmtId",new Object[]{dn,umtId});
	}
	@Override
	public String[] getUmtIdByCstnetId(String[] cstnetIds)throws ServiceException  {
		return (String[])sendService("User.getUmtIdByCstnetId",new Object[]{cstnetIds});
	}
	@Override
	public VmtUser getUserByUmtId(String umtId) throws ServiceException {
		return (VmtUser)sendService("User.getUserByUmtId",new Object[]{umtId});
	}
	@Override
	public List<VmtUser> getUsersByUmtIds(String[] umtIds) throws ServiceException {
		return (List<VmtUser>)sendService("User.getUsersByUmtIds",new Object[]{umtIds});
	}
	@Override
	public List<VmtApiApp> getAppsByUmtId(String umtId) throws ServiceException {
		return (List<VmtApiApp>)sendService("User.getAppsByUmtId",new Object[]{umtId});
	}
	@Override
	public void updateByUmtId(String teamDN,VmtUser u) throws ServiceException {
		sendService("User.updateByUmtId",new Object[]{teamDN,u});
	}
}
