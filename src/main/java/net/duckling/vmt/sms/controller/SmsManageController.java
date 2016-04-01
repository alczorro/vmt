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

import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.index.VmtIndex;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.sms.ISmsService;
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

@Controller
@RequestMapping("/user/sms")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
@SecurityMapping(level = SecurityLevel.SUPER_ADMIN)
public class SmsManageController {
	@Autowired
	private IVmtIndexService indexService;
	@Autowired
	private ISmsService smsService;

	@RequestMapping("{groupId}/addSmsAdmin")
	@ResponseBody
	public JsonResult addAdmin(@PathVariable("groupId") int groupId,
			@RequestParam("indexId") int indexId) {

		VmtIndex index = indexService.selectIndexById(indexId);
		if (index == null) {
			return new JsonResult("未找到人员,请重新检索");
		}
		SmsGroupMember sgm = smsService.getGroupMemberByGidAndUmtId(groupId,
				index.getUmtId());
		JsonResult js = new JsonResult();
		if(sgm == null) {
			sgm = new SmsGroupMember();
			sgm.setAdmin(true);
			sgm.setCstnetId(index.getUserCstnetId());
			sgm.setGroupId(groupId);
			sgm.setUmtId(index.getUmtId());
			sgm.setUserName(index.getUserName());
			smsService.addMember(sgm);
			js.setData(sgm);
			return js;
		}
		if (sgm.isAdmin()) {
			return new JsonResult("此成员已经为管理员,请勿重复添加");
		}
		SmsGroupMember m = new SmsGroupMember();
		m.setId(sgm.getId());
		m.setAdmin(true);
		smsService.update(sgm);
		sgm.setAdmin(true);
		js.setData(sgm);
		return js;
	}
	@RequestMapping("{groupId}/recharge")
	@ResponseBody
	public JsonResult recharge(@PathVariable("groupId")int gid,@RequestParam("plus")int plus,@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser vsu){
		SmsGroup g=smsService.getGroupById(gid);
		if(g==null){
			return new JsonResult("未找到对应账号");
		}
		if(g.getSmsCount()>10000000){
			return new JsonResult("短信数量充足,不允许充值");
		}
		smsService.recharge(g,plus,vsu.getUserInfo().getCstnetId());
		return new JsonResult();
	}
	@RequestMapping("{groupId}/getLog")
	@ResponseBody
	public JsonResult getLog(@PathVariable("groupId")int gid){
		JsonResult jr=new JsonResult();
		jr.setData(smsService.findChargeLogByGid(gid));
		return jr;
	}
	@RequestMapping("{groupId}/removeGroup")
	@ResponseBody
	public JsonResult removeGroup(@PathVariable("groupId")int gid){
		smsService.removeGroupById(gid);
		return new JsonResult();
	}
	
	

	@RequestMapping("addSmsGroup")
	@ResponseBody
	public JsonResult addSystemGroup(SmsGroup smsGroup) {
		if(!isAccountUnUsed(smsGroup.getAccount())){
			return new JsonResult("账号已存在");
		}
		smsService.createSmsGroup(smsGroup);
		return new JsonResult();
	}

	@RequestMapping("removeAdmin/{mId}")
	@ResponseBody
	public JsonResult removeAdmin(@PathVariable("mId") int mid,
			@RequestParam("gid") int gid) {
		smsService.removeMember(mid);
		return new JsonResult();
	}

	@RequestMapping("searchMember")
	@ResponseBody
	public List<VmtIndex> searchMember(@RequestParam("q") String keyword) {
		if (CommonUtils.isNull(keyword)) {
			return null;
		}
		return indexService.selectIndexByKeyword(CommonUtils.trim(keyword));
	}
	@RequestMapping("isAccountUsed")
	@ResponseBody
	public boolean isAccountUnUsed(@RequestParam("account")String account){
		return !smsService.isAccountUsed(CommonUtils.trim(account));
	}

}
