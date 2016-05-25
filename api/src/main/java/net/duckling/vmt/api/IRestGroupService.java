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
import java.util.Map;

import net.duckling.vmt.api.domain.TreeNode;
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.api.domain.VmtUser;
import cn.vlabs.rest.ServiceException;

/**
 * 操作群组用的接口，定义了可用的方法
 * @author lvly
 * @since 2013-5-21
 */
public interface IRestGroupService {
	
	/**
	 *获得某一个人可见的群组列表，权限影响返回结构
	 *@param umtId 需要查询的用户的umtId
	 *@throws ServiceException
	 **/
	List<VmtGroup> getSbGroup(String umtId) throws ServiceException;
	/**
	 * 创建群组 <br>
	 * @param group 群组实体类
	 * @throws ServiceException <br>
	 * required:ErrorCode.FIELD_NULL		creator,name,symbol<br>
	 * validate:ErrorCode.SYMBOL_USED 		symbol已使用<br>
	 * 			ErrorCode.PARTTERN_ERROR	字段不符合规范<br>
	 * @return 生成的群组的dn
	 * */
	String create(VmtGroup group)throws ServiceException;
	/**
	 * 重命名
	 * 
	 * @param groupDN 要操作的群组的DN
	 * @param newName 要改成的新名字
	 * @throws ServiceException 
	 * validate:<br>
	 * 		ErrorCode.NOT_A_GROUP			groupDN必须是团队的dn，如果传入别的，则抛出<br>
	 * 		ErrorCode.DN_NOT_EXISTS			如果传入的dn无效，则抛出<br>
	 * */
	void rename(String groupDN,String newName)throws ServiceException;
	
	/**
	 * 删除群组
	 * @param groupDN 要删除的团队的DN
	 * @throws ServiceException
	 *	validata:<br>
	 * 		ErrorCode.NOT_A_GROUP			groupDN必须是团队的dn，如果传入别的，则抛出<br>
	 * 		ErrorCode.DN_NOT_EXISTS			如果传入的dn无效，则抛出<br>
	 * */
	void delete(String groupDN)throws ServiceException;
	
	/**
	 * 增加管理员
	 * @param groupDN
	 * @param umtId
	 * @throws ServiceException 
	 * validate：<br>
	 * 		ErrorCode.USER_NOT_EXISTS		提供的umtId对应的人并不在此团队内<br>
	 * 		ErrorCode.DN_NOT_EXISTS			提供的dn并不存在
	 * */
	void addAdminByUmtId(String groupDN,String umtId)throws ServiceException;
	/**
	 * 增加管理员
	 * @param groupDN
	 * @param umtId
	 * @throws ServiceException 
	 * validate：<br>
	 * 		ErrorCode.USER_NOT_EXISTS		提供的umtId对应的人并不在此团队内<br>
	 * 		ErrorCode.DN_NOT_EXISTS			提供的dn并不存在
	 * */
	void addAdminByCstnetId(String groupDN,String cstnetId)throws ServiceException;
	
	/**
	 * 删除管理员
	 * @param groupDN
	 * @param umtId
	 * @throws ServiceException 
	 * validate：<br>
	 * 		ErrorCode.DN_NOT_EXISTS			提供的dn并不存在
	 * */
	void removeAdminByUmtId(String groupDN,String umtId)throws ServiceException;
	/**
	 * 删除管理员,用邮箱删除
	 * @param groupDN
	 * @param umtId
	 * @throws ServiceException 
	 * validate：<br>
	 * 		ErrorCode.DN_NOT_EXISTS			提供的dn并不存在
	 * */
	void removeAdminByCstnetId(String groupDN,String cstnetId)throws ServiceException;
	
	/**
	 * 验证团队标识是否已使用
	 * @param symbol 团队标志
	 * @return 团队标志是否已使用
	 * */
	boolean hasSymbolUsed(String symbol)throws ServiceException;
	
	/**
	 * 获得第三方通过api创建的群组,通过umtId获得
 	 * @param from 用户来源，详见VmtGroup.FROM_XXX
 	 * @param umtId 用户umtId
 	 * @return 我创建的所有第三方群组
 	 * @throws ServiceException
 	 *  validate：<br>
	 * 		ErrorCode.FIELD_REQUIRED		umtId为空或者异常<br>
	 * 		ErrorCode.FIELD_EXPECT 			提供的from值异常，请参考VmtGroup.FROM_XXX
 	 */
 	List<VmtGroup> getMyThirdPartyGroupByUmtId(String from,String umtId)throws ServiceException;
 	
 	/**
 	 * 获得第三方通过api创建的群组,通过用户email获得，不推荐使用
 	 * @param from 用户来源，详见VmtGroup.FROM_XXX
 	 * @param cstnetId 用户邮箱
 	 * @return 我创建的所有第三方群组
 	 * @throws ServiceException
 	 *  validate：<br>
	 * 		ErrorCode.FIELD_REQUIRED		cstnetId异常，生成umtId失败
	 * 		ErrorCode.FIELD_EXPECT 			提供的from值异常，请参考VmtGroup.FROM_XXX
 	 * * */
 	List<VmtGroup> getMyThirdPartyGroupByCstnetId(String from,String cstnetId)throws ServiceException;
	
 	
 	/**
 	 * 判断群组名称是否被占用，正常来说是允许，名称相同的，根据应用，酌情使用
 	 * @param groupName 团队名称
 	 * @return (true|false)
 	 * <p> true:团队名称已被使用
 	 * <p> false:团队名称未被使用
 	 * @throws ServiceException
 	 */
 	boolean hasNameUsed(String groupName)throws ServiceException;
 	
 	
 	/**
 	 * 通过群组标识获得群组
 	 * @param symbol 群组标识
 	 * @return 找到则是群组对象，未找到，则是null
 	 * @throws ServiceException
 	 * validate：<br>
	 * 		ErrorCode.FIELD_REQUIRED		symbol为空
 	 */
 	VmtGroup getGroupBySymbol(String symbol)throws ServiceException;
 	
 	/**
 	 * 
 	 * 获得群组，但是这个数只有一个层级
 	 * @throws ServiceException
 	 * @param groupDn 群组的dn
 	 * validate：<br>
	 * 		ErrorCode.DN_NOT_EXISTS
	 * @return 树状结构，里面的对象只有一层，且全是VmtUser
 	 * */
 	TreeNode getMember(String groupDn)throws ServiceException;
 	/**
 	 *根据dn获得，获得某个群组的所有管理员
 	 *@param groupDn 群组的dn
 	 *@throws ServiceException
 	 * ErrorCode.DN_NOT_EXISTS 			dn不存在<br>
 	 * ErrorCode.NOT_A_GROUP			groupDN必须是团队的dn，如果传入别的，则抛出<br>
 	 * **/
	List<VmtUser> getAdmins(String groupDn) throws ServiceException;
	
	
	/**
	 * 更新团队信息
	 * @param group 更新团队属性,会触发MQ消息
	 * 				<br>group.symbol为主键,必填
	 * 				<br>group.description 团队描述
	 * @throws  ServiceException
	 * ErrorCode
	 * */
	
	void update(VmtGroup group) throws ServiceException;
	/**
	 * 查询组织下所有用户的属性值
	 * @param dn
	 * @param attributeName
	 * @return
	 * @throws ServiceException 
	 */
	Map<String,String> searchUserAttribute(String dn, String attributeName) throws ServiceException;
	
}
