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

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.common.PhoneUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.Oauth;
import cn.vlabs.umt.oauth.UMTOauthConnectException;
import cn.vlabs.umt.oauth.UserInfo;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;

/**
 * @author lvly
 * @since 2013-4-24
 */
@Controller
@RequestMapping("/oauth/callback")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class OauthLoginCallBackController {
	private static final Logger LOGGER=Logger.getLogger(OauthLoginCallBackController.class);
	@Autowired
	private VmtConfig config;
	@Autowired
	private UserService userService;
	
	/**
	 * Oauth登录回调方法页面
	 * @param request
	 * @param code oauth返回的码
	 * */
	@RequestMapping(params={"code","state"})
	public ModelAndView disPlay(HttpServletRequest request,
			@RequestParam("code")String code,
			@RequestParam("state") String state){

		AccessToken token=null;
		UserInfo userInfo=null;
		try {
			Oauth oauth = new Oauth(config.getOauthUmtProp());
			token = oauth.getAccessTokenByRequest(request);
			userInfo = token.getUserInfo();
		} catch (UMTOauthConnectException e){
			LOGGER.error(e.getMessage(),e);
		} catch (OAuthProblemException e) {
			LOGGER.error(e.getMessage()+"["+e.getError()+":"+e.getDescription()+"]",e);
		}
		ModelAndView mv=null;
		
		if(userInfo==null){
			mv=new ModelAndView("user/msg");
			mv.addObject("msg", "登陆异常，请您检查登陆参数是否还有效");
		}else{
			mv=doLoginSuccess(userInfo,state);
		}
		return mv;
	}
	private ModelAndView doLoginSuccess(UserInfo userInfo,String state){
		ModelAndView mv=new ModelAndView(new RedirectView(state));
		VmtSessionUser user=new VmtSessionUser();
		user.setUserInfo(userInfo);
		String domain=EmailUtils.getDomain(userInfo.getCstnetId());
		//判断用户是否是coreMail所支持的域名
		if(KEY.GLOBAL_ESCIENCE_DOMAIN.equals(domain)){
			user.setCoreMailDomain("");
		}else if(userService.domainExist(domain)){
			user.setCoreMailDomain(domain);
		}else{
			user.setCoreMailDomain("");
		}
		//判断用户是否是超级管理员
		String[] superAdmins=config.getSuperAdmin();
		user.setIsSuperAdmin(CommonUtils.isEqualsContain(superAdmins, userInfo.getCstnetId()));
		mv.addObject(KEY.SESSION_LOGIN_USER,user);
		return mv;
	}
	/**
	 * 如果没有返回地址，则调用跟这个
	 * @param request
	 * @param code
	 * @return
	 */
	@RequestMapping
	public ModelAndView disPlay(HttpServletRequest request,@RequestParam("code")String code){
		if(PhoneUtils.isMobile(request)){
			return disPlay(request, code,"../user/mobile");
		}
		return disPlay(request, code, "../user/index");
	}

}
