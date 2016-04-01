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
package net.duckling.vmt.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.PinyinUtils;
import net.duckling.vmt.dao.IDepartmentDAO;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.coremail.CoreMailOrgUnit;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapNode;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.view.ListView;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.ICommonService;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IDepartmentService;
import net.duckling.vmt.service.INodeService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.ISearchService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.mq.MQMessageSenderExt;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 对部门的业务逻辑
 * @author lvly
 * @since 2013-5-2
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService{
	@Autowired
	private IDepartmentDAO departDAO;
	@Autowired
	private INodeService nodeService;
	@Autowired
	private ISearchService searchService;
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private MQMessageSenderExt sender;
	@Autowired
	private ICoreMailService coreMailService;
	private String getUseableSymble(String teamDn,String symbol){
		if(symbol.length()>25){
			symbol=System.currentTimeMillis()+"";
		}
		StringBuffer sb=new StringBuffer();
		for(char c:symbol.toCharArray()){
			if(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')||(c>='0'&&c<='9')){
				sb.append(c);
			}
		}
		if(sb.length()==0){
			sb.append(RandomUtils.nextLong());
		}
		symbol=sb.toString();
		if(nodeService.isSymbolUsed(teamDn, symbol)){
			return getUseableSymble(teamDn, symbol+"_dup");
		}
		return symbol;
	}
	
	@Override
	public LdapDepartment getDepartByPath(String teamDn, String[] paths) {
		LdapOrg org=orgService.getOrgByDN(teamDn);
		if(CommonUtils.isNull(paths)){
			LdapDepartment ldap=new LdapDepartment();
			ldap.setDn(teamDn);
			return ldap;
		}
		String pdn=teamDn;
		LdapDepartment dept=null;
		for(String deptName:paths){
			if(CommonUtils.isNull(deptName)){
				continue;
			}
			dept=departDAO.getDepartByName(pdn,deptName);
			if(dept==null){
				dept=new LdapDepartment();
				dept.setName(deptName);
				String deptSym=getUseableSymble(teamDn, PinyinUtils.getPinyin(deptName));
				dept.setSymbol(deptSym);
				dept.setCreator(KEY.GLOBAL_SYSTEM);
				if(LdapNode.FROM_CORE_MAIL.equals(org.getFrom())){
					CoreMailOrgUnit unit=new CoreMailOrgUnit();
					unit.setOrgId(org.getSymbol());
					unit.setOuId(deptSym);
					unit.setOuName(deptName);
					if(!LdapUtils.isOrgDN(pdn)){
						unit.setParentId(LdapUtils.getLastValue(pdn));
					}
					coreMailService.createUnit(unit);
				}
				create(pdn, dept);
				sender.sendCreateDeptMessage(dept, pdn);
			}
			pdn=dept.getDn();
		}
		return getDepartByDN(dept.getDn());
	}
	
	@Override
	public boolean create(String pdn, LdapDepartment depart) {
		depart.setSymbol(depart.getSymbol().toLowerCase());
		depart.setCurrentDisplay(nodeService.getNode(pdn).addDisplay(depart.getName()));
		return departDAO.insert(pdn,depart);
	}

	@Override
	public boolean isNameExists(String pdn,String departName) {
		return departDAO.isExists(pdn,departName);
	}
	
	@Override
	public List<ListView> isMoveCauseMerge(String destDn, String orgDn,boolean sonOnly) {
		List<ListView> result=new ArrayList<ListView>();
		if(!sonOnly){
			LdapDepartment dept=departDAO.getDepartByDN(orgDn);
			String wantDestDn=departDAO.getDn(destDn, dept.getName());
			if(!CommonUtils.isNull(wantDestDn)){
				result.add(new ListView(dept));
			}
			return result;
		}
		List<ListView> datas=searchService.searchDepartByList(orgDn);
		for(ListView data:datas){
			String wantDestDn=departDAO.getDn(destDn, data.getName());
			if(!CommonUtils.isNull(wantDestDn)){
				result.add(data); 
			}
		}
		return result;
	}
	@Override
	public LdapDepartment updateDepart(LdapDepartment dept) {
		LdapDepartment orgDept=getDepartByDN(dept.getDn());
		coreMailService.updateUnitName(LdapUtils.getLastValue(LdapUtils.getTeamDN(dept.getDn())),LdapUtils.getLastValue(dept.getDn()), CommonUtils.trim(dept.getName()));
		nodeService.updateSonAndSelfDisplayName(dept.getDn(), dept.getName());
		attrService.update(dept.getDn(), "vmt-name",CommonUtils.trim(dept.getName()));
		attrService.update(dept.getDn(), "vmt-list-rank",dept.getListRank());
		if(orgDept.isVisible()!=dept.isVisible()){
			nodeService.updateSubObjectVisible(dept.getDn(),dept.isVisible(),true);
		}
		return orgDept;
	}
	
	@Override
	public List<LdapDepartment> getDepartByAll(String baseDn) {
		return departDAO.getDepartByAll(baseDn);
	}
	
	private boolean moveSon(String destDn,String orgDn,boolean isVisibleExtendDest){
		List<?> datas=searchService.searchByListLocalData(orgDn);
		List<LdapUser> users=new ArrayList<LdapUser>();
		for(Object data:datas){
			if(data instanceof LdapDepartment){
				LdapDepartment dept=(LdapDepartment)data;
				String wantDestDn=departDAO.getDn(destDn, dept.getName());
				String orgDeptDn=dept.getDn();
				if(CommonUtils.isNull(wantDestDn)){
					dept.setDn("");
					create(destDn, dept);
					wantDestDn=dept.getDn();
				}
				moveSon(wantDestDn,orgDeptDn,isVisibleExtendDest);
			}else if(data instanceof LdapUser){
				LdapUser user=(LdapUser)data;
				users.add(user);
			}else{
				return false;
			}
		}
		userService.move(getArray(users), destDn,false);
		commonService.unbind(orgDn);
		return true;
	}
	@Override
	public boolean moveDepartment(String destDn, String orgDn,boolean sonOnly,boolean isVisibleExtendDest){
		moveCoreMailDepartment(destDn,orgDn,sonOnly);
		ifDestNotOrgAndVisibleExtend( destDn,orgDn, sonOnly,isVisibleExtendDest);
		//包括自己，全部移动
		if(!sonOnly){
			LdapDepartment dept=departDAO.getDepartByDN(orgDn);
			String wantDestDn=departDAO.getDn(destDn, dept.getName());
			if(CommonUtils.isNull(wantDestDn)){
				dept.setDn("");
				create(destDn, dept);
				wantDestDn=dept.getDn();
			}
			if(wantDestDn.equals(orgDn)){
				return true;
			}
			return moveSon(wantDestDn,orgDn,isVisibleExtendDest);
		}else{
			return moveSon(destDn,orgDn,isVisibleExtendDest);
		}
	}
	private void ifDestNotOrgAndVisibleExtend(String destDn, String orgDn,boolean sonOnly,boolean isVisibleExtendDest){
		//判定目标不是根,且要继承目标的可见属性的话
		if(!LdapUtils.isOrgDN(destDn)&&isVisibleExtendDest){
			LdapDepartment wantDest=departDAO.getDepartByDN(destDn);
			nodeService.updateSubObjectVisible(orgDn, wantDest.isVisible(), !sonOnly);
		}
	}
	private void moveCoreMailDepartment(String destDn,String orgDn,boolean sonOnly){
		String orgSymbol=LdapUtils.getLastValue(LdapUtils.getTeamDN(destDn));
		String orgDeptSymbol=LdapUtils.getLastValue(orgDn);
		String destDeptSymbol=null;
		boolean isMove2Root=LdapUtils.isOrgDN(destDn);
		if(!isMove2Root){
			destDeptSymbol=LdapUtils.getLastValue(destDn);
		}
		//只移动子元素
		if(sonOnly){
			List<?> datas=searchService.searchByListLocalData(orgDn);
			for(Object data:datas){
				if(data instanceof LdapDepartment){
					LdapDepartment dept=(LdapDepartment)data;
					coreMailService.moveUnit(orgSymbol, dept.getSymbol(),destDeptSymbol);
				}else if(data instanceof LdapUser){
					LdapUser user=(LdapUser)data;
					coreMailService.moveUser(user.getCstnetId(), destDeptSymbol);
				}
			}
			coreMailService.deleteUnit(orgSymbol, orgDeptSymbol);
		}else{
			coreMailService.moveUnit(orgSymbol, orgDeptSymbol, destDeptSymbol);
		}
	}
	private String[] getArray(List<LdapUser> users){
		String[] result=new String[users.size()];
		int index=0;
		for(LdapUser user:users){
			result[index++]=user.getDn();
		}
		return result;
	}
	
	@Override
	public String getNeedDeleteDepart(String orgDN) {
		if(!nodeService.isSymbolUsed(orgDN, NEED_DELETE_DEPART)){
			LdapDepartment depart=new LdapDepartment();
			depart.setCreator(KEY.GLOBAL_SYSTEM);
			depart.setSymbol(NEED_DELETE_DEPART);
			depart.setName("已删除成员");
			create(orgDN, depart);
			return depart.getDn();
		}
		return "vmt-symbol="+NEED_DELETE_DEPART+","+orgDN;
	}
	@Override
	public LdapDepartment getDepartByDN(String decodeDestDn){
		return departDAO.getDepartByDN(decodeDestDn);
	}
	@Override
	public LdapDepartment getDepartBySymbol(String orgDN,String symbol) {
		return departDAO.getDepartBySymbol(orgDN,symbol);
	}
}
