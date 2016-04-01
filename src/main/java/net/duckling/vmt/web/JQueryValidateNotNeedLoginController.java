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
package net.duckling.vmt.web;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.octo.captcha.Captcha;
import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.ImageCaptchaService;


/**
 * 针对一些jquery validator远程校验的接口
 * @author lvly
 * @since 2013-6-21
 */
@Controller
@RequestMapping("/jq/")
public class JQueryValidateNotNeedLoginController {
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IVmtMessageService msgService;

	@RequestMapping("/isUserNameExist")
	@ResponseBody
	public boolean userNameUsed(@RequestParam("email")String email,@RequestParam("domain")String domain){
		String loginName=email+"@"+domain;
		return !userService.isGlobalLoginNameExists(loginName)&&!msgService.isLoginNameUsed(loginName);
	}
	@RequestMapping("validateCode")
	@ResponseBody
	public boolean validateCode(@RequestParam("validateCode")String validateCode,HttpServletRequest request)throws Exception{
		HttpSession session=request.getSession();
		ImageCaptchaService obj=(ImageCaptchaService)(SimpleImageCaptchaServlet.service);
		Field f=obj.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("store");
		f.setAccessible(true);
		FastHashMapCaptchaStore fs=(FastHashMapCaptchaStore)(f.get(obj));
		boolean result=false;
		if(fs!=null&&session!=null){
			Captcha cp=fs.getCaptcha(session.getId());
			if(cp!=null){
				result=(cp.validateResponse(validateCode.trim()));
			}
		}
		return result;
	}
	
	
	
}
