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
package net.duckling.vmt.domain;

import java.io.Serializable;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping;
import net.duckling.cloudy.db.simpleORM.anno.FieldMapping.Type;
import net.duckling.cloudy.db.simpleORM.anno.TableMapping;
import net.duckling.falcon.api.serialize.JSONMapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@TableMapping("vmt_app")
public class VmtApp implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4244612993184200485L;
	@FieldMapping(type=Type.ID)
	private int id;
	@FieldMapping("app_name")
	private String appName;
	@FieldMapping("app_client_id")
	private String appClientId;
	@FieldMapping("app_url")
	private String appClientUrl;
	@FieldMapping("app_mobile_url")
	private String appMobileUrl;
	@FieldMapping("app_type")
	private String appType=APP_TYPE_LOCAL;
	public static final String APP_TYPE_OAUTH="oauth";
	public static final String APP_TYPE_LOCAL="local";
	@FieldMapping("team_dn")
	private String teamDn;
	@FieldMapping("team_symbol")
	private String teamSymbol;
	@FieldMapping("logo_100_url")
	private String logo100Url;
	@FieldMapping("logo_64_url")
	private String logo64Url;
	@FieldMapping("logo_32_url")
	private String logo32Url;
	@FieldMapping("logo_16_url")
	private String logo16Url;
	@FieldMapping("logo_custom_url")
	private String logoCustomUrl;
	
	
	public String getLogoCustomUrl() {
		return logoCustomUrl;
	}
	public void setLogoCustomUrl(String logoCustom) {
		this.logoCustomUrl = logoCustom;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppClientId() {
		return appClientId;
	}
	public void setAppClientId(String appClientId) {
		this.appClientId = appClientId;
	}
	public String getAppClientUrl() {
		return appClientUrl;
	}
	public void setAppClientUrl(String appClientUrl) {
		this.appClientUrl = appClientUrl;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getTeamDn() {
		return teamDn;
	}
	public void setTeamDn(String teamDn) {
		this.teamDn = teamDn;
	}
	public String getTeamSymbol() {
		return teamSymbol;
	}
	public void setTeamSymbol(String teamSymbol) {
		this.teamSymbol = teamSymbol;
	}
	public String getLogo100Url() {
		return logo100Url;
	}
	public void setLogo100Url(String logo100Url) {
		this.logo100Url = logo100Url;
	}
	public String getLogo64Url() {
		return logo64Url;
	}
	public void setLogo64Url(String logo64Url) {
		this.logo64Url = logo64Url;
	}
	public String getLogo32Url() {
		return logo32Url;
	}
	public void setLogo32Url(String logo32Url) {
		this.logo32Url = logo32Url;
	}
	public String getLogo16Url() {
		return logo16Url;
	}
	public void setLogo16Url(String logo16Url) {
		this.logo16Url = logo16Url;
	}
	public String toJson(){
		return JSONMapper.getJSONString(this);
	}
	public static String toJson(List<VmtApp> apps){
		return JSONMapper.getJSONString(apps);
	}
	public static List<VmtApp> convert(String vmtAppCopy) {
		if(CommonUtils.isNull(vmtAppCopy)){
			return null;
		}
		Gson gson = new Gson();
		return gson.fromJson(vmtAppCopy,new TypeToken<List<VmtApp>>(){}.getType());
	}
	public String getFkId(){
		if(APP_TYPE_OAUTH.equals(this.appType)){
			return this.appClientId;
		}else{
			return this.id+"";
		}
	}
	public String getAppMobileUrl() {
		return appMobileUrl;
	}
	public void setAppMobileUrl(String appMobileUrl) {
		this.appMobileUrl = appMobileUrl;
	}
	
}
