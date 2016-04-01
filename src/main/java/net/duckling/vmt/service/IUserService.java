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
package net.duckling.vmt.service;

import java.util.List;

import net.duckling.vmt.domain.coremail.CoreMailUser;
import net.duckling.vmt.domain.ldap.LdapUser;
import cn.vlabs.umt.oauth.UserInfo;

/**
 * 用户管理
 * @author lvly
 * @since 2013-5-2
 */
public interface IUserService {
	
	/**
	 * 在某个节点增加用户（在umt已知用户）
	 * @param dn
	 * @param umtId 需要增加的umtId
	 * @param status 初始化状态
	 * @param checkIsExists 确认是否已存在
	 * @return
	 */
	boolean[] addUserToNodeUsed(String dn,String umtId[],String status,boolean checkIsExists);
	String addUserToNode(String destDn,LdapUser user,UserInfo userInfo);
	/**
	 * 在某个节点增加用户（未生成umtId的用户）
	 * @param dn 
	 * @param cstnetId
	 * @param listlink
	 * @param userInfo
	 * @param status
	 * @param sendMail
	 * @return
	 */
	boolean[] addUserToNodeUnUsed(String dn,String cstnetId[],UserInfo userInfo,String status,boolean sendMail,boolean checkIsExists);
	
	/**
	 * @param dn
	 * @param users
	 * @return
	 */
	boolean[] addUserToNodeCoreMail(String dn,List<CoreMailUser> users);
	/**
	 * 查询所有用户，一次所有
	 * @param scopes 用户所在的组
	 * */
	List<LdapUser> searchUsersByAll(String[] scopes,String umtId);
	/**
	 * 查询所有用户，精确查找
	 * @param letter 字母
	 * @param scope 用户所在的组
	 * */
	List<LdapUser> searchUsersByLetter(String letter,String[] scope,String umtId);
	
	/**
	 * 根据用户umtId查询用户Id，一般取名字什么的时候用
	 * @param pdn 查询的根路径，可以是任何dn
	 * @param umtId 用户的umtId
	 * @return
	 */
	List<LdapUser> searchUsersByUmtId(String pdn,String[] umtId);
	
	/**
	 *查询所有用户，用关键字
	 *@param keyword 关键字
	 **/
	List<LdapUser> searchUsersByKeyword(String keyword,String[] scope,String umtId);
	List<LdapUser> searchUsersByKeyword(String keyword,String[]scope,String[] domain,String umtId);
	/**
	 * 内部同步数据或者，啥时候用，请勿在controller或者别的地方调用
	 * */
	List<LdapUser> searchUsersAll();
	
	/**
	 * 查询某个团队下的所有用户
	 * @param dn
	 * @return
	 */
	List<LdapUser> searchUsersByDN(String dn,String expectUmtId);
	/**
	 * 删除指定用户
	 * @param dns 多个用户的dn
	 */
	void unbind(String[] dns);
	/**
	 * @param decode
	 * @param moveToDn
	 */
	void move(String[] dns, String moveToDn,boolean isExtendDest);
	/**
	 * 增加管理员用户
	 * @param dn 路径
	 * @param umtId umtId
	 * */
	boolean addAdmin(String dn, String umtId);
	/**
	 * 删除管理员
	 * @param dn 路径
	 * @param umtId
	 * */
	void removeAdmin(String dn, String umtId);
	
	/**
	 * 查看用户的激活码是否正确
	 * @param random
	 * @param umtId
	 * @return 正确用户的DN
	 * */
	LdapUser checkRandomOK(String teamDn,String random, String umtId);
	
	/**
	 * 改dn下是否存在当前用户
	 * */
	boolean isExistsSubTree(String dn,String umtId,boolean expectRefuse);
	
	/**
	 * 改变团队的用户数量
	 * @param dn 团队dn
	 * @param plus 增加多少，可以是负数，表示减少
	 * */
	void plusCount(String dn,int plus);
	
	/**
	 * 
	 * 查询当前dn下面有多少用户
	 * @param baseDn 欲查询的dn
	 * */
	int getSubUserCount(String baseDN);
	/**
	 * 
	 * 获得下属人员数量
	 * @param decodeDN
	 * @param umtId
	 * @return
	 */
	int getSubUserCount(String decodeDN, String umtId);
	
	
	/**
	 * 通过dn获得用户
	 * @param dn 用户dn
	 * @return
	 */
	LdapUser getUserByDN(String dn);
	/**
	 * 重新发送激活邮件
	 * @param dn
	 * @param userInfo
	 */
	void resend(LdapUser  user, UserInfo userInfo);
	
	/**
	 * 获得我可见的团队dn的列表
	 * @param umtId 登录用户id
	 * @return
	 */
	String[] getMyAccessableTeam(String umtId);
	/**
	 * @param decodeDN
	 * @param cstnetId
	 * @return
	 */
	List<LdapUser> searchUsersByCstnetId(String decodeDN, String cstnetId[]);
	/**
	 * 批量删除用户，
	 * @param dn
	 * @param cstnetId
	 */
	void deleteUserBatch(String dn, String[] cstnetId);
	/**
	 * 判断是否是空的，或者只有他自己
	 * @param key
	 * @return
	 */
	boolean isNullOrSelf(String key);
	/**
	 * 根据umtId查询一个用户
	 * @param dbOrgBaseDn
	 * @param umtId
	 */
	LdapUser searchUserByUmtId(String baseDn, String umtId);
	
	/**
	 * 更新用户
	 * @param user
	 */
	void updateUser(LdapUser user);
	
	void batchUpdateUser(LdapUser user,String[] needUpdate);
	
	/**
	 * 判断下属是否存在于coreMail用户
	 * @param pdn
	 * @return
	 */
	boolean checkHasCoreMailUser(String pdn);
	
	/***
	 * 判断是否可以操作coreMail字段,逻辑是,是否域名相同
	 * @dn 用户的dn
	 * */
	boolean isCoreMailUserInCoreMailOrg(LdapUser user);
	
	
	boolean isGlobalLoginNameExists(String loginName);
	
	
}
