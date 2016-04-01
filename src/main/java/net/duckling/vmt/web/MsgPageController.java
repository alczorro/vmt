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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用于显示消息
 * @author lvly
 * @since 2013-5-23
 */
@Controller
@RequestMapping("msg/{word}")
public class MsgPageController {
	/**
	 * 展示消息页面
	 * @param msg
	 * @return
	 */
	@RequestMapping
	public ModelAndView msg(@PathVariable("word")String msg){
		ModelAndView mv=new ModelAndView("/user/msg");
		mv.addObject("msg",msg);
		return mv;
	}
}
