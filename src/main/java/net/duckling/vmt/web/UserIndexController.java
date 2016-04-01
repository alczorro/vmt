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

import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.web.helper.IndexHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户登录成功以后的页面
 * @author lvly
 * @since 2013-4-15
 */
@Controller 
@RequestMapping("/user")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class UserIndexController {

	
	@Autowired
	private IndexHelper indexHelper;
	
	private static final String ALL="all";
	/**
	 * 显示搜索视图，全部
	 * @param user
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView display(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user) {
		return display(user,null);
	}
	/**
	 * 显示搜索视图，指定某个机构
	 * @param user
	 * @param dn
	 * @return
	 */
	@SecurityMapping(expect=ALL)
	@RequestMapping(value="/index",params={"dn"})
	public ModelAndView display(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,@RequestParam("dn")String dn) {
		return indexHelper.getModelView(user,"/user/index",dn);
	}
	/**
	 * 显示层级视图，指定某个机构
	 * @param user
	 * @param dn
	 * @return
	 */
	@SecurityMapping
	@RequestMapping(value="/index2",params={"dn"})
	public ModelAndView display2(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user,@RequestParam("dn")String dn){
		return indexHelper.getModelView(user,"/user/index2",dn);
	}
	/**
	 * 显示层级视图，指定全部
	 * @param user
	 * @return
	 */
	@RequestMapping("/index2")
	public ModelAndView display2(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		return display2(user,null);
	}
	
	
}
