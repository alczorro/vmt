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
package net.duckling.vmt.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IVmtIndexDAO;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2013-7-16
 */
@Component
public class VmtIndexDAOJDBCImpl implements IVmtIndexDAO{
	@Autowired
	private BaseDAO<VmtIndex> baseDAO;
	@Override
	public void insertIndex(List<VmtIndex> indexs) {
		baseDAO.batchAdd(indexs);
	}
	@Override
	public void deleteAll() {
		baseDAO.delete(new VmtIndex());
	}
	@Override
	public void deleteAUserIndex(String umtId) {
		VmtIndex index=new VmtIndex();
		index.setUmtId(umtId);
		baseDAO.delete(index);
		
	}
	@Override
	public List<VmtIndex> selectIndexByTeamDNAndStatus(String[] teamDns, String status) {
		if(CommonUtils.isNull(teamDns)){
			return null;
		}
		String sql=" select * from `vmt_user_team_rel` where 1=1  and `status`=:status";
		
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("status",status);
		int index=0;
		sql+=" and ( ";
		for(String teamDn:teamDns){
			sql+=(index==0?"":" or ")+"team_dn=:teamDn"+(index);
			params.put("teamDn"+index, teamDn);
			index++;
		}
		sql+=")";
		return baseDAO.getTmpl().query(sql, params, baseDAO.getORMParser(VmtIndex.class).getRowMapper());
	}

	@Override
	public void deleteATeamIndex(String teamDn) {
		VmtIndex index=new VmtIndex();
		index.setTeamDN(teamDn);
		baseDAO.delete(index);
	}
	
