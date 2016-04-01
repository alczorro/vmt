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
package net.duckling.vmt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.dao.IVmtMessageDAO;
import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.domain.message.VmtMsgColumns;
import net.duckling.vmt.service.IVmtMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VmtMessageServiceImpl implements IVmtMessageService {
	
	@Autowired
	private IVmtMessageDAO msgDAO;
	

	@Override
	public int insertMsg(VmtMessage vmtMessage) {
		vmtMessage.setId( msgDAO.insertMsg(vmtMessage));
		return vmtMessage.getId();
	}
	@Override
	public VmtMessage getMsgById(int msgId) {
		VmtMessage vmtMsg=msgDAO.getMsgById(msgId);
		return fillDetail(vmtMsg);
	}

	@Override
	public void insertMsgColumns(VmtMsgColumns column) {
		List<VmtMsgColumns> columns=new ArrayList<VmtMsgColumns>();
		columns.add(column);
		msgDAO.insertMsgColumns(columns);
	}
	
	@Override
	public void insertMsgColumns(List<VmtMsgColumns> columns) {
		msgDAO.insertMsgColumns(columns);
	}
	@Override
	public List<VmtMessage> getMsgByEntryId(String entryId) {
		List<VmtMessage> msgs=msgDAO.getMsgByEntryId(entryId);
		return fillDetail(msgs);
	}
	private VmtMessage fillDetail(VmtMessage msg){
		List<VmtMessage> msgs=new ArrayList<VmtMessage>();
		msgs.add(msg);
		return CommonUtils.first(fillDetail(msgs));
	}
	private List<VmtMessage> fillDetail(List<VmtMessage> msgs){
		if(CommonUtils.isNull(msgs)){
			return null;
		}
		List<VmtMsgColumns> columns=msgDAO.getMsgColumnsByEntryId(extractId(msgs));
		if(CommonUtils.isNull(columns)){
			return msgs;
		}
		Map<Integer,VmtMessage> map=new HashMap<Integer,VmtMessage>();
		for(VmtMessage msg:msgs){
			map.put(msg.getId(), msg);
		}
		for(VmtMsgColumns c:columns){
			map.get(c.getMsgId()).addColumn(c);
		}
		return msgs;
	}
	@Override
	public int getMsgByICanSeeCount(String myCstnetId, String[] allDN,String[] adminDN) {
		int countAdmins=msgDAO.getMsgByToAdminCount(adminDN);
		int countToMe=msgDAO.getMsgByToMeCount(allDN,myCstnetId);
		return countAdmins+countToMe;
	}
	@Override
	public List<VmtMessage> getMsgByICanSee(String myCstnetId,String[] allDN,String[] imAdminDNs) {
		List<VmtMessage> result=new ArrayList<VmtMessage>();
		List<VmtMessage> adminCanSee=fillDetail(msgDAO.getMsgByToAdmin(imAdminDNs));
		List<VmtMessage> sendToMe=fillDetail(msgDAO.getMsgByToMe(allDN, myCstnetId));
		if(!CommonUtils.isNull(adminCanSee)){
			result.addAll(adminCanSee);
		}
		if(!CommonUtils.isNull(sendToMe)){
			result.addAll(sendToMe);
		}
	
		return result;
	}
	private List<Integer> extractId(List<VmtMessage> msgs){
		List<Integer> ids=new ArrayList<Integer>();
		for(VmtMessage msg:msgs){
			ids.add(msg.getId());
		}
		return ids;
	}

	@Override
	public void updateMsgReaded(int msgId) {
		msgDAO.updateMsgStatus(msgId,VmtMessage.MSG_STATUS_OVER);
	}
	@Override
	public void updateMsgUnReaded(int msgId){
		msgDAO.updateMsgStatus(msgId,VmtMessage.MSG_STATUS_NEED_REED);
	}

	@Override
	public void deleteMsg(int msgId) {
		msgDAO.deleteMsg(msgId);
	}
	@Override
	public VmtMessage getMsgByProperty(VmtMessage msg) {
		return msgDAO.getMsgByProperty(msg);
	}
	@Override
	public boolean isEquals(VmtMessage msg) {
		return getMsgByProperty(msg)!=null;
	}
	@Override
	public boolean isLoginNameUsed(String entryId) {
		VmtMessage msg=new VmtMessage();
		msg.setMsgTo(VmtMessage.MSG_TO_ADMIN);
		msg.setMsgType(VmtMessage.MSG_TYPE_USER_REGIST_APPLY);
		msg.setMsgStatus(VmtMessage.MSG_STATUS_NEED_REED);
		msg.setEntryId(entryId);
		return msgDAO.getMsgByProperty(msg)!=null;
	}
	@Override
	public void updateMsgReaded(VmtMessage msg) {
		VmtMessage selectedMsg=msgDAO.getMsgByProperty(msg);
		if(selectedMsg==null){
			return;
		}
		updateMsgReaded(selectedMsg.getId());
	}
	

}
