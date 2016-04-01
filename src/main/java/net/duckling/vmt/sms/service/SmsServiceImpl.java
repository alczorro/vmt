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
package net.duckling.vmt.sms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.util.RenderUtils;
import net.duckling.vmt.domain.HttpPost;
import net.duckling.vmt.domain.SimpleEmail;
import net.duckling.vmt.service.IMailService;
import net.duckling.vmt.sms.ISmsService;
import net.duckling.vmt.sms.dao.ISmsDAO;
import net.duckling.vmt.sms.dao.ISmsGetterDAO;
import net.duckling.vmt.sms.dao.ISmsGroupDAO;
import net.duckling.vmt.sms.dao.ISmsGroupMemberDAO;
import net.duckling.vmt.sms.dao.ISmsRechargeLogDAO;
import net.duckling.vmt.sms.dao.ISmsSendLogDAO;
import net.duckling.vmt.sms.domain.Sms;
import net.duckling.vmt.sms.domain.SmsGetter;
import net.duckling.vmt.sms.domain.SmsGroup;
import net.duckling.vmt.sms.domain.SmsGroupMember;
import net.duckling.vmt.sms.domain.SmsRechargeLog;
import net.duckling.vmt.sms.domain.SmsSendLog;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements ISmsService {

	private static final Logger LOG = Logger.getLogger(SmsServiceImpl.class);
	@Autowired
	private VmtConfig vmtConfig;
	@Autowired
	private ISmsGroupDAO smsDAO;
	@Autowired
	private ISmsGroupMemberDAO smsMDAO;
	@Autowired
	private ISmsRechargeLogDAO chargeDAO;
	@Autowired
	private ISmsDAO smsBDAO;
	@Autowired
	private ISmsGetterDAO getterDAO;
	@Autowired
	private ISmsSendLogDAO logDAO;
	@Autowired
	private IMailService mailService;

	@Override
	public void ifLessThan20PercentSendEmail(SmsGroup smsGroup) {
		if (smsGroup.getSmsUsed() * 1.0 / smsGroup.getSmsCount() > 0.8) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("${groupName}", smsGroup.getGroupName());
			map.put("${used}", smsGroup.getSmsUsed() + "");
			map.put("${count}", smsGroup.getSmsCount() + "");
			map.put("${last}", (smsGroup.getSmsCount() - smsGroup.getSmsUsed())
					+ "");
			map.put("${baseUrl}", vmtConfig.getMyBaseUrl());
			String[] superAdmin = vmtConfig.getSuperAdmin();
			String content = RenderUtils.getHTML(map, RenderUtils.SMS_USE_20P);
			SimpleEmail se = new SimpleEmail(superAdmin, content, "短信群组["
					+ smsGroup.getAccount() + "]可用短信数量低于20%");
			mailService.sendEmail(se);
		}
	}

	@Override
	public SmsGroup getGroupById(int gid) {
		return smsDAO.getGroupById(gid);
	}

	@Override
	public void changeMemberRole(int memberId, boolean isAdmin) {
		smsMDAO.changeMemberRole(memberId, isAdmin);
	}

	@Override
	public void sendSms(Sms sms) {
		HttpPost post = new HttpPost(vmtConfig.getSmsSendUrl());
		post.add("account", vmtConfig.getSmsAccount());
		post.add("password", DigestUtils.md5Hex(vmtConfig.getSmsPassword()));
		post.add("smsType", "02");
		post.add("message", sms.getSmsContent());
		String str = post.post();
		getSmsResultFromXml(str, sms, false);
	}

	@Override
	public void resendSms(Sms sms) {
		HttpPost post = new HttpPost(vmtConfig.getSmsSendUrl());
		post.add("account", vmtConfig.getSmsAccount());
		post.add("password", DigestUtils.md5Hex(vmtConfig.getSmsPassword()));
		post.add("smsType", "02");
		post.add("message", sms.getSmsContent());
		String str = post.post();
		getSmsResultFromXml(str, sms, true);
	}

	@SuppressWarnings("unchecked")
	private void getSmsResultFromXml(String xml, Sms sms, boolean resend) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml);
			int index = 0;
			Element rootElt = doc.getRootElement(); // 获取根节点
			String uuid = rootElt.elementTextTrim("smsId");
			sms.setSubStatus(rootElt.elementTextTrim("subStat"));
			sms.setSmsUuid(uuid);
			if (!resend) {
				sms.setId(smsBDAO.insert(sms));
			}
			int successCount = 0;
			for (Iterator<Element> iter = rootElt.elementIterator("resDetail"); iter
					.hasNext(); index++) {
				Element ele = iter.next();
				SmsGetter getter = sms.getGetter().get(index);
				getter.setMobile(ele.element("phoneNumber").getText());
				getter.setStat(ele.element("stat").getText());
				getter.setSmsId(sms.getId());
				getter.setSmsUuid(uuid);
				if (resend) {
					getterDAO.updateResStat(getter);
				}
				if ("r:000".equals(getter.getStat())) {
					successCount++;
				}
			}
			smsDAO.plusSmsUsedByID(sms.getGroupId(), successCount);
			SmsSendLog log = new SmsSendLog();
			log.setSendCount(successCount);
			log.setSenderCstnetId(sms.getSenderCstnetId());
			log.setSenderName(sms.getSenderName());
			log.setUmtId(sms.getSenderUmtId());
			log.setSmsId(sms.getId());
			log.setGroupId(sms.getGroupId());
			logDAO.insert(log);
			if (!resend) {
				getterDAO.insert(sms.getGetter());
			}

		} catch (DocumentException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public List<SmsGroup> getAllSmsGroup() {
		List<SmsGroup> gs = smsDAO.findAll();
		fillAdmin(gs);
		return gs;
	}

	private void fillAdmin(List<SmsGroup> group) {
		if (CommonUtils.isNull(group)) {
			return;
		}
		List<Integer> groupIds = new ArrayList<Integer>();
		Map<Integer, SmsGroup> groups = new HashMap<Integer, SmsGroup>();
		for (SmsGroup g : group) {
			groupIds.add(g.getId());
			groups.put(g.getId(), g);
		}
		List<SmsGroupMember> members = smsMDAO.getMemberByGroupId(groupIds);
		for (SmsGroupMember m : members) {
			groups.get(m.getGroupId()).addMember(m);
		}
	}

	@Override
	public boolean createSmsGroup(SmsGroup smsGroup) {
		smsGroup.setId(smsDAO.create(smsGroup));
		return smsGroup.getId() > 0;
	}

	@Override
	public int getAdminCount(int groupId) {
		return smsMDAO.getAdminCount(groupId);
	}

	@Override
	public void removeMember(int mid) {
		smsMDAO.removeMember(mid);
	}

	@Override
	public void addMember(SmsGroupMember m) {
		m.setId(smsMDAO.addMember(m));
	}

	@Override
	public SmsGroupMember getGroupMemberByGidAndUmtId(int groupId, String umtId) {
		return smsMDAO.getGroupMemberByGidAndUmtId(groupId, umtId);
	}

	@Override
	public void update(SmsGroupMember m) {
		smsMDAO.update(m);
	}

	@Override
	public void recharge(SmsGroup g, int plus, String email) {
		SmsGroup gupdate = new SmsGroup();
		gupdate.setId(g.getId());
		gupdate.setSmsCount(g.getSmsCount() + plus);
		smsDAO.update(gupdate);
		// recharge log
		SmsRechargeLog log = new SmsRechargeLog();
		log.setGroupId(g.getId());
		log.setLast(gupdate.getSmsCount());
		log.setPlus(plus);
		log.setWhoCharged(email);
		chargeDAO.addLog(log);

	}

	@Override
	public boolean isAccountUsed(String trim) {
		return smsDAO.isAccountUsed(trim);
	}

	@Override
	public List<SmsRechargeLog> findChargeLogByGid(int gid) {
		return chargeDAO.findLogsByGid(gid);
	}

	@Override
	public void removeGroupById(int gid) {
		smsDAO.removeGropyById(gid);
	}

	@Override
	public SmsGroup getLastGroup(String umtId) {
		SmsGroupMember sgm = CommonUtils.first(smsMDAO
				.getGroupMemberByUmtId(umtId));
		if (sgm == null) {
			return null;
		} else {
			SmsGroup g = smsDAO.getGroupById(sgm.getGroupId());
			return g;
		}
	}

	@Override
	public List<Sms> getSmsByGroupId(int groupId) {
		Sms s = new Sms();
		s.setGroupId(groupId);
		return smsBDAO.getSms(s);
	}

	@Override
	public List<Sms> getSmsByGroupIdAndUmtId(int groupId, String umtId) {
		Sms s = new Sms();
		s.setGroupId(groupId);
		s.setSenderUmtId(umtId);
		return smsBDAO.getSms(s);
	}

	@Override
	public Sms getSmsById(int smsId) {
		Sms s = new Sms();
		s.setId(smsId);
		return CommonUtils.first(smsBDAO.getSms(s));
	}

	@Override
	public Sms getSmsByIdAndUmtId(int smsId, String umtId) {
		Sms s = new Sms();
		s.setId(smsId);
		s.setSenderUmtId(umtId);
		return CommonUtils.first(smsBDAO.getSms(s));
	}

	@Override
	public List<SmsGetter> getSmsGetterBySmsId(int smsid) {
		SmsGetter g = new SmsGetter();
		g.setSmsId(smsid);
		return getterDAO.getSmsGetter(g);
	}

	@Override
	public SmsGetter getSmsGetterBySmsGetterId(int smsLogId) {
		SmsGetter g = new SmsGetter();
		g.setId(smsLogId);
		return CommonUtils.first(getterDAO.getSmsGetter(g));
	}

	@Override
	public List<SmsGetter> getSmsGetterByFail(int smsid) {
		SmsGetter g = new SmsGetter();
		g.setSmsId(smsid);
		g.setStat("r:004");
		return getterDAO.getSmsGetter(g);
	}

	@Override
	public List<SmsGroupMember> getGroupMemberByGroupId(int groupId) {
		return smsMDAO.getgroupMemberByGroupId(groupId);
	}

	@Override
	public List<SmsSendLog> getLogGroupByuserName(int gid) {
		return logDAO.getLogGroupByuserName(gid, null);
	}

	@Override
	public SmsGroupMember getGroupMemberByGidAndId(int gid, int memberId) {
		return smsMDAO.getgroupMemberByGidAndId(gid, memberId);
	}

	@Override
	public List<SmsSendLog> getLogGroupByuserName(int gid, String umtId) {
		return logDAO.getLogGroupByuserName(gid, umtId);
	}

}
