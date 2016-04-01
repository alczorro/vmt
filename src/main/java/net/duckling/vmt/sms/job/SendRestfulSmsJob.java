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
package net.duckling.vmt.sms.job;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.IPUtils;
import net.duckling.vmt.domain.RestSms;
import net.duckling.vmt.service.IIPFilterService;
import net.duckling.vmt.service.thread.Jobable;
import net.duckling.vmt.sms.ISmsService;
import net.duckling.vmt.sms.domain.Sms;
import net.duckling.vmt.sms.domain.SmsGetter;
import net.duckling.vmt.sms.domain.SmsGroup;
import net.duckling.vmt.sms.domain.SmsGroupMember;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.rest.ServiceException;

public class SendRestfulSmsJob implements Jobable{
	private static final Logger LOG=Logger.getLogger(SendRestfulSmsJob.class);
	public RestSms rs;
	public String cstnetId;
	public int groupId;
	private ISmsService smsService;
	private UserService umtService;
	private IIPFilterService ipFilter;
	private HttpServletRequest request;
	public SendRestfulSmsJob(String cstnetId,int groupId,RestSms rs,HttpServletRequest request){
		this.cstnetId=cstnetId;
		this.groupId=groupId;
		this.rs=rs;
		this.request=request;
		smsService=BeanFactory.getBean(ISmsService.class);
		umtService=BeanFactory.getBean(UserService.class);
		ipFilter=BeanFactory.getBean(IIPFilterService.class);
	}
	private String getSmsContent(String subject){
		if(subject.startsWith("邮件需要您的审核")){
			return "院邮件系统给您发送了邮件审核通知，"+subject;
		}else if(subject.startsWith("邮件正在等待您的审核")){
			return "院邮件系统再次给您发送了邮件审核通知,"+subject;
		}
		
		return subject;
	}
	public void doJob() {
		String ip=IPUtils.getIP(request);
		if(!ipFilter.canAccess(ip)){
			 error("权限不足:"+ip);
			 return;
		}
		if(rs==null){
			error("未正确读取到配置");
			return;
		}
		rs.setGroupId(groupId);
		rs.setCstnetId(cstnetId);
		
		UMTUser u=umtService.getUMTUserByLoginName(rs.getCstnetId());
		if(u==null||CommonUtils.isNull(rs.getCstnetId())){
			 error("无法找到账户["+rs.getCstnetId()+"]");
			 return;
		}
		SmsGroup sg=smsService.getGroupById(rs.getGroupId());
		if(sg==null||rs.getGroupId()==0){
			 error("无法找到短信组["+rs.getGroupId()+"]");
			 return;
		}
		
		SmsGroupMember sm=smsService.getGroupMemberByGidAndUmtId(rs.getGroupId(), u.getUmtId());
		if(sm==null){
			 error("成员["+rs.getCstnetId()+"]不是组["+sg.getGroupName()+"]的成员");
			 return;
		}
		if(sg.getSmsCount()-sg.getSmsUsed()<=0){
			 error("短信余额不足["+sg.getGroupName()+"]");
			return;
		}
		UMTUser getterUmt=umtService.getUMTUserByLoginName(rs.getTo());
		if(getterUmt==null){
			 error("未找到接受成员["+rs.getTo()+"]");
			 return;
		}
		if(CommonUtils.isNull(getterUmt.getUmtId())){
			try {
				getterUmt.setUmtId(umtService.generateUmtId(new String[]{getterUmt.getCstnetId()})[0]);
			} catch (ServiceException e) {
				LOG.error("",e);
				error("生成UMTID失败");
				return;
			}
		}
		List<SmsGetter> getters = new ArrayList<SmsGetter>();
		SmsGetter getter=new SmsGetter();
		getter.setCstnetId(getterUmt.getCstnetId());
		getter.setTrueName(getterUmt.getTruename());
		getter.setMobile(rs.getSmsAddress());
		getter.setUmtId(getterUmt.getUmtId());
		getters.add(getter);
		Sms sms = new Sms(getters, getSmsContent(rs.getSubject()));
		sms.setSenderCstnetId(u.getCstnetId());
		sms.setSenderName(u.getTruename());
		sms.setSenderUmtId(u.getUmtId());
		sms.setGroupId(rs.getGroupId());
		smsService.sendSms(sms);
		LOG.info("to:"+rs.getSmsAddress()+"，content:"+sms.getContent());
		smsService.ifLessThan20PercentSendEmail(sg);
	}

	@Override
	public String getJobId() {
		return "restful.sms.jog."+System.currentTimeMillis();
	}
	@Override
	public boolean isJobEquals(Jobable job) {
		return false;
	}
	

	
	private JSONObject error(String msg){
		JSONObject jso=new JSONObject();
		jso.put("result", false);
		jso.put("desc",msg);
		LOG.error(jso);
		return jso;
	}
}

