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
import net.duckling.vmt.api.domain.VmtDepart;
import net.duckling.vmt.api.domain.VmtOrg;
import net.duckling.vmt.api.domain.VmtOrgDomain;
import net.duckling.vmt.api.domain.VmtUser;
import cn.vlabs.rest.ServiceException;

/**
 * 定义了一些对组织机构的操作
 * @author lvly
 * @since 2013-5-21
 */
public interface IRestOrgService {
	/**
	 *获得一个人可见的组织,权限和是否是管理员，影响结果集
	 *@param umtId	某个人的umtId
	 *@throws ServiceException 
	 **/
	List<VmtOrg> getSbOrg(String umtId) throws ServiceException;
	
	/**
	 * 获得一个组织下的所有用户
	 * @param orgDN 组织的dn
	 * @throws ServiceException
	 * validate:ErrorCode.DN_NOT_EXISTS		dn不存在<br>
	 * validate:ErrorCode.NOT_A_ORG			所提供的DN并不是一个有效的机构DN<br>
	 * @return 所有用户
	 * */
	List<VmtUser> getAllUsers(String orgDN) throws ServiceException;
	
	/**
	 * 创建组织机构
	 * @param org 群组实体类
	 * @return 生成的dn
	 * @throws ServiceException <br>
	 * required:ErrorCode.FIELD_NULL		creator,name,symbol<br>
	 * validate:ErrorCode.SYMBOL_USED 		symbol已使用<br>
	 * 			ErrorCode.PATTERN_ERROR		字段不符合规范<br>
	 * @return 生成的组织的dn
	 * */
	String create(VmtOrg org)throws ServiceException;
	/**
	 * 重命名组织机构
	 * @param orgDN	要改名的组织机构DN
	 * @param newName 要改成的新名字
	 * @throws ServiceException <br>
	 * validate:ErrorCode.DN_NOT_EXISTS		dn不存在<br>
	 * validate:ErrorCode.NOT_A_ORG			所提供的DN并不是一个有效的机构DN<br>
	 * 
	 * */
	void rename(String orgDN,String newName)throws ServiceException;
	
	/**
	 * 删除一个组织机构
	 * @param orgDN 要删除的组织机构的DN
	 * @throws ServiceException<br>
	 * validate:ErrorCode.DN_NOT_EXISTS		dn不存在<br>
	 * validate:ErrorCode.NOT_A_ORG			所提供的DN并不是一个有效的机构DN<br>
	 * */
	void delete(String orgDN)throws ServiceException;
	
	/**
	 * 给一个组织机构增加管理员，必须是组织的成员
	 * @param orgDN  目标组织机构的DN
	 * @param umtId	 期望加入的管理员的umtId
	 * @throws ServiceException<br>
	 * validate:ErrorCode.NOT_A_ORG			所提供的DN并不是一个有效的机构DN<br>
	 * validate:ErrorCode.USER_NOT_EXISTS	所提供的umtId并不在当前组织里面<br>
	 * */
	void addAdmin(String orgDN,String umtId)throws ServiceException;
	
	/**
	 * 判断组织机构的标识是否已被占用
	 * @param symbol 期望使用的组织标识
	 * @return (true,false)
	 * <p>true:已被使用
	 * <p>false:可以使用
	 * */
	boolean hasSymbolUsed(String symbol)throws ServiceException;
	
	/***
	 * 增加一个部门
	 * @param pdn 期望加到哪里，路径，如果为orgDN，那么就会加入到根
	 * @param depart 加入的部门信息
	 * @return 返回生成的部门dn
	 * @throws ServiceException <br>
	 * required:ErrorCode.FILELD_REQUIRED	creator,name,symbol<br>
	 * validate:ErrorCode.NOT_A_ORG			提供的部门不是标准的组织dn<br>
	 * validate:ErrorCode.NAME_USED			部门名称已被使用<br>
	 * validate:ErrorCode.SYMBOL_USED		部门标识已被使用<br>
	 * validate:ErrorCode.PARTTERN_ERROR	字段名称不符合规范<br>
	 * */
	String addDepartment(String pdn,VmtDepart depart)throws ServiceException;
	
	
	/***
	 * 删除部门
	 * @param departDN	 部门的dn
	 * @return 返回删除的人员数量
	 * @throws ServiceException<br>
	 * validate:ErrorCode.NOT_A_ORG			提供的部门不是标准的组织dn
	 * */
	int removeDepartment(String departDN) throws ServiceException;
	
