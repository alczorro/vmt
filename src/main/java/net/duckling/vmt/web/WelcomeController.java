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
package net.duckling.vmt.web;

import javax.servlet.http.HttpServletRequest;

import net.duckling.cloudy.common.PhoneUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.util.UmtRequestUtils;
import net.duckling.vmt.web.helper.SessionHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 访问根目录需要返回的页面逻辑写与此
 * @author lvly
 * @since 2013-4-15
 */
@Controller
@RequestMapping("/")
public class WelcomeController {
	@Autowired
	private VmtConfig config;
	/**
	 * 跳转到欢迎页，如果已登录，那么直接进首页
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView index(HttpServletRequest request){
		if(!SessionHelper.hasLogin(request)){
			if(PhoneUtils.isMobile(request)){
				return new ModelAndView(new RedirectView("user/mobile"));
			}else{
				return new ModelAndView("vmtIndex");
			}
		}else{
			if(PhoneUtils.isMobile(request)){
				return new ModelAndView(new RedirectView(UmtRequestUtils.getOauthLoginUrl(config)));
			}else{
				return new ModelAndView(new RedirectView("user/index"));
			}
		}
	}
}
