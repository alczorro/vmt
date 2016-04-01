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
package net.duckling.vmt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.repository.BaseDAO;
import net.duckling.vmt.dao.IVmtMessageDAO;
import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.domain.message.VmtMsgColumns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class VmtMessageJDBCDAOImpl implements IVmtMessageDAO{
	@Autowired
	private BaseDAO<VmtMessage> msgDAO;
	@Autowired
	private BaseDAO<VmtMsgColumns> columnsDAO;

	@Override
	public int insertMsg(VmtMessage vmtMessage) {
		return msgDAO.insert(vmtMessage);
	}
	@Override
	public VmtMessage getMsgById(int msgId) {
		VmtMessage v=new VmtMessage();
		v.setId(msgId);
		return msgDAO.selectOne(v);
	}

	@Override
	public void insertMsgColumns(List<VmtMsgColumns> columns) {
		columnsDAO.batchAdd(columns);
	}

	@Override
	public List<VmtMessage> getMsgByEntryId(String entryId) {
		VmtMessage msg=new VmtMessage();
		msg.setEntryId(entryId);
		return msgDAO.select(msg,"and msgStatus!='"+VmtMessage.MSG_STATUS_DELETED+"'");
	}

	@Override
	public List<VmtMsgColumns> getMsgColumnsByEntryId(List<Integer> extractId) {
		VmtMsgColumns columns=new VmtMsgColumns();
		String extendSql="";
		for(int i=0;i<extractId.size();i++){
			extendSql+=((i==0)?"":",")+extractId.get(i);
		}
		return columnsDAO.select(columns, "and msgId in("+extendSql+")");
	}

	@Override
	public void updateMsgStatus(int msgId,String status) {
		VmtMessage msg=new VmtMessage();
		msg.setId(msgId);
		msg.setMsgStatus(status);
		msgDAO.update(msg);
	}

	@Override
	public void deleteMsg(int msgId) {
		VmtMessage msg=new VmtMessage();
		msg.setId(msgId);
		msg.setMsgStatus(VmtMessage.MSG_STATUS_DELETED);
		msgDAO.update(msg);
	}
	@Override
	public List<VmtMessage> getMsgByToAdmin(String[] imAdminDNs) {
		if(CommonUtils.isNull(imAdminDNs)){
			return null;
		}
		Map<String,Object> params=getMsgToAdminSql(false, imAdminDNs,false);
		return 	msgDAO.getTmpl().query(params.get("sql").toString(), params,msgDAO.getORMParser(VmtMessage.class).getRowMapper());
	}
	@Override
	public List<VmtMessage> getMsgByToMe(String[] allDN,String myCstnetId) {
		if(CommonUtils.isNull(myCstnetId)||CommonUtils.isNull(allDN)){
			return null;
		}
		Map<String,Object> params=getMsgToMeSql(false,myCstnetId,allDN,false);
		return 	msgDAO.getTmpl().query(params.get("sql").toString(), params,msgDAO.getORMParser(VmtMessage.class).getRowMapper());
	}
	@Override
	public int getMsgByToAdminCount(String[] dns) {
		if(CommonUtils.isNull(dns)){
			return 0;
		}
		Map<String,Object> params=getMsgToAdminSql(true, dns,false);
		return 	msgDAO.getTmpl().queryForInt(params.get("sql").toString(), params);
	}
	@Override
	public int getMsgByToMeCount(String[] allDN,String cstnetId) {
		if(CommonUtils.isNull(cstnetId)||CommonUtils.isNull(allDN)){
			return 0;
		}
		Map<String,Object> params=getMsgToMeSql(true, cstnetId, allDN, false);
		return 	msgDAO.getTmpl().queryForInt(params.get("sql").toString(), params);
	}
	private Map<String,Object> getMsgToAdminSql(boolean isCount,String[] entryId,boolean getOver){
		String extendSql="";
		Map<String,Object> params=new HashMap<String,Object>();
		for(int i=0;i<entryId.length;i++){
			String paramId="teamDN"+i;
			extendSql+=((i==0)?"":" , ")+":"+paramId;
			params.put(paramId, entryId[i]);
		}
		String sql="select "+(isCount?"count(*)":"*")+" from vmt_message where msgTo='"+VmtMessage.MSG_TO_ADMIN+"' and  `teamDN` in("+extendSql+") and msgStatus!='"+VmtMessage.MSG_STATUS_DELETED+"'";
		if(!getOver){
			sql+=" and `msgStatus`='"+VmtMessage.MSG_STATUS_NEED_REED+"'";
		}
		params.put("sql",sql);
		return params;
	}
	private Map<String,Object> getMsgToMeSql(boolean isCount,String cstnetId,String[] teamDN,boolean getOver){
		String extendSql="";
		Map<String,Object> params=new HashMap<String,Object>();
		for(int i=0;i<teamDN.length;i++){
			String paramId="teamDN"+i;
			extendSql+=((i==0)?"":" , ")+":"+paramId;
			params.put(paramId, teamDN[i]);
		}
		String sql="select "+(isCount?"count(*)":"*")+" from vmt_message where msgTo='"+VmtMessage.MSG_TO_USER+"' and entryId=:entryId and  `teamDN` in("+extendSql+") and msgStatus!='"+VmtMessage.MSG_STATUS_DELETED+"'";
		if(!getOver){
			sql+=" and `msgStatus`='"+VmtMessage.MSG_STATUS_NEED_REED+"'";
		}
		params.put("sql",sql);
		params.put("entryId",cstnetId);
		return params;
	}
	
	@Override
	public VmtMessage getMsgByProperty(VmtMessage msg) {
		return msgDAO.selectOne(msg,"and msgStatus!='"+VmtMessage.MSG_STATUS_DELETED+"'");
	}
	@Override
	public boolean isEntryIdUsed(String entryId) {
		VmtMessage msg=new VmtMessage();
		msg.setEntryId(entryId);
		return msgDAO.getCount(msg)>0;
	}
	

}
