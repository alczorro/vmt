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
package net.duckling.vmt.api;

import java.util.List;

import net.duckling.vmt.api.domain.VmtApiApp;
import net.duckling.vmt.api.domain.VmtUser;
import cn.vlabs.rest.ServiceException;

/**
 * 定义了一些用户操作的方法
 * @author lvly
 * @since 2013-5-21
 */
public interface IRestUserService {
	/***
	 * 增加已知的用户，即知道umtId的用户，理论上速度快一些
	 * @param pdn	把这些用户加到哪里？
	 * @param umtIds 用户的umtId数组
	 * @param isActive 是否默认激活？
	 * @return (true,false)布尔数组，数量和umtIds相当
	 * <p>true	用户成功添加到指定节点
	 * <p>false 用户添加失败
	 * @throws ServiceException
	 * validate：<br>
	 * 		ErrorCode.DN_NOT_EXISTS			提供的dn并不存在
	 * */
	boolean[] addKnownUserToDN(String pdn,String[] umtIds,boolean isActive)throws ServiceException;
	/***
	 * 增加未知的用户，即不知道是不是umt用户，速度慢，但是限制少<br>
	 * 如果所查邮箱属于coreMail用户，则同步过来，生成umtId<br>
	 * 如果所查邮箱属于umt，则直接返回umtId<br>
	 * 如果所查邮箱两者都不是，则自动创建用户，生成umtId
	 * @param pdn	把这些用户加到哪里？
	 * @param cstnetIds 用户的邮箱数组
	 * @param isActive 是否默认激活？
	 * @return (true,false)布尔数组，数量和cstnetIds相当
	 * <p>true	用户成功添加到指定节点
	 * <p>false 用户添加失败
	 * @throws ServiceException
	 * validate：<br>
	 * 		ErrorCode.DN_NOT_EXISTS			提供的dn并不存在
	 * */
	boolean[] addUnkownUserToDN(String pdn,String[] cstnetIds,boolean isActive)throws ServiceException;
	
	/**
	 * 删除用户
	 * @param dns 用户的dn，可以同时删除多个用户
	 * @throws ServiceException
	 * */
	void removeUser(String[] dns)throws ServiceException ;
	
	/**
	 * 重命名指定用户
	 * @param dn 用户的dn
	 * @param newName 用户的新姓名
	 * @throws ServiceException
	 * */
	void rename(String dn,String newName)throws ServiceException;
	
	/**
	 * 查询用户,提供查询范围
	 * @param scopes 查询范围，群组的dn也可，组织机构的dn亦可
	 * @param umtId  要查询谁的结果，如果为空，则返回全部
	 * @param keyword 关键字,如果为空，则返回全部
	 * @return 查询结果
	 * @throws ServiceException
	 * */
	List<VmtUser> searchSbUser(String[] scopes,String umtId,String keyword)throws ServiceException;
	
	/**
	 * 查询用户,查询范围是用户可见所有组织群组
	 * @param umtId  要查询谁的结果，如果穿进去空，则返回全部
	 * @param keyword 关键字
	 * @return 查询结果
	 * @throws ServiceException
	 * */
	List<VmtUser> searchSbAllUser(String umtId,String keyword)throws ServiceException;
	
	
	/**
	 * 通过用户的umtId查询用户
	 * @param dn 并非仅限团队，也可以是部门，递归的树形查询
	 * @param umtId 用户的umtId
	 * @return 匹配所有用户
	 * @throws ServiceException
	 * validate：<br>
	 * 		ErrorCode.DN_NOT_EXISTS			提供的dn并不存在
	 */
	List<VmtUser> searchUserByCstnetId(String dn,String umtId[])throws ServiceException;
	/**
	 * 通过用户的邮箱地址查询用户
	 * @param dn 并非仅限团队，也可以是部门，递归的树形查询
	 * @param cstnetId 用户的邮箱地址
	 * @return 匹配的所有用户
	 * validate：<br>
	 * 		ErrorCode.DN_NOT_EXISTS			提供的dn并不存在
	 * */
	List<VmtUser> searchUserByUmtId(String dn, String[] umtId)throws ServiceException;
	
	/**
	 * 根据用户的邮箱，反向查出umtId
	 * @param cstnetIds
	 * @throws ServiceException 
	 */
	String[] getUmtIdByCstnetId(String[] cstnetIds) throws ServiceException;
	
	/**
	 * 根据用户umtId,查出用户信息，信息是umt的信息
	 * @param umtIds
	 * @return
	 * @throws ServiceException
	 * ErrorCode.USER_NOT_EXISTS 给出的umtId未查询到相应的用户
	 */
	List<VmtUser> getUsersByUmtIds(String[] umtIds)throws ServiceException;
	/**
	 * 根据用户umtId,查出用户信息，信息是umt的信息
	 * @param umtIds
	 * @return
	 * @throws ServiceException
	 * ErrorCode.USER_NOT_EXISTS 给出的umtId未查询到相应的用户
	 */
	VmtUser getUserByUmtId(String umtId)throws ServiceException;
	
	/**
	 * 根据用户的umtId,返回用户指定的appId,已经过排重,并过滤隐藏的应用
	 * @param umtIds
	 * @return 用户所属组织定义的应用
	 * @throws ServiceException 
	 * */
	List<VmtApiApp> getAppsByUmtId(String umtId) throws ServiceException;
	
	/**
	 * 根据用户的UmtId更新用户信息
	 * @param teamDN 组织机构或者群组的dn
	 * @param u 不能为空，umtId字段必须指定
	 * @throws ServiceException
	 * */
	void updateByUmtId(String teamDN,VmtUser u) throws ServiceException;
	
}
