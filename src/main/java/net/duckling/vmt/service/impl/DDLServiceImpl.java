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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.ddl.DDLGroup;
import net.duckling.vmt.service.IDDLService;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.rest.ServiceException;

/**
 * @author lvly
 * @since 2013-5-30
 */
@Service
public class DDLServiceImpl implements IDDLService {
	private static final Logger LOG = Logger.getLogger(DDLServiceImpl.class);
	@Autowired
	private VmtConfig config;
	@Autowired
	private UserService umtService;

	@Override
	public List<String> getAllTeamCode() {
		JSONArray json=getJsonArray(getJson(config.getDdlReadAllPath()));
		return getListFromArrayJson(json);
	}
	private List<String> getListFromArrayJson(JSONArray array){
		List<String> result=new ArrayList<String>();
		if(array==null){
			return result;
		}
		for(Object obj:array){
			result.add(obj.toString());
		}
		return result;
	}
	
	@Override
	public DDLGroup getTeamInfo(String teamCode) {
		String json = getJson(config.getDdlReadOnePath() + teamCode);
		JSONObject team=getJsonObject(json);
		if(team==null){
			return null;
		}
		JSONObject teamInfo=(JSONObject)team.get("teamInfo");
		DDLGroup group=new DDLGroup();
		group.setAdmin(getUmtIds(teamInfo.get("admin")));
		group.setTeamName(teamInfo.get("teamName").toString());
		group.setTeamCode(teamInfo.get("teamCode").toString());
		group.setUsers(getUmtIds(team.get("userInfo")));
		return group;
	}
	


	private String[] getUmtIds(Object sth) {
		JSONArray array=(JSONArray)sth;
		if (array == null) {
			return null;
		}
		String[] emails = new String[array.size()];
		int index = 0;
		for (Object obj : array) {
			emails[index++] = obj.toString();
		}
		try {
			return umtService.generateUmtId(emails);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	private String getJson(String path) {
		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage()+",can't touch this url="+path, e);
			return null;
		}
		try (InputStream ins = url.openConnection().getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(ins, KEY.GLOBAL_ENCODE));) {
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
	
	private JSONObject getJsonObject(String json) {
		if(json==null){
			return null;
		}
		try {
			return (JSONObject) new JSONParser().parse(json);
		} catch (ParseException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
	private JSONArray getJsonArray(String json){
		if(json==null){
			return null;
		}
		try {
			return (JSONArray) new JSONParser().parse(json);
		} catch (ParseException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
	@Override
	public List<String> getMyTemCode(String uid) {
		JSONArray jsonArray=getJsonArray(getJson(config.getDdlMyGroupsPath()+uid));
		return getListFromArrayJson(jsonArray);
	}
}
