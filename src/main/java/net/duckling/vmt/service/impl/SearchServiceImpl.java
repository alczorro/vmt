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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.PinyinUtils;
import net.duckling.vmt.dao.ISearchDAO;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.view.ListView;
import net.duckling.vmt.service.IPrivilegeService;
import net.duckling.vmt.service.ISearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-5-10
 */
@Service
public class SearchServiceImpl implements ISearchService{
	@Autowired
	private ISearchDAO searchDAO;
	@Autowired
	private IPrivilegeService pService;
	
	@Override
	public List<ListView> searchByList(String dn,String umtId) {
		String teamDN=LdapUtils.getTeamDN(dn);
		boolean searchUnConfirm=CommonUtils.isNull(umtId)?true:pService.canLookUnConfirm(teamDN, umtId);
		boolean isAdmin=CommonUtils.isNull(umtId)?true:pService.isAdmin(teamDN, umtId);
		return sort(searchDAO.searchByList(dn,searchUnConfirm,isAdmin));
	}
	@Override
	public List<ListView> searchAllByTree(String dn) {
		return searchDAO.searchDepartByAll(dn);
	}
	@Override
	public List<?> searchByListLocalData(String dn) {
		return sortLocalData(searchDAO.searchByListLocalData(dn));
	}
	private List<?> sortLocalData(List<?> list){
		if(CommonUtils.isNull(list)){
			return list;
		}
		List<LdapDepartment> dept=new ArrayList<LdapDepartment>();
		List<LdapUser> users=new ArrayList<LdapUser>();
		for(Object obj:list){
			if(obj instanceof LdapDepartment){
				dept.add((LdapDepartment)obj);
			}
			else if(obj instanceof LdapUser){
				users.add((LdapUser)obj);
			}
		}
		Collections.sort(dept);
		Collections.sort(users);
		List<Object> result=new ArrayList<Object>();
		result.addAll(dept);
		result.addAll(users);
		return result;
	}
	private List<ListView> sort(List<ListView> list){
		if(CommonUtils.isNull(list)){
			return list;
		}
		List<ListView> dept=new ArrayList<ListView>();
		for(ListView view:list){
			if(ListView.TYPE_DEPART.equals(view.getType())){
				dept.add(view);
			}
		}
		Comparator<ListView> comp=new Comparator<ListView>() {
			@Override
			public int compare(ListView l1, ListView l2) {
				if(l1==null){
					return -1;
				}
				if(l2==null){
					return 1;
				}
				int result=l2.getListRank()-l1.getListRank();
				
				if(result==0){
					return PinyinUtils.getPinyin(l1.getName()).compareTo(PinyinUtils.getPinyin(l2.getName())); 
				}
				return result;
				
			}
		};
		Collections.sort(dept,comp);
		List<ListView> users=new ArrayList<ListView>();
		for(ListView view:list){
			if(ListView.TYPE_USER.equals(view.getType())){
				users.add(view);
			}
		}
		Collections.sort(users,comp);
		List<ListView> result=new ArrayList<ListView>();
		result.addAll(dept);
		result.addAll(users);
		return result;
	} 
	@Override
	public List<ListView> searchDepartByList(String decodeDN) {
		return sort(searchDAO.searchDepartByList(decodeDN));
	}
	@Override
	public int searchActiveUserCount(String dn) {
		return searchDAO.searchActiveUserCount(dn);
	}

}
