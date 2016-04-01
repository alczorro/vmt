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
package net.duckling.vmt.service.thread;

import java.util.Arrays;

import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.domain.SimpleEmail;
import net.duckling.vmt.service.IMailService;

/**
 * @author lvly
 * @since 2013-5-31
 */

public class EmailSendJob  implements Jobable{
	private IMailService emailService;
	private SimpleEmail email;
	/**
	 * @param email
	 */
	public EmailSendJob(SimpleEmail email){
		emailService=BeanFactory.getBean(IMailService.class);
		this.email=email;
	}
	@Override
	public void doJob() {
		emailService.sendEmail(email);
	}
	@Override
	public String getJobId() {
		return SEND_MAIL_JOB+Arrays.toString(email.getAddress())+"["+email.getTitle()+"]";
	}
	@Override
	public boolean isJobEquals(Jobable job) {
		return false;
	}
}
