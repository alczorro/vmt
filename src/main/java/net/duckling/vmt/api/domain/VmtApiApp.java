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
package net.duckling.vmt.api.domain;


/**
 * 应用列表POJO
 * */
public class VmtApiApp {
	/**
	 * 应用名称
	 * */
	private String appName;
	/**
	 *Oauth的clientID,如果非Oauth应用则为空
	 * */
	private String appClientId;
	/**
	 *应用的网址 
	 * */
	private String appClientUrl;
	
	/**
	 * 应用的手机网址
	 * */
	private String appMobileUrl;
	/**
	 * 应用类型,local-为自定义应用,oauth-为oauth应用
	 * */
	private String appType=APP_TYPE_LOCAL;
	public static final String APP_TYPE_OAUTH="oauth";
	public static final String APP_TYPE_LOCAL="local";
	/**
	 * 所属团队dn,如果是个人应用设置,teamDN为空
	 * */
	private String teamDn;
	/**
	 * 所属团队的dn,如果是个人应用设置,teamSymbol为空
	 **/
	private String teamSymbol;
	/**
	 * 100*100像素的 应用图标,直接用即可
	 * */
	private String logo100Url;
	/**
	 * 64*64像素的 应用图标,直接用即可
	 * */
	private String logo64Url;
	/**
	 * 32*32像素的 应用图标,直接用即可
	 * */
	private String logo32Url;
	/**
	 * 16*16像素的 应用图标,直接用即可
	 * */
	private String logo16Url;
	/**24*24*/
	private String logoCustomUrl;
	
	
	public String getLogoCustomUrl() {
		return logoCustomUrl;
	}
	public void setLogoCustomUrl(String logoCustomUrl) {
		this.logoCustomUrl = logoCustomUrl;
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
	public String getAppMobileUrl() {
		return appMobileUrl;
	}
	public void setAppMobileUrl(String appMobileUrl) {
		this.appMobileUrl = appMobileUrl;
	}
}
