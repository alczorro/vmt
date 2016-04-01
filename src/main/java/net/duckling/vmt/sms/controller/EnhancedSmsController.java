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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.sms.ISmsService;
import net.duckling.vmt.sms.domain.Sms;
import net.duckling.vmt.sms.domain.SmsGetter;
import net.duckling.vmt.sms.domain.SmsGroup;
import net.duckling.vmt.sms.domain.SmsGroupMember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequestMapping("/user/enhanced/sms")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class EnhancedSmsController {
	@Autowired
	private ISmsService smsService;
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IGroupService groupService;
	@Autowired
	private IVmtIndexService indexService;
	@RequestMapping
	public ModelAndView show(
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		SmsGroup s = smsService.getLastGroup(user.getUserInfo().getUmtId());
		if (s == null) {
			return new ModelAndView("user/no_sms_msg"); 
		}
		return new ModelAndView(new RedirectView("sms/" + s.getId()));
	}
	

	
	@RequestMapping("{groupId}/resendSmsAll")
	@ResponseBody
	public JsonResult resendAll(@PathVariable("groupId")int gid,@RequestParam("smsId")int smsId,@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user){
		checkSmsMember(gid, user.getUserInfo().getUmtId());
		List<SmsGetter> smsGetters=smsService.getSmsGetterByFail(smsId);
		if(CommonUtils.isNull(smsGetters)){
			return new JsonResult("没有可以重发的短信");
		}
		SmsGroup sg=smsService.getGroupById(gid);
		if(sg.getSmsCount()-sg.getSmsUsed()<smsGetters.size()){
			return new JsonResult("短信余额不足");
		}
		Sms sms=smsService.getSmsById(smsId);
		sms.setGetter(smsGetters);
		smsService.resendSms(sms);
		return new JsonResult();
	}
	@RequestMapping("{groupId}/resendSms/{smsLogId}")
	@ResponseBody
	public JsonResult resendSms(@PathVariable("groupId")int gid,@PathVariable("smsLogId")int smsLogId,@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user){
		checkSmsMember(gid, user.getUserInfo().getUmtId());
		SmsGetter getter=smsService.getSmsGetterBySmsGetterId(smsLogId);
		if(getter==null){
			return new JsonResult("未找到对应人员");
		}
		if(!getter.getStat().equals("r:004")){
			return new JsonResult("状态为"+Sms.RT_CODE.get(getter.getStat())+",不可重发");
		}
		SmsGroup sg=smsService.getGroupById(gid);
		if(sg.getSmsCount()-sg.getSmsUsed()<=0){
			return new JsonResult("短信余额不足");
		}
		Sms sms=smsService.getSmsById(getter.getSmsId());
		sms.setGetter(Arrays.asList(getter));
		smsService.resendSms(sms);
		return new JsonResult();
	}
	

	@RequestMapping("{groupId}")
	public ModelAndView showByGid(@PathVariable("groupId") int gid,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		return getModelAndSetGroup(gid, "user/sms_send", user.getUserInfo()
				.getUmtId());
	}
	
	@RequestMapping("{groupId}/removeMember")
	@ResponseBody
	public JsonResult removeMember(@PathVariable("groupId") int gid,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user,@RequestParam("mId")int memberId){
		checkSmsAdmin(gid,user.getUserInfo().getUmtId());
		SmsGroupMember sgm=smsService.getGroupMemberByGidAndId(gid, memberId);
		if(sgm==null){
			return new JsonResult("改成员并非对应团队的成员");
		}
		smsService.removeMember(memberId);
		return new JsonResult();
	}
	@RequestMapping("{groupId}/changeAdmin")
	@ResponseBody
	public JsonResult changeAdmin(@PathVariable("groupId") int gid,@RequestParam("isAdmin") boolean isAdmin,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user,@RequestParam("mId")int memberId){
		String umtId=user.getUserInfo().getUmtId();
		checkSmsAdmin(gid,umtId);
		SmsGroupMember sgm=smsService.getGroupMemberByGidAndId(gid, memberId);
		if(sgm==null){
			return new JsonResult("改成员并非对应团队的成员");
		}
		if(umtId.equals(sgm.getUmtId())){
			return new JsonResult("请勿更改当前登录账号的权限");
		}
		smsService.changeMemberRole(memberId,isAdmin);
		return new JsonResult();
	}

	@RequestMapping("{groupId}/getSendLogDetail")
	@ResponseBody
	public JsonResult getSendLogDetail(@PathVariable("groupId") int gid,
			@RequestParam("smsId") int smsId,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		SmsGroupMember sgm = smsService.getGroupMemberByGidAndUmtId(gid, user
				.getUserInfo().getUmtId());
		if (sgm == null) {
			return new JsonResult("您不是该短信组的成员");
		}
		Sms sms;
		if (sgm.isAdmin()) {
			sms = smsService.getSmsById(smsId);

		} else {
			sms = smsService.getSmsByIdAndUmtId(smsId, user.getUserInfo()
					.getUmtId());
		}
		if (sms == null) {
			return new JsonResult("无法获得短信");
		}
		List<SmsGetter> getters = smsService.getSmsGetterBySmsId(sms.getId());
		sms.setGetter(getters);
		JsonResult js = new JsonResult();
		js.setData(sms);
		return js;
	}
	@RequestMapping(value="{groupId}",params="func=manage")
	public ModelAndView showManage(@PathVariable("groupId") int gid,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user){
		String umtId=user.getUserInfo().getUmtId();
		SmsGroupMember sgm=checkSmsMember(gid, umtId);
		ModelAndView mv=getModelAndSetGroup(gid,"user/sms_manage",umtId);
		List<SmsGroupMember> sgms=smsService.getGroupMemberByGroupId(gid);
		mv.addObject("members",sgms);
		mv.addObject("me",sgm);
		if(sgm.isAdmin()){
			mv.addObject("smsLog",smsService.getLogGroupByuserName(gid));
		}else{
			mv.addObject("smsLog",smsService.getLogGroupByuserName(gid,user.getUserInfo().getUmtId()));
		}
		
		return mv;
	}
	@RequestMapping("{groupId}/checkMemberUseable")
	@ResponseBody
	public boolean checkMemberUseable(@PathVariable("groupId")int gid,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user,
			@RequestParam("cstnetId")String email){
		checkSmsAdmin(gid, user.getUserInfo().getUmtId());
		VmtIndex vi=indexService.getUseableVmtUser(email);
		return vi!=null;
	}
	@RequestMapping("{groupId}/addMember")
	@ResponseBody
	public JsonResult addMember(@PathVariable("groupId")int gid,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user,
			@RequestParam("cstnetId")String email){
		checkSmsAdmin(gid, user.getUserInfo().getUmtId());
		JsonResult jr=new JsonResult();
		VmtIndex vi=indexService.getUseableVmtUser(email);
		if(vi==null){
			return new JsonResult("组织通讯录不存在此用户");
		}
		SmsGroupMember sgm = smsService.getGroupMemberByGidAndUmtId(gid,
				vi.getUmtId());
		if(sgm!=null){
			return new JsonResult("此用户已存在,请勿重复添加");
		}
		sgm=new SmsGroupMember();
		sgm.setAdmin(false);
		sgm.setCstnetId(email);
		sgm.setGroupId(gid);
		sgm.setUmtId(vi.getUmtId());
		sgm.setUserName(vi.getUserName());
		smsService.addMember(sgm);
		jr.setData(sgm);
		return jr;
	}
	

	@RequestMapping(value = "{groupId}", params = "func=log")
	public ModelAndView showLog(@PathVariable("groupId") int gid,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		ModelAndView mv = getModelAndSetGroup(gid, "user/sms_log", user
				.getUserInfo().getUmtId());
		SmsGroupMember sgm = smsService.getGroupMemberByGidAndUmtId(gid, user
				.getUserInfo().getUmtId());
		List<Sms> smss;
		if (sgm.isAdmin()) {
			smss = smsService.getSmsByGroupId(gid);
		} else {
			smss = smsService.getSmsByGroupIdAndUmtId(gid, user.getUserInfo()
					.getUmtId());
		}
		mv.addObject("smss", smss);
		return mv;
	}

	private ModelAndView getModelAndSetGroup(int gid, String viewName,
			String umtId) {
		SmsGroup sg = smsService.getGroupById(gid);
		checkSmsMember(gid, umtId);
		ModelAndView mv = new ModelAndView(viewName);
		mv.addObject("currGroup", sg);
		return mv;
	}

	private SmsGroupMember checkSmsMember(int gid, String umtId) {
		SmsGroupMember sgm=smsService.getGroupMemberByGidAndUmtId(gid, umtId);
		if (sgm==null) {
			throw new RuntimeException("你不是这个团队成员");
		}
		return sgm;
		
	}
	private SmsGroupMember checkSmsAdmin(int gid, String umtId) {
		SmsGroupMember sgm=smsService.getGroupMemberByGidAndUmtId(gid, umtId);
		if (sgm==null||!sgm.isAdmin()) {
			throw new RuntimeException("你不是这个团队的管理员");
		}
		return sgm;
	}

	@RequestMapping("/{groupId}/getMyTeam")
	@ResponseBody
	public JsonResult getOrgAndGroup(@PathVariable("groupId") int gid,
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		checkSmsMember(gid, user.getUserInfo().getUmtId());
		JsonResult result = new JsonResult();
		String umtId = user.getUserInfo().getUmtId();
		List<LdapOrg> orgs = orgService.getMyOrgs(umtId);
		List<LdapGroup> groups = groupService.getMyGroups(umtId);
		result.setData(merge(orgs, groups));
		return result;
	}

	@RequestMapping("/{groupId}/sendSms")
	@ResponseBody
	public JsonResult sendSms(@PathVariable("groupId") int gid,
			@RequestParam("content") String content,
			@RequestParam("name[]") String name[],
			@RequestParam("cstnetId[]") String cstnetIds[],
			@RequestParam("mobile[]") String mobile[],
			@RequestParam("umtId[]") String umtId[],
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user) {
		if (CommonUtils.isNull(name) || CommonUtils.isNull(cstnetIds)
				|| CommonUtils.isNull(mobile) || CommonUtils.isNull(umtId)) {
			return new JsonResult("缺乏提交参数");
		}
		int length = name.length;
		if (cstnetIds.length != length || mobile.length != length
				|| umtId.length != length) {
			return new JsonResult("提交参数长度有误");
		}
		checkSmsMember(gid, user.getUserInfo().getUmtId());
		SmsGroup g=smsService.getGroupById(gid);
		if((g.getSmsCount()-g.getSmsUsed())<length){
			return new JsonResult("短信余额不足");
		}
		List<SmsGetter> getter = getSmsGetters(umtId, cstnetIds, mobile, name);
		Sms sms = new Sms(getter, content);
		sms.setSenderCstnetId(user.getUserInfo().getCstnetId());
		sms.setSenderName(user.getUserInfo().getTrueName());
		sms.setSenderUmtId(user.getUserInfo().getUmtId());
		sms.setGroupId(gid);
		if(!sms.isValid()){
			return new JsonResult("收件人不允许超过一千人");
		}
		smsService.sendSms(sms);
		smsService.ifLessThan20PercentSendEmail(g);
		return new JsonResult();
	}

	private List<SmsGetter> getSmsGetters(String[] umtId, String[] cstnetIds,
			String[] mobile, String[] name) {
		int length = umtId.length;
		List<SmsGetter> getters = new ArrayList<SmsGetter>();
		for (int i = 0; i < length; i++) {
			getters.add(new SmsGetter(umtId[i], cstnetIds[i], name[i],
					mobile[i]));
		}
		return getters;

	}

	private List<Object> merge(List<LdapOrg> org, List<LdapGroup> group) {
		List<Object> result = new ArrayList<Object>();
		if (!CommonUtils.isNull(org)) {
			for (LdapOrg o : org) {
				result.add(o);
			}
		}
		if (!CommonUtils.isNull(group)) {
			for (LdapGroup g : group) {
				result.add(g);
			}
		}
		return result;
	}
}