	@Override
	public void deleteIndex(String dn, String umtId) {
		VmtIndex index=new VmtIndex();
		index.setTeamDN(dn);
		index.setUmtId(umtId);
		baseDAO.delete(index);
	}
	@Override
	public List<VmtIndex> selectIndexByType(String umtId,int type) {
		VmtIndex index=new VmtIndex();
		index.setUmtId(umtId);
		index.setType(type);
		return baseDAO.select(index," and `status`!='"+LdapUser.STATUS_REFUSE+"'");
	}
	@Override
	public List<VmtIndex> selectIndexByTypeAll(String umtId,int type) {
		VmtIndex index=new VmtIndex();
		index.setUmtId(umtId);
		index.setType(type);
		return baseDAO.select(index);
	}
	@Override
	public List<VmtIndex> selectIndexByStatus(String umtId, String status) {
		VmtIndex index=new VmtIndex();
		index.setUmtId(umtId);
		index.setStatus(status);
		return baseDAO.select(index);
	}
	@Override
	public void updateStatus(String umtId, String dn, String status) {
		String sql="update vmt_user_team_rel set `status`=:status where `umt_id`=:umtId and `team_dn`=:teamDn";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("status", status);
		param.put("teamDn", dn);
		param.put("umtId", umtId);
		baseDAO.getTmpl().update(sql, param);
	}
	@Override
	public void updateTeamName(String teamDn, String teamName) {
		String sql="update vmt_user_team_rel set `team_name`=:teamName where team_dn=:teamDn";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("teamName", teamName);
		param.put("teamDn", teamDn);
		baseDAO.getTmpl().update(sql, param);
	}
	@Override
	public void updateUserStatus(String dn, String status) {
		String sql="update vmt_user_team_rel set `status`=:status where user_dn=:userDn";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("status", status);
		param.put("userDn", dn);
		baseDAO.getTmpl().update(sql, param);
	}
	@Override
	public void deleteAUserIndexByUserDN(String[] userDNs) {
		String sql="delete from vmt_user_team_rel where user_dn in(";
		int index=0;
		Map<String,Object> param=new HashMap<String,Object>();
		for(String userDN:userDNs){
			sql+=index++==0?"":",";
			sql+=":userDn"+index;
			param.put("userDn"+index, userDN);
		}
		sql+=")";
		baseDAO.getTmpl().update(sql, param);
	}
	@Override
	public void deleteIndex(String teamDn) {
		String sql="delete from vmt_user_team_rel where team_dn =:teamDN";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("teamDN",teamDn);
		baseDAO.getTmpl().update(sql, param);
		
	}
	@Override
	public void updateUserName(String userDn, String userName) {
		String sql="update vmt_user_team_rel set `user_name`=:userName where user_dn=:userDn";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userName", userName);
		param.put("userDn", userDn);
		baseDAO.getTmpl().update(sql, param);
	}
	@Override
	public void updateUserVisible(String dn, boolean visible) {
		String sql="update vmt_user_team_rel set `user_visible`=:visible where user_dn=:userDn";
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("visible", visible);
		param.put("userDn", dn);
		baseDAO.getTmpl().update(sql, param);
	}
	@Override
	public Map<String, int[]> selectCountByType(String[] groupDns, int type) {
		String sql=
		"select  r.team_dn,"+
		"(select count(*)  "+
		"from vmt_user_team_rel r1 "+
		"where r1.team_dn=r.team_dn  "+
		"and r1.`status`='"+LdapUser.STATUS_ACTIVE+"' "+
		"and r1.`user_visible`=1 ) activeCount, "+
		"(select count(*)  "+
		"from vmt_user_team_rel r2 "+
	 	"where r2.team_dn=r.team_dn ) allCount "+
		"from vmt_user_team_rel r "+
	 	"where r.team_dn in(";
		int index=0;
		Map<String,Object> param=new HashMap<String,Object>();
		for(String dn:groupDns){
			sql+=(index==0)?"":",";
			sql+=":dn"+index;
			param.put("dn"+index, dn);
			index++;
		}
		sql+=") and r.type="+type;
		sql+=" group by r.team_dn ";
		final Map<String,int[]> count=new HashMap<String,int[]>();
		baseDAO.getTmpl().query(sql, param, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				int activeCount=rs.getInt("activeCount");
				int allCount=rs.getInt("allCount");
				String teamDn=rs.getString("team_dn");
				count.put(teamDn, new int[]{activeCount,allCount});
				return null;
			}
		});
		return count;
	}
	@Override
	public boolean isUserExistsOtherOrg(String umtId) {
		String sql=" select count(*) from `vmt_user_team_rel` r "
				 +" where `umt_id`=:umtId and `type`="+VmtIndex.TYPE_ORG;
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("umtId", umtId);
		return baseDAO.getTmpl().queryForInt(sql, param)>0;
	}
	@Override
	public boolean isUseableVmtMember(String email) {
		String sql=" select count(*) from `vmt_user_team_rel` r "
				 +" where `status`=:status and `user_cstnet_id`=:userCstnetId";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status","true");
		map.put("userCstnetId",email);
		return baseDAO.getTmpl().queryForInt(sql, map)>0;
	}
	@Override
	public VmtIndex getUseableVmtUser(String email) {
		String sql=" select * from `vmt_user_team_rel` r "
				 +" where `status`=:status and `user_cstnet_id`=:userCstnetId limit 0,1";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status","true");
		map.put("userCstnetId",email);
		return CommonUtils.first(baseDAO.getTmpl().query(sql,map, baseDAO.getORMParser(VmtIndex.class).getRowMapper()));
	}
	@Override
	public List<VmtIndex> selectIndexByKeyword(String trim) {
		String sql=" select * from `vmt_user_team_rel` r "
				 +" where `status`=:status and ";
		sql+="( r.`user_cstnet_id` like :keyword or r.`user_name` like :keyword) group by umt_id limit 10";
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status","true");
		map.put("keyword", "%"+trim+"%");
		return baseDAO.getTmpl().query(sql, map, baseDAO.getORMParser(VmtIndex.class).getRowMapper());
	}
	@Override
	public VmtIndex selectIndexById(int indexId) {
		VmtIndex i=new VmtIndex();
		i.setId(indexId);
		return baseDAO.selectOne(i);
	}

}
