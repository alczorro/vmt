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
package net.duckling.vmt.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import net.duckling.common.util.CommonUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * 系统配置项，路径是conf/vmt.properties
 * @author lvly
 * @since 2013-4-15
 */
@Configuration
@PropertySource("classpath:/conf/vmt.properties")
public class VmtConfig implements Serializable{
	private static final Logger LOGGER=Logger.getLogger(VmtConfig.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 7522141991535347291L;
	
	
	/**
	 * Oauth Param
	 * */

	@Value("${oauth.umt.client_id}")
	private String oauthUmtClientId;
	@Value("${oauth.umt.client_secret}")
	private String oauthUmtClientSecret;
	@Value("${oauth.umt.redirect_uri}")
	private String oauthUmtRedirectURL;
	@Value("${oauth.umt.access_token_URL}")
	private String oauthUmtAccessTokenURL;
	@Value("${oauth.umt.authorize_URL}")
	private String oauthUmtAuthorizeURL;
	@Value("${oauth.umt.scope}")
	private String oauthUmtScope;
	@Value("${oauth.umt.theme}")
	private String oauthUmtTheme;
	/**
	 * Umt Params
	 * */
	@Value("${duckling.umt.logout}")
	private String umtLogoutUrl;
	@Value("${duckling.umt.service}")
	private String umtServiceUrl;
	@Value("${duckling.umt.site}")
	private String umtBase;
	/**
	 * Ldap Param
	 * */
	@Value("${ldap.base.dn}")
	private String baseDN;
	/**Email Service*/
	@Value("${email.mail.smtp.host}")
	private String stmpHost;
	@Value("${email.mail.smtp.auth}")
	private String stmpAuth;
	@Value("${email.mail.pop3.host}")
	private String pop3Auth;
	@Value("${email.username}")
	private String emailUserName;
	@Value("${email.password}")
	private String emailPassword;
	@Value("${email.display.name}")
	private String emailDisplay;
	/**
	 * MyParam
	 **/
	@Value("${my.base.url}")
	private String myBaseUrl;
	@Value("${my.super.admin}")
	private String superAdmin;
	/**
	 * coreMail config
	 * */
	@Value("${coremail.api.ip}")
	private String coreMailIP;
	@Value("${coremail.api.port}")
	private int coreMailPort;
	@Value("${coremail.create.user.cos}")
	private String coreMailCreateUserCos;
	@Value("${coremail.providerId}")
	private String coreMailProviderId;
	
	
	public String getCoreMailCreateUserCos() {
		return coreMailCreateUserCos;
	}



	/**
	 * DDL config
	 * */
	@Value("${ddl.api.readAllGroup}")
	private String ddlReadAllPath;
	@Value("${ddl.api.readAGroup}")
	private String ddlReadOnePath;
	@Value("${ddl.api.readMyGroups}")
	private String ddlMyGroupsPath;
	
	/**
	 * mq config
	 * */
	@Value("${mq.ip}")
	private String mqIp;
	@Value("${mq.exchangeName}")
	private String mqExchangeName;
	@Value("${mq.username}")
	private String mqUserName;
	@Value("${mq.password}")
	private String mqPasswd;
	
	/**
	 * clb config below
	 * */
	@Value("${clb.url}")
	private String clbUrl;
	@Value("${clb.pricipal.user}")
	private String clbUserName;
	@Value("${clb.pricipal.user}")
	private String clbPassword;
	public String getClbUrl() {
		return clbUrl;
	}
	@Value("${sms.base.url}")
	private String smsBaseUrl;
	@Value("${sms.account}")
	private String smsAccount;
	@Value("${sms.password}")
	private String smsPassword;
	
	@Value("${dchat.notice.url}")
	private String dchatNoticeUrl;
	
	public String getDchatNoticeUrl() {
		return dchatNoticeUrl;
	}
	public String getSmsAccount() {
		return smsAccount;
	}
	public String getSmsPassword() {
		return smsPassword;
	}
	public String getSmsBaseUrl(){
		return this.smsBaseUrl;
	}
	public String getSmsSendUrl(){
		return this.smsBaseUrl+"/sendSms";
	}
	public String getSmsStateUrl(){
		return this.smsBaseUrl+"/getSmsState";
	}

	public String getClbUserName() {
		return clbUserName;
	}



	public String getClbPassword() {
		return clbPassword;
	}



	/**
	 * 默认加载一份配置文件到内存，以便动态获取使用
	 * */
	private static Properties props;
	static{
		if(props==null){
			props=new Properties();
			try(
				InputStream s=new ClassPathResource("/conf/vmt.properties").getInputStream();
				){
				props.load(s);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}
	}
	/**
	 * 没有成员变量的情况，动态获取值用
	 * @param key 欲获取相对应的key的值
	 * @return 返回相对key的值
	 * */
	public String get(String key){
		return props.getProperty(key);
	}
	
	
	
	public String getCoreMailIP() {
		return coreMailIP;
	}
	/**
	 * 获得系统配置的超级管理员，邮箱以逗号分隔，如果是一个，则无需逗号
	 * @return split出来的配置项
	 */
	public String[] getSuperAdmin(){
		if(CommonUtils.isNull(superAdmin)||!superAdmin.contains(",")){
			return new String[]{superAdmin};
		}
		return superAdmin.split(",");
	}



	public int getCoreMailPort() {
		return coreMailPort;
	}



	public static Logger getLogger() {
		return LOGGER;
	}



	public String getMqUserName() {
		return mqUserName;
	}



	public String getMqPasswd() {
		return mqPasswd;
	}



	public static Properties getProps() {
		return props;
	}



	public String getEmailDisplay() {
		return emailDisplay;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getStmpHost() {
		return stmpHost;
	}



	public String getStmpAuth() {
		return stmpAuth;
	}



	public String getPop3Auth() {
		return pop3Auth;
	}



	public String getEmailUserName() {
		return emailUserName;
	}



	public String getEmailPassword() {
		return emailPassword;
	}

	/**
	 * @return
	 */
	public String getBaseDN(){
		return this.baseDN;
	}
	
	
	
	public String getMqIp() {
		return mqIp;
	}



	public String getMqExchangeName() {
		return mqExchangeName;
	}



	public String getUmtBase() {
		return umtBase;
	}



	public String getUmtServiceUrl() {
		return umtServiceUrl;
	}

	/**
	 * getter 
	 * @return
	 */
	public String getUmtLogoutUrl(){
		return this.umtLogoutUrl;
	}
	
	public String getOauthUmtClientId() {
		return oauthUmtClientId;
	}

	public String getOauthUmtClientSecret() {
		return oauthUmtClientSecret;
	}

	public String getOauthUmtRedirectURL() {
		return oauthUmtRedirectURL;
	}
	public String getOauthUmtAccessTokenURL() {
		return oauthUmtAccessTokenURL;
	}
	public String getOauthUmtAuthorizeURL() {
		return oauthUmtAuthorizeURL;
	}
	public String getOauthUmtScope() {
		return oauthUmtScope;
	}
	public String getOauthUmtTheme() {
		return oauthUmtTheme;
	}
	
	/**
	 * Umt的Oauth需要Properties参数，从全局系统参数里面配置，然后读出来
	 * @return
	 */
	public Properties getOauthUmtProp(){
		Properties prop=new Properties();
		prop.setProperty("client_id", getOauthUmtClientId());
		prop.setProperty("client_secret", getOauthUmtClientSecret());
		prop.setProperty("redirect_uri", getOauthUmtRedirectURL());
		prop.setProperty("access_token_URL",getOauthUmtAccessTokenURL());
		prop.setProperty("authorize_URL", getOauthUmtAuthorizeURL());
		prop.setProperty("scope",getOauthUmtScope());
		prop.setProperty("theme", getOauthUmtTheme());
		return prop;
	}


	/**
	 * 如果想让Spring自动装填，自动替换值，请写此方法，因为所以科学道理
	 * @return
	 * 
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	public String getMyBaseUrl() {
		return myBaseUrl;
	}
	public String getDdlReadAllPath() {
		return ddlReadAllPath;
	}
	public String getDdlReadOnePath() {
		return ddlReadOnePath;
	}



	public String getDdlMyGroupsPath() {
		return ddlMyGroupsPath;
	}



	/**
	 * @return
	 */
	public String getCoreMailProviderId() {
		return coreMailProviderId;
	}

	
}
