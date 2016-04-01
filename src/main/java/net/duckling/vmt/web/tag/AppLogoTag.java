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
package net.duckling.vmt.web.tag;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.domain.VmtApp;

import org.apache.log4j.Logger;

/**
 * 打印一行绝对的url，取值由vmt.properties 相关
 * @author lvly
 * @since 2013-4-15
 */
public class AppLogoTag extends TagSupport{
	private static final long serialVersionUID = 32381213720942755L;
	private static final Logger LOGGER=Logger.getLogger(AppLogoTag.class);
	private VmtApp app;
	private VmtConfig config;
	
	public VmtApp getApp() {
		return app;
	}

	public void setApp(VmtApp app) {
		this.app = app;
	}
	public AppLogoTag(){
		this.config=BeanFactory.getBean(VmtConfig.class);
	}
	/**
	 * 
	 */
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request=(HttpServletRequest)this.pageContext.getRequest();
		try {
			String defaultLogo=request.getContextPath()+"/logo/0";
			if(app==null){
				out.print(defaultLogo);
				return SKIP_BODY;
			}
			String prefix=request.getContextPath();
			if(app.getAppType().equals(VmtApp.APP_TYPE_OAUTH)){
				prefix=config.getUmtBase();
			}else{
				prefix=request.getContextPath()+"/logo/";
			}
			if(!CommonUtils.isNull(app.getLogo100Url())){
				out.print(prefix+app.getLogo100Url());
			}else if(!CommonUtils.isNull(app.getLogo64Url())){
				out.print(prefix+app.getLogo64Url());
			}else if(!CommonUtils.isNull(app.getLogo32Url())){
				out.print(prefix+app.getLogo32Url());
			}else if(!CommonUtils.isNull(app.getLogo16Url())){
				out.print(prefix+app.getLogo16Url());
			}else{
				out.print(defaultLogo);
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e,e);
		} catch (IOException e) {
			LOGGER.error(e,e);
		}
		return SKIP_BODY;
		
	}
}
