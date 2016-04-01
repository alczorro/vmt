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
package net.duckling.vmt.service;

import java.util.List;

import net.duckling.vmt.domain.SimpleEmail;
import net.duckling.vmt.domain.email.Email;
import net.duckling.vmt.domain.email.EmailFile;
import net.duckling.vmt.domain.email.EmailGetter;

/**
 * 邮件发送接口,程序勿调用，通义用EmailSendThread.add，用队列实现
 * <br> 如果要求实施性较高，则可以调用
 * @author lvly
 * @since 2013-5-13
 */	
public interface IMailService {
	
	/**
	 * 实施发邮件
	 * @param mail 简单邮件内容定义
	 * @return
	 */
	boolean sendEmail(SimpleEmail mail);

	/**
	 * 用指定人的名义发送邮件
	 * */
	void sendCustomMail(Email email, List<EmailGetter> emailGetter,
			List<EmailFile> fils);
	
}
