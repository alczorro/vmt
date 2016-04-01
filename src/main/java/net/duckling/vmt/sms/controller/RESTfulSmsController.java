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
package net.duckling.vmt.sms.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import net.duckling.vmt.domain.RestSms;
import net.duckling.vmt.service.thread.JobThread;
import net.duckling.vmt.sms.job.SendRestfulSmsJob;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sms/restful")
public class RESTfulSmsController {
	private static final Logger LOG=Logger.getLogger(RESTfulSmsController.class);
	@ResponseBody
	@RequestMapping("{cstnetId}/{groupId}")
	public JSONObject sendSms(
			@PathVariable("cstnetId")String cstnetId,
			@PathVariable("groupId")int groupId,
			HttpServletRequest request){
		JSONObject jso=new JSONObject();
		JobThread.addJobThread(new SendRestfulSmsJob(cstnetId, groupId, readSms(request),request));
		jso.put("flag", true);
		return jso;
	}
	private RestSms readSms(HttpServletRequest request){
		RestSms sms=new RestSms();
		try{
			sms.setMid(request.getParameter("Mid"));
			sms.setSmsAddress(request.getParameter("SMSADDR"));
			sms.setTo(request.getParameter("To"));
			sms.setSubject(getSubject(request.getParameter("Subject")));
			sms.setVersion(request.getParameter("Version"));
			sms.setDecodeCharSet(request.getParameter("DecodeCharSet"));
			sms.setFrom(request.getParameter("From"));
		}
		catch(RuntimeException e){
			LOG.error("",e);
			return null;
		}
		return sms;
	}
	private String getSubject(String subject){
		String result="";
		try {
			result=MimeUtility.decodeText(subject);
		} catch (UnsupportedEncodingException e) {
			LOG.error("",e);
		}
		
		return result;
	}
}
