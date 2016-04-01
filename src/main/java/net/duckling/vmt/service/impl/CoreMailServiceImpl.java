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
package net.duckling.vmt.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttributes;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.coremail.CoreMailOrgUnit;
import net.duckling.vmt.domain.coremail.CoreMailUser;
import net.duckling.vmt.exception.CoreMailServiceException;
import net.duckling.vmt.service.ICoreMailService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tebie.applib.api.APIContext;
import tebie.applib.api.IClient;

/**
 * 针对CoreMailServiceImpl
 * 
 * @author lvly
 * @since 2013-5-29
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CoreMailServiceImpl implements ICoreMailService {
	private static final Logger LOG = Logger.getLogger(CoreMailServiceImpl.class);
	@Autowired
	private VmtConfig config;

	/**
	 * API声明说务必保证一个线程一个client，用完就关闭
	 * */
	private IClient getCoreMailClient() throws IOException {
		Socket socket = new Socket(config.getCoreMailIP(), config.getCoreMailPort());
		return APIContext.getClient(socket);
	}

	private void closeClient(IClient client) {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void updateUserPrivacyLevel(String cstnetId, String privacyLevel) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			client.changeAttrs(cstnetId, "privacy_level=" + privacyLevel);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void createUnit(CoreMailOrgUnit unit) {
		IClient client = null;
		APIContext context = null;
		try {
			client = getCoreMailClient();
			String attrs = "parent_org_unit_id=" + CommonUtils.killNull(unit.getParentId()) + "&org_unit_name="
					+ unit.getOuName();
			context = client.addUnit(unit.getOrgId(), unit.getOuId(), attrs);
			switch (context.getRetCode()) {
			case APIContext.RC_NORMAL:
				LOG.info("create unit:" + unit.getOuName() + "success");
				break;
			default:
				LOG.error("info:pid:" + unit.getParentId() + ",name:" + unit.getOuName() + ",id:" + unit.getOuId()
						+ "," + context);
				break;
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public CoreMailOrgUnit getUnit(String orgId, String ouId) {
		String departInfo = null;
		IClient client = null;
		try {
			client = getCoreMailClient();
			departInfo = client.getUnitAttrs(orgId, ouId, "parent_org_unit_id=&org_unit_name&=&org_unit_list_rank=")
					.getResult();
			if (departInfo == null) {
				return null;
			}
			CoreMailOrgUnit unit = new CoreMailOrgUnit();
			unit.setParentId(getParameter(departInfo, "parent_org_unit_id"));
			unit.setOrgId(orgId);
			unit.setOuName(getParameter(departInfo, "org_unit_name"));
			unit.setOuId(ouId);
			String listRank = getParameter(departInfo, "org_unit_list_rank");
			if (!CommonUtils.isNull(listRank)) {
				unit.setListRank(Integer.parseInt(listRank));
			}
			return unit;
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return null;
		} finally {
			closeClient(client);
		}
	}

	private String getParameter(String encoded, String key) {
		int start;
		if (encoded == null) {
			return null;
		}
		if (encoded.startsWith(key + '=')) {
			start = key.length() + 1;
		} else {
			int i = encoded.indexOf('&' + key + '=');
			if (i == -1) {
				return null;
			}
			start = i + key.length() + 2;
		}
		int end = encoded.indexOf('&', start);
		String value = (end == -1) ? encoded.substring(start) : encoded.substring(start, end);
		try {
			return URLDecoder.decode(value, KEY.GLOBAL_ENCODE);
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<CoreMailUser> getUsers(String orgId) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			String[] userAttributes = { "user_id", "user_list_rank", "domain_name", "org_unit_id", "privacy_level",
					"admin_type", "true_name", "user_expiry_date" };

			BasicAttributes attr = (BasicAttributes) (client.listUsersJNDI(orgId, null, true, true, userAttributes,
					null, null, 0, -1).getResultEx());
			Attribute u = attr.get("u");
			Set<String> depart = new HashSet<String>();
			List<CoreMailUser> users = new ArrayList<CoreMailUser>();
			for (int i = 0; i < u.size(); i++) {
				BasicAttributes userInfo = (BasicAttributes) (u.get(i));
				Attribute ouId = userInfo.get("org_unit_id");
				if (ouId != null && ouId.get() != null) {
					depart.add((String) ouId.get());
				}
				CoreMailUser user = buildUser(userInfo);
				if (user.isVisible()) {
					users.add(user);
				}
			}
			return users;
		} catch (IOException | NamingException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
		return null;
	}

	private CoreMailUser buildUser(BasicAttributes userInfo) {
		try {
			CoreMailUser user = new CoreMailUser();
			Attribute userId = userInfo.get("user_id");
			Attribute domain = userInfo.get("domain_name");
			Attribute ouId = userInfo.get("org_unit_id");
			Attribute adminType = userInfo.get("admin_type");
			Attribute privacyLevel = userInfo.get("privacy_level");
			Attribute listRank = userInfo.get("user_list_rank");
			Attribute userName = userInfo.get("true_name");
			Attribute expireTime = userInfo.get("user_expiry_date");
			user.setEmail(userId.get().toString() + "@" + domain.get().toString());
			if (ouId != null && ouId.get() != null) {
				user.setOuId(ouId.get().toString());
			}
			if (adminType != null && adminType.get() != null) {
				user.setAdmin("OA".equals(adminType.get().toString()));
			}
			if (privacyLevel != null && privacyLevel.get() != null) {
				user.setVisible("2".equals(privacyLevel.get().toString()));
			}
			if (listRank != null && listRank.get() != null) {
				user.setListRank(Integer.parseInt(listRank.get().toString()));
			}
			if (userName != null && userName.get() != null) {
				user.setName(userName.get().toString());
			}
			if (expireTime != null && expireTime.get() != null) {
				String s = expireTime.get().toString();
				user.setExpireTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date().parse(s)));
			}
			return user;
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String[] getOrgIdFromDomain(String domain) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			APIContext context = client.getOrgListByDomain(domain);
			return context.getResult() == null ? new String[] {} : context.getResult().split(",");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
		return new String[] {};
	}

	@Override
	public String getOrgName(String orgId) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			APIContext context = client.getOrgInfo(orgId, "org_name=");
			return getParameter(context.getResult(), "org_name");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
		return null;
	}

	@Override
	public String[] getAllOrgId() {
		IClient client = null;
		try {
			client = getCoreMailClient();
			APIContext context = client.getOrgList();
			return context.getResult().split(",");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
		return null;
	}

	@Override
	public boolean isUserExists(String cstnetId) {
		IClient client = null;
		APIContext context = null;
		try {
			client = getCoreMailClient();
			context = client.userExist(cstnetId);
			switch (context.getRetCode()) {
			case APIContext.RC_NORMAL:
				return true;
			default:
				return false;
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
		throw new CoreMailServiceException("why can't find user:result=" + context);
	}

	@Override
	public void moveUnit(String orgId, String unitId, String punitId) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			judice("moveUnit",
					client.setUnitAttrs(orgId, unitId, "parent_org_unit_id=" + CommonUtils.killNull(punitId)));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
	}

	public boolean judice(String method, APIContext context) {
		int code = context.getRetCode();
		if (code != APIContext.RC_NORMAL) {
			LOG.error(print(context, method));
			return false;
		}
		return true;
	}

	@Override
	public void updateUnitName(String orgId, String ouId, String newName) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			judice("updateUnitName", client.setUnitAttrs(orgId, ouId, "org_unit_name=" + newName));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
	}

	public String print(APIContext context, String oper) {
		StringBuffer sb = new StringBuffer();
		sb.append("\nThe Result of Operation[" + oper + "]:");
		sb.append("{");
		sb.append("\tError Info:" + context.getErrorInfo());
		sb.append("\tResult:" + context.getResult());
		sb.append("\tReturn Code:" + context.getRetCode());
		sb.append("\tResult Ex:" + context.getResultEx());
		sb.append("}\n\n");
		return sb.toString();
	}

	@Override
	public void moveUser(String cstnetId, String pouId) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			judice("moveUser", client.changeAttrs(cstnetId, "org_unit_id=" + CommonUtils.killNull(pouId)));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}

	}

	@Override
	public void updateAccount(String email, String status, String expireTime) {
		if(CommonUtils.isNull(status)&&CommonUtils.isNull(expireTime)){
			return;
		}
		String attr="";
		if(!CommonUtils.isNull(status)){
			String coreMailStatus = CoreMailUser.getCoreMailStatus(status);
			attr+= "user_status=" + CommonUtils.killNull(coreMailStatus);
		}
		//FIX BUG:用户过期时间置空后，邮箱系统依旧在时间到期后锁定了邮箱
		attr += (CommonUtils.isNull(attr)?"":"&")+"user_expiry_date=" + expireTime;
		IClient client = null;
		try {
			client = getCoreMailClient();
			judice("changeCoremailAccount:status", client.changeAttrs(email, attr));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
	}

	@Override
	public void deleteUnit(String orgId, String ouId) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			judice("moveUser", client.delUnit(orgId, ouId));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
	}

	@Override
	public void deleteUser(String cstnetId) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			judice("removeUser", client.deleteUser(cstnetId));
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
	}

	@Override
	public String[] getDomainListFromOrgId(String orgId) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			APIContext context = getCoreMailClient().getOrgInfo(orgId, "domain_name");
			if (!judice("getDomainListFromOrgId", context)) {
				return null;
			}
			String domainList = context.getResult();
			return getParameter(domainList, "domain_name").split(",");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
		return null;
	}

	@Override
	public void createUser(CoreMailUser user) {
		IClient client = null;
		try {
			client = getCoreMailClient();
			String param = "cos_id=" + config.getCoreMailCreateUserCos() + "&user_status="+user.getStatus() + "&quota_delta=0"
					+ "&password=" + LdapUtils.encode(user.getPassword()) + "&true_name=" + user.getName() + "&domain_name="
					+ user.getDomain() + "&org_unit_id=" + CommonUtils.killNull(user.getOuId()) + "&user_expiry_date="
					+ CommonUtils.killNull(user.getExpireTime());

			APIContext context = getCoreMailClient().createUser(config.getCoreMailProviderId(), user.getOrgId(),
					user.getEmail(), param);
			judice("createUser", context);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			closeClient(client);
		}
	}

	@Override
	public boolean changePassword(String cstnetId, String password) {
		IClient client = null;
		APIContext context = null;
		try {
			client = getCoreMailClient();
			context = client.changeAttrs(cstnetId, "password=" + URLEncoder.encode(password, "UTF-8"));
			return judice("changePassword", context);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return false;
		} finally {
			closeClient(client);
		}
	}
}