	/**
	 * 重命名部门
	 * @param departDN 期望重命名的部门的DN
	 * @param newName  期望改成的名字
	 * @throws ServiceException<br>
	 * validate:ErrorCode.NOT_A_ORG			提供的部门不是标准的组织dn<br>
	 * validate:ErrorCode.NAME_USED			名字已被别人使用
	 * */
	void renameDepartment(String departDN,String newName) throws ServiceException;
	
	/**
	 * 判断部门名字是否可以使用
	 * @param pdn 期望探知的部门父路径
	 * @param departName 期望探知的部门名
	 * @return (true,false)
	 * 		<p>true：已经被使用
	 * 		<p>false:可以使用
	 * @throws ServiceException<br>		
	 * validate:ErrorCode.NOT_A_ORG			提供的部门不是标准的组织dn<br>
	 * */
	boolean hasDepartNameUsed(String pdn,String departName)throws ServiceException;
	
	
	/**
	 * 判断一个部门标识是否被使用
	 * @param pdn 期望探知的部门父路径
	 * @param symbol 期望探知的部门标识
	 * @return (true,false)
	 * 		<p>true：已经被使用
	 * 		<p>false:可以使用
	 * @throws ServiceException<br>		
	 * validate:ErrorCode.NOT_A_ORG			提供的部门不是标准的组织dn<br>
	 * */
	boolean hasDepartSymbolUsed(String pdn,String symbol)throws ServiceException;
	
	
	/**
	 * 移动人员(或者部门，部门功能暂时未开放)
	 * @param targetDN 	目标dn，如果成功则被移动到这里
	 * @param dns		用户(或者部门，部门功能暂时未开放)的dns;
	 * @throws ServiceException<br>		
	 * validate:ErrorCode.NOT_A_ORG			提供的部门不是标准的组织dn<br>
	 * */
	void move(String targetDN,String dns[])throws ServiceException;
	
	/**
	 * 获得树状结构,
	 * @param targetDN 是组织的根节点
	 * @throws ServiceException<br>		
	 * validate:ErrorCode.NOT_A_ORG			提供的dn不是标准的组织dn<br>
	 * ErrorCode.DN_NOT_EXISTS		dn不存在<br>
	 * @return  树状的TreeNode
	 */
	TreeNode getTree(String targetDN)throws ServiceException;
	
	/***
	 * 获得某个DN的下一个实体，有可能是VmtDepart或者VmtUser
	 * 
	 * @param dn 父目录的dn
	 * @throws ServiceException<br>		
	 * validate:
	 *  ErrorCode.NOT_A_ORG			提供的dn不是标准的组织dn<br>
	 * 	ErrorCode.DN_NOT_EXISTS		dn不存在<br>
	 *  ErrorCode.FILELD_REQUIRED   
	 * @return  获得下级目录
	 */
	List<?> getChild(String dn)throws ServiceException;
	
	/**
	 * 根据域名查询组织的详细信息
	 * @params domain 域名 例如 cstnet.cn
	 * @return 匹配的组织，应该只有一个
	 * */
	VmtOrg getOrgByDomain(String domain)throws ServiceException;
	
	/**
	 * 所有组织和域名的对应关系
	 * @return return all org-domain mappings
	 * */
	List<VmtOrgDomain> getAllDomains()throws ServiceException;
	/**
	 * 查询组织下所有用户的属性值
	 * @param dn
	 * @param attributeName
	 * @return
	 * @throws ServiceException 
	 */
	Map<String,String> searchUserAttribute(String dn, String attributeName) throws ServiceException;
}
