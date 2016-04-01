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
package net.duckling.vmt.service.thread;

import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.domain.email.Email;
import net.duckling.vmt.domain.email.EmailFile;
import net.duckling.vmt.domain.email.EmailGetter;
import net.duckling.vmt.service.IClbService;
import net.duckling.vmt.service.IMailService;

import org.apache.log4j.Logger;


public class BatchSendEmailJob implements Jobable{
	private static final Logger LOG=Logger.getLogger(BatchSendEmailJob.class);
	private Email email;
	private List<EmailGetter> emailGetter;
	private List<EmailFile> emailFiles;
	
	private IMailService emailService;
	private IClbService clbService;
	
	public BatchSendEmailJob(Email email,List<EmailGetter> egs,List<EmailFile> efs){
		this.email=email;
		this.emailGetter=egs;
		this.emailFiles=efs;
		
		//bean
		emailService=BeanFactory.getBean(IMailService.class);
		clbService=BeanFactory.getBean(IClbService.class);
	}
	@Override
	public void doJob() {
		downloadFile();
		if(CommonUtils.isNull(emailGetter)){
			LOG.info("email send job stoped,getter is null,"+getJobId());
			return;
		}
		emailService.sendCustomMail(email,emailGetter,emailFiles);
	}
	private void downloadFile(){
		if(CommonUtils.isNull(emailFiles)){
			return;
		}
		for(EmailFile ef:emailFiles){
			LOG.info("downloading..."+ef.getFileName());
			ef.setFile(clbService.downloadAsFile(ef.getClbId()));
		}
	}
	@Override
	public String getJobId() {
		return "batch-send-email-"+this.email.getSenderCstnetId()+"."+System.currentTimeMillis();
	}
	@Override
	public boolean isJobEquals(Jobable job) {
		return false;
	}

}
