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
package net.duckling.vmt.web.helper;

import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.coremail.CoreMailOrgUnit;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IDepartmentService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.stereotype.Service;

/**
 * 需要同步的coreMail逻辑，比如新建部门需要在coreMail那面也新建一下
 * @author lvly
 * @since 2013-9-23
 */
@Service
public class CoreMailHelper {
	@Autowired
	private ICoreMailService coreMailService;
	@Autowired
	private IDepartmentService deptService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrgService orgService;
	public void checkDepartment(String deptdn){
		if(LdapUtils.isOrgDN(deptdn)){
			return;
		}
		String pdn=LdapUtils.less(deptdn, 1);
		createDepart(pdn,deptService.getDepartByDN(deptdn));
	}
	
	public void createDepart(String pdn,LdapDepartment dept){
		LdapOrg org=null;
		LdapDepartment currDept=dept;
		if(!LdapUtils.isOrgDN(pdn)){
			pdn=LdapUtils.getTeamDN(pdn);
		}
		org=orgService.getOrgByDN(pdn);
		if(dept.getDn()==null){
			currDept=deptService.getDepartBySymbol(pdn, dept.getSymbol());
		}
		createDepartment(org,currDept);
	}
	
	/**创建部门也在coreMail那边添加
	 * @param dept
	 * @param parentDept
	 */
	public void createDepartment(LdapOrg org,LdapDepartment dept){
		if(!LdapNode.FROM_CORE_MAIL.equals(org.getFrom())){
			return;
		}
		CoreMailOrgUnit unit=coreMailService.getUnit(org.getSymbol(), dept.getSymbol());
		boolean isRoot=false;
		if(new DistinguishedName(dept.getDn()).size()==3){
			isRoot=true;
		}
		String parentDeptDn=LdapUtils.less(dept.getDn(), 1);
		String parentSymbol=LdapUtils.getLastValue(parentDeptDn);
		//未到根目录，接着递归
		if(unit==null&&!isRoot){
			createDepartment(org,deptService.getDepartByDN(parentDeptDn));
			createCoreMailUnit(org,dept,parentSymbol);
		}
		//已经到了根目录，可以停止了
		else if(unit==null&&isRoot){
			createCoreMailUnit(org,dept,null);
		}
		//到这里代表coreMail也有，不做检查
	}
	private void createCoreMailUnit(LdapOrg org,LdapDepartment dept,String parentDeptSymbol){
		CoreMailOrgUnit unit=new CoreMailOrgUnit();
		unit.setOrgId(org.getSymbol());
		unit.setOuId(dept.getSymbol());
		unit.setParentId(parentDeptSymbol);
		unit.setOuName(dept.getName());
		coreMailService.createUnit(unit);
	}

	/**
	 * 批量移动用户
	 * @param decodeUserDns
	 * @param decodeDestDn
	 */
	public void batchMoveUser(String[] userDns, String destDn) {
		String dest=null;
		if(!LdapUtils.isOrgDN(destDn)){
			dest=LdapUtils.getLastValue(destDn);
		}
		for(String dn:userDns){
			LdapUser user=userService.getUserByDN(dn);
			coreMailService.moveUser(user.getCstnetId(), dest);
		}

		
	}
}
