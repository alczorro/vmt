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
package net.duckling.vmt.dao;

import java.util.List;

import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.exception.LdapOpeException;

/**
 * @author lvly
 * @since 2013-5-3
 */
public interface IUserDAO {
	 /**
	 * 往某个团队里面加入人
	 * @param dn 团队的dn
	 * @param umtIds 用户的umtIds
	 * @return 
	 * @throws LdapOpeException 
	 */
	boolean[] addUserToNode(String dn,List<LdapUser> users);
	
	/**
	 * 查询所有用户，精确查找
	 * @param letter 字母
	 * @param scope 用户所在的组
	 * @param isAdmin 
	 * */
	List<LdapUser> searchUsersByLetter(String letter,String[] scope,boolean canSeeUnConfirm[], boolean isAdmin);
	
	/**
	 * @param keyword
	 * @param scope
	 * @param isAdmin 
	 * @return
	 */
	List<LdapUser> searchUserByKeyword(String keyword, String[] scope,String[] domain,boolean canSeeUnConfirm[], boolean isAdmin);

	/**
	 * 查询所有用户，一次所有
	 * @param scopes 用户所在的组
	 * */
	List<LdapUser> searchUserByAll(String[] scopes,boolean[] canSee,boolean isAdmin);
	/**
	/**
	 * 删除用户
	 * @param dns
	 */
	void unbind(String[] dns);

	/**
	 * @param users
	 * @param moveto
	 * @return 
	 */
	boolean[] move(String[] users, String moveto);

	/**
	 * 判断激活用户的激活码是否正常
	 * @param random
	 * @param umtId
	 * @return
	 */
	LdapUser checkRandomOK(String teamDn,String random, String umtId);
	
	/**在子树里面存不存在这个人
	 * @param dn
	 * @param umtId
	 * @param getRefuse
	 * @return
	 */
	boolean isExistsSubTree(String dn,String umtId, boolean getRefuse);
	
	/**
	 * 用于判断这个dn里面有没有这个人
	 * @param dn
	 * @param umtId
	 * @return
	 */
	boolean isExistLevel(String dn,String umtId);
	
	/**
	 * 是否用户被其他机构使用了
	 * @param dn
	 * @return
	 */
	boolean isUserUsedInOrg(String umtId);
	/**
	 * @param baseDN
	 * @return
	 */
	int getSubUserCount(String baseDN);

	/**
	 * @param dn
	 * @return
	 */
	LdapUser getUser(String dn);

	/**
	 * @param decodeDN
	 * @param cstnetId
	 * @return
	 */
	List<LdapUser> searchUsersByCstnetId(String decodeDN, String[] cstnetId);

	/**
	 * @param pdn
	 * @param umtId
	 * @return
	 */
	List<LdapUser> searchUsersByUmtId(String pdn, String[] umtId);

	/**
	 * @param dn
	 * @param expectMeUmtId 
	 * @return
	 */
	List<LdapUser> searchUserByDN(String dn, String expectMeUmtId);

	/**
	 * 获取库里的所有用户
	 * @return
	 */
	List<LdapUser> searchUserAll();

	/**
	 * 获取
	 * @param decodeDN
	 * @param canLookUnConfirm
	 * @param admin
	 * @return
	 */
	int getSubUserCount(String decodeDN, boolean canLookUnConfirm, boolean admin);

	/**
	 * 查询coreMail用户
	 * @param pdn
	 * @return
	 */
	List<LdapUser> searchCoreMailUser(String pdn);
}
