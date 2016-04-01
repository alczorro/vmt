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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.util.RenderUtils;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.SimpleEmail;
import net.duckling.vmt.domain.index.OrgDetail;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.message.VmtMessage;
import net.duckling.vmt.domain.message.VmtMsgColumns;
import net.duckling.vmt.service.IOrgDomainMappingService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;
import net.duckling.vmt.service.IVmtMessageService;
import net.duckling.vmt.service.thread.EmailSendJob;
import net.duckling.vmt.service.thread.JobThread;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;

@Controller
@RequestMapping("applyEmail")
public class ApplyRegistController {
	private static final Logger LOG=Logger.getLogger(ApplyRegistController.class);
	@Autowired
	private IVmtMessageService msgService;
	@Autowired
	private IOrgDomainMappingService domainService;
	@Autowired
	private IOrgService orgService;
	@Autowired
	private UserService userService;
	@Autowired
	private IUserService luserService;
	@Autowired
	private VmtConfig vmtConfig;
	
	@RequestMapping("/show")
	public ModelAndView show(@RequestParam("domain")String domain){
		return new ModelAndView("applyRegist").addObject("domain",domain);
	}
	@RequestMapping("/submit")
	@ResponseBody
	public JsonResult show(HttpServletRequest request){
		String validateCode=CommonUtils.trim(request.getParameter("validateCode"));
		if(!SimpleImageCaptchaServlet.validateResponse(request,validateCode )){
			return new JsonResult("验证码错误");
		}
		String email=CommonUtils.trim(request.getParameter("email")).toLowerCase();
		String domain=CommonUtils.trim(request.getParameter("domain")).toLowerCase();
		String loginName=email+"@"+domain;
		if(luserService.isGlobalLoginNameExists(loginName)|msgService.isLoginNameUsed(loginName)){
			return new JsonResult("用户名已使用");
		}
		OrgDetail od=domainService.getOrgByDomain(domain);
		if(od==null){
			LOG.warn("who regist the loginName["+loginName+"],not found the domain!!! get out");
			return new JsonResult();
		}
		LdapOrg org=orgService.getOrgBySymbol(od.getSymbol());
		if(org==null){
			return new JsonResult();
		}
		if(CommonUtils.isNull(org.getAdmins())){
			return new JsonResult("无对应管理员");
		}
		String trueName=CommonUtils.trim(request.getParameter("trueName"));
		String custom1=CommonUtils.trim(request.getParameter("custom1"));
		String custom2=CommonUtils.trim(request.getParameter("custom2"));
		String phone=CommonUtils.trim(request.getParameter("phone")); 
		String contractEmail=CommonUtils.trim(request.getParameter("contractEmail"));
		String other=CommonUtils.trim(request.getParameter("other"));
		
		
		VmtMessage msg=new VmtMessage();
		List<VmtMsgColumns> list=new ArrayList<VmtMsgColumns>();
		msg.setEntryId(loginName);
		msg.setMsgTo(VmtMessage.MSG_TO_ADMIN);
		msg.setMsgStatus(VmtMessage.MSG_STATUS_NEED_REED);
		msg.setMsgType(VmtMessage.MSG_TYPE_USER_REGIST_APPLY);
		msg.setTeamDN(org.getDn());
		msg.setTeamName(od.getName());
		msgService.insertMsg(msg);
		list.add(new VmtMsgColumns("email", loginName, msg.getId()));
		list.add(new VmtMsgColumns("trueName",trueName,msg.getId()));
		list.add(new VmtMsgColumns("custom1",custom1,msg.getId()));
		list.add(new VmtMsgColumns("custom2",custom2,msg.getId()));
		list.add(new VmtMsgColumns("phone",phone,msg.getId()));
		list.add(new VmtMsgColumns("contractEmail",contractEmail,msg.getId()));
		list.add(new VmtMsgColumns("other", other, msg.getId()));
		msgService.insertMsgColumns(list);
		List<LdapUser> admins=luserService.searchUsersByUmtId(org.getDn(), org.getAdmins());
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("${email}", loginName);
		param.put("${trueName}", trueName);
		param.put("${trueName}",trueName);
		param.put("${custom1}",custom1);
		param.put("${custom2}",custom2);
		param.put("${phone}",phone);
		param.put("${contractEmail}",contractEmail);
		param.put("${other}", other);
		param.put("${url}", vmtConfig.getMyBaseUrl()+"/user/message");
		String title="有新的电子邮件账号申请";
		String content=RenderUtils.getHTML(param, RenderUtils.APPLY_REGIST);
		for(LdapUser u:admins){
			JobThread.addJobThread(new EmailSendJob(new SimpleEmail(u.getCstnetId(), content, title)));
		}
		return new JsonResult();
	}
	
	
}
