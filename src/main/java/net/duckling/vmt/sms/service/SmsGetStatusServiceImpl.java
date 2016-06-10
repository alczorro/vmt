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

import java.util.Iterator;

import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.domain.HttpPost;
import net.duckling.vmt.sms.ISmsGetStatusService;
import net.duckling.vmt.sms.dao.ISmsGetterDAO;
import net.duckling.vmt.sms.domain.SmsGetter;
import net.duckling.vmt.sms.domain.SmsStatusRes;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsGetStatusServiceImpl implements ISmsGetStatusService {
    private static final Logger LOG = Logger
	.getLogger(SmsGetStatusServiceImpl.class);
    @Autowired
    private VmtConfig config;
    @Autowired
    private ISmsGetterDAO getterDAO;

    @Override
    public void pullSmsStatus() {
	String baseUrl = config.getSmsBaseUrl();
	if ("null".equalsIgnoreCase(baseUrl)) {
	    LOG.error("Sms not set and will be skipped.");
	    return;
	}

	HttpPost post = new HttpPost(baseUrl + "/getSmsState");
	post.add("account", config.getSmsAccount());
	post.add("password", DigestUtils.md5Hex(config.getSmsPassword()));
	String xml = post.post();
	SmsStatusRes res = parseXml(xml);

	if ("r:000".equals(res.getSubStat())) {
	    if (res.getGetter().size() > 0) {
		LOG.error("success read msg:" + xml);
		for (SmsGetter g : res.getGetter()) {
		    if ("-1".equals(g.getSmsUuid())) {
			getterDAO.updateResStatByMobile(g.getMobile(),
							g.getStat());
		    } else {
			getterDAO.updateResStat(g);
		    }
		}
	    }
	} else if (!"p:010".equals(res.getSubStat())) {
	    LOG.error(xml);
	}
    }

    @SuppressWarnings("unchecked")
    public SmsStatusRes parseXml(String xml) {
	Document doc = null;
	try {
	    SmsStatusRes sms = new SmsStatusRes();
	    doc = DocumentHelper.parseText(xml);
	    Element rootElt = doc.getRootElement(); // 获取根节点
	    sms.setSubStat(rootElt.elementTextTrim("subStat"));
	    for (Iterator<Element> iter = rootElt.elementIterator("resDetail"); iter
		     .hasNext();) {
		Element ele = iter.next();
		SmsGetter getter = new SmsGetter();
		getter.setMobile(ele.element("phoneNumber").getText());
		String stat = ele.element("stat").getText();
		if ("r:000".equals(stat)) {
		    stat = "r:xxx";
		}
		getter.setStat(stat);
		getter.setSmsUuid(ele.elementTextTrim("smsId"));
		sms.addGetter(getter);
	    }
	    return sms;

	} catch (DocumentException e) {
	    LOG.error(e.getMessage(), e);
	}
	return null;
    }

}
