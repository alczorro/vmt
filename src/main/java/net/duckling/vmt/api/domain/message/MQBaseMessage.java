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
package net.duckling.vmt.api.domain.message;

import java.io.Serializable;
import java.util.Date;

import net.duckling.falcon.api.serialize.JSONMapper;
/**
 * @author lvly
 * @since 2013-8-2
 */
public class MQBaseMessage implements Serializable{  
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6429179629282060401L;
	
	protected Date publishTime;
	protected String type;
	protected int msgId;
	
	public MQBaseMessage(String operation,String scope){
		this.publishTime=new Date();
		buildType(operation, scope);
	}
	public String toJsonString(){
		return JSONMapper.getJSONString(this);
	}
	
	
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	public String getType() {
		return type;
	}

	public static final String OPERATION_CREATE="create";
	public static final String OPERATION_UPDATE="update";
	public static final String OPERATION_DELETE="delete";
	public static final String OPERATION_MOVE="move";
	public static final String OPERATION_REFRESH="refresh";
	public static final String OPERATION_SWITCH_DCHAT="switchDchat";
	public static final String OPERATION_SWITCH_PHONE="switchPhone";
	
	public static final String SCOPE_ORG="org";
	public static final String SCOPE_GROUP="group";
	public static final String SCOPE_DEPT="dept";
	public static final String SCOPE_USER="user";
	public static final String SCOPE_APP_PROFILE="appProfile";
	
	protected void buildType(String operation,String scope){
		this.type=operation+"."+scope;
	}
	
	
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
}
