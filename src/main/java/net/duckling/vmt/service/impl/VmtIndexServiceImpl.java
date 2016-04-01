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
import java.util.Map;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.adapter.Collection2ArrayAdapter;
import net.duckling.vmt.common.adapter.LdapUser2VmtIndexAdapter;
import net.duckling.vmt.dao.IVmtIndexDAO;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.service.thread.JobThread;
import net.duckling.vmt.service.thread.VmtBuildAIndexJob;
import net.duckling.vmt.service.thread.VmtBuildIndexJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-7-16
 */
@Service
public class VmtIndexServiceImpl implements IVmtIndexService{
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IVmtIndexDAO indexDAO;
	
	@Override
	public void addIndexByCstnetId(String teamDN, boolean[] flags, String[] cstnetIds) {
		List<String> result=new ArrayList<String>();
		int index=0;
		for(boolean flag:flags){
			if(flag){
				result.add(cstnetIds[index].split(KEY.GLOBAL_DATA_SPLIT)[0]);
			}
			index++;
		}
		if(CommonUtils.isNull(result)){
			return;
		}
		List<LdapUser> users=userService.searchUsersByCstnetId(teamDN,result.toArray(new String[result.size()]));
		indexDAO.insertIndex(LdapUser2VmtIndexAdapter.convert(users));
	}
	@Override
	public void addIndexByUser(String teamDN, boolean[] flag, LdapUser user) {
		if(flag==null||flag.length!=1||!flag[0]){
			return;
		}
		List<LdapUser> users=new ArrayList<LdapUser>();
		users.add(user);
		indexDAO.insertIndex(LdapUser2VmtIndexAdapter.convert(users));
	}
	@Override
	public void addIndexByUmtId(String teamDN, boolean[] flags, String[] umtId) {
		int index=0;
		List<String> result=new ArrayList<String>();
		for(boolean flag:flags){
			if(flag){
				result.add(umtId[index]);
			}
			index++;
		}
		if(CommonUtils.isNull(result)){
			return;
		}
		List<LdapUser> users=userService.searchUsersByUmtId(teamDN,result.toArray(new String[result.size()]));
		indexDAO.insertIndex(LdapUser2VmtIndexAdapter.convert(users));
	}
	
	
	@Override
	public void buildIndexSynchronous() {
		List<LdapUser> users=userService.searchUsersAll();
		indexDAO.deleteAll();
		indexDAO.insertIndex(LdapUser2VmtIndexAdapter.convert(users));
	}
	@Override
	public void buildIndexJob() {
		JobThread.addJobThread(new VmtBuildIndexJob());
	}
	@Override
	public void buildAIndexJob(String dn) {
		JobThread.addJobThread(new VmtBuildAIndexJob(dn));
		
	}
	@Override
	public void buildAIndexSynchronous(String dn) {
		List<LdapUser> users=userService.searchUsersByAll(new String[]{dn}, null);
		indexDAO.deleteATeamIndex(dn);
		indexDAO.insertIndex(LdapUser2VmtIndexAdapter.convert(users));
		
	}
	@Override
	public List<VmtIndex> selectIndexByType(String umtId,int type) {
		return indexDAO.selectIndexByType(umtId,type);
	}
	@Override
	public List<VmtIndex> selectIndexByStatus(String umtId, String status) {
		return indexDAO.selectIndexByStatus(umtId, status);
	}
	@Override
	public void setGroupCount(List<LdapGroup> groups,String umtId) {
		if(CommonUtils.isNull(groups)){
			return;
		}
		Map<String,int[]> map=indexDAO.selectCountByType(Collection2ArrayAdapter.convertField(groups, "dn"),VmtIndex.TYPE_GROUP);
		for(LdapGroup group:groups){
			int[] result=map.get(group.getDn());
			if(result==null){
				continue;
			}
			if(umtId!=null&&!group.canSeeUnConfirm(umtId)){
				group.setCount(result[0]);
			}else{
				group.setCount(result[1]);
			}
		}
	}
	@Override
	public void setOrgCount(List<LdapOrg> orgs,String umtId) {
		if(CommonUtils.isNull(orgs)){
			return;
		}
		Map<String,int[]> map=indexDAO.selectCountByType(Collection2ArrayAdapter.convertField(orgs, "dn"),VmtIndex.TYPE_ORG);
		for(LdapOrg org:orgs){
			int[] result=map.get(org.getDn());
			if(result==null){
				continue;
			}
			if(umtId!=null&&!org.canSeeUnConfirm(umtId)){
				org.setCount(result[0]);
			}else{
				org.setCount(result[1]);
			}
		}
	}
	@Override
	public void updateStatus(String dn, String umtId, String status) {
		indexDAO.updateStatus(umtId, dn, status);
	}
	@Override
	public void deleteIndex(String teamDN, String umtId) {
		indexDAO.deleteIndex(teamDN, umtId);
	}
	@Override
	public void updateUserName(String userDn, String userName) {
		indexDAO.updateUserName(userDn,userName);
	}
	@Override
	public void updateTeamName(String teamDn, String teamName) {
		indexDAO.updateTeamName(teamDn,teamName);
	}
	@Override
	public void deleteUser(String[] userDNs) {
		indexDAO.deleteAUserIndexByUserDN(userDNs);
	}
	@Override
	public void deleteIndex(String teamDn) {
		indexDAO.deleteATeamIndex(teamDn);
	}
	@Override
	public List<VmtIndex> selectIndexByTeamDNAndStatus(String[] teamDns, String statusApply) {
		return indexDAO.selectIndexByTeamDNAndStatus(teamDns,statusApply);
	}
	
	@Override
	public void updateUserVisible(String dn, boolean visible) {
		indexDAO.updateUserVisible(dn,visible);
	}
	@Override
	public void updateUserStatus(String dn, String status) {
		indexDAO.updateUserStatus(dn,status);
	}
	@Override
	public List<VmtIndex> selectIndexByTypeAll(String umtId, int typeGroup) {
		return indexDAO.selectIndexByTypeAll(umtId, typeGroup);
	}
	@Override
	public boolean isUseableVmtMember(String email) {
		return indexDAO.isUseableVmtMember(email);
	}
	@Override
	public VmtIndex getUseableVmtUser(String email) {
		return indexDAO.getUseableVmtUser(email);
	}
	@Override
	public List<VmtIndex> selectIndexByKeyword(String trim) {
		return indexDAO.selectIndexByKeyword(trim);
	}
	@Override
	public VmtIndex selectIndexById(int indexId) {
		return indexDAO.selectIndexById(indexId);
	}
}
