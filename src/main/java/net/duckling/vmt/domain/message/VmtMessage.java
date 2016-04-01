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
package net.duckling.vmt.domain.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@TableMapping("vmt_message")
public class VmtMessage{
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping 
	private String msgTo;
	public static final String MSG_TO_TEAM="toTeam";
	public static final String MSG_TO_ADMIN="toAdmin";
	public static final String MSG_TO_USER="toUser";
	@FieldMapping 
	private String teamName;
	@FieldMapping
	private String teamDN;
	public String getTeamName() {
		return teamName;
	}
	
	public static JSONArray groupBy(List<VmtMessage> msgs){
		JSONArray groups=new JSONArray();
		if(CommonUtils.isNull(msgs)){
			return groups;
		}
		Map<String,List<VmtMessage>> map=new HashMap<String,List<VmtMessage>>();
		for(VmtMessage msg:msgs){
			String key=msg.getTeamDN()+"_"+msg.getTeamName();
			if(map.containsKey(key)){
				map.get(key).add(msg);
			}else{
				List<VmtMessage> ms=new ArrayList<VmtMessage>();
				ms.add(msg);
				map.put(key, ms);
			}
		}
		for(Entry<String,List<VmtMessage>> entry:map.entrySet()){
			JSONObject group=new JSONObject();
			String[] teamInfo=entry.getKey().split("_");
			group.put("teamDN",teamInfo[0]);
			group.put("teamName", teamInfo[1]);
			group.put("list", entry.getValue());
			groups.add(group);
		}
		return groups;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamDN() {
		return teamDN;
	}

	public void setTeamDN(String teamDN) {
		this.teamDN = teamDN;
	}

	@FieldMapping
	private String msgType;
	public static final String MSG_TYPE_USER_ADD_TEMP="addUserTemp";
	public static final String MSG_TYPE_USER_APPLY_TEMP="userApplyGroupTemp";
	public static final String MSG_TYPE_USER_REGIST_APPLY="userRegistApply";
	@FieldMapping
	private String msgStatus;
	@FieldMapping
	private String entryId;
	public static final String MSG_STATUS_NEED_REED="needRead";
	public static final String MSG_STATUS_OVER="over";
	public static final String MSG_STATUS_DELETED="deleted";
	
	public Map<String,String> getKeyValue(){
		Map<String,String> keyValue=new HashMap<String,String>();
		for(VmtMsgColumns c:getColumns()){
			keyValue.put(c.getColumnName(), c.getColumnValue());
		}
		return keyValue;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}


	public String getMsgTo() {
		return msgTo;
	}

	public void setMsgTo(String msgTo) {
		this.msgTo = msgTo;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public List<VmtMsgColumns> getColumns() {
		return columns;
	}

	public void setColumns(List<VmtMsgColumns> columns) {
		this.columns = columns;
	}

	private List<VmtMsgColumns> columns=new ArrayList<VmtMsgColumns>();
	public void addColumn(VmtMsgColumns c){
		columns.add(c);
	}
	
}
