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

import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.UmtRequestUtils;

import org.apache.log4j.Logger;

/**
 * 打印一行绝对的url，取值由vmt.properties 相关
 * @author lvly
 * @since 2013-4-15
 */
public class LogOutUrlTag extends TagSupport{
	private static final long serialVersionUID = 323811223720942755L;
	private static final Logger LOGGER=Logger.getLogger(LogOutUrlTag.class);
	private VmtConfig config;
	/**
	 * 
	 */
	public LogOutUrlTag(){
		this.config=BeanFactory.getBean(VmtConfig.class);
	}
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			out.print(UmtRequestUtils.getLogoutUrl(config,(HttpServletRequest)this.pageContext.getRequest()));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e,e);
		} catch (IOException e) {
			LOGGER.error(e,e);
		}
		return SKIP_BODY;
	}
}
