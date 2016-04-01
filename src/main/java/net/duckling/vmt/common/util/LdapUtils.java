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
package net.duckling.vmt.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.service.IDepartmentService;

import org.apache.log4j.Logger;
import org.springframework.ldap.core.DistinguishedName;

/**
 *针对Ldap的一些列工具操作，包括dn等
 * @author lvly
 * @since 2013-5-3
 */
public final class LdapUtils {  
	private static final Logger LOGGER = Logger.getLogger(LdapUtils.class);
	private static final int MAX_DEEP=5;
	private LdapUtils(){}
	
	/**
	 * @param name
	 * @param deep
	 * @return
	 */
	public static String getDN(Name name, int deep) {
		return name.getPrefix(deep).toString();
	}
	/**
	 * @param dn
	 * @param deep
	 * @return
	 */
	public static String getDN(String dn, int deep) {
		if(dn==null){
			return null;
		}
		return getDN(new DistinguishedName(dn), deep);
	}
	/**
	 * @param dn
	 * @return
	 */
	public static String getParent(String dn){
		return less(dn, 1);
	}
	
	public static String minus(String dn,int i){
		DistinguishedName dN=new DistinguishedName(dn);
		return dN.getPrefix(dN.size()-i).toString();
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static String removeDangerous(String str){
		if(CommonUtils.isNull(str)){
			return str;
		}
		return CommonUtils.trim(str.replaceAll("[~/!@#\\\\$%^<>&*,+=()'\"\\s]", ""));
	}
	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public static String updateNameValue(String name,String value){
		String[] keyvalue=name.split("=");
		return keyvalue[0]+"="+value;
	}
	/**
	 * 
	 * @param dn
	 * @param dep
	 * @return
	 */
	public static String less(String dn,int dep){
		DistinguishedName dN=new DistinguishedName(dn);
		return dN.getPrefix(dN.size()-dep).toString();
	}
	/**
	 * @param dn
	 * @return
	 */
	public static String getLastValue(String dn){
		DistinguishedName name=new DistinguishedName(dn);
		return getValue(name.get(name.size()-1));
	}
	/**
	 * @param dn
	 * @return
	 */
	public static String getLast(String dn){
		DistinguishedName name=new DistinguishedName(dn);
		return name.getSuffix(name.size()-1).toString();
	}

	/**
	 * @param dn
	 * @return
	 */
	public static String getValue(String dn) {
		return dn.split("=")[1].trim();
	}

	/**
	 * 把attribute的数组属性，读出来，当做String数组
	 * @param attribute
	 * @return
	 * @throws NamingException
	 */
	public static String[] attr2StringArray(Attribute attribute) throws NamingException {
		if (attribute == null) {
			return new String[]{};
		}
		if (attribute.size() < 2) {
			return new String[] { attribute.get().toString() };
		} else {
			NamingEnumeration<?> enums = attribute.getAll();
			String[] str = new String[attribute.size()];
			int index = 0;
			while (enums.hasMore()) {
				str[index++] = enums.next().toString();
			}
			return str;
		}
	}

	/**
	 * @param dns
	 * @return
	 */
	public static String[] decode(String[] dns) {
		String decodeScope[] = new String[dns.length];
		for (int i = 0; i < dns.length; i++) {
			decodeScope[i] = decode(dns[i]);
		}
		return decodeScope;

	}
	/**
	 * @param dn
	 * @return
	 */
	public static String encode(String dn){
		if(CommonUtils.isNull(dn)){
			return dn;
		}
		try {
			return URLEncoder.encode(dn, KEY.GLOBAL_ENCODE);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
			return "";
		}
	}

	/**
	 * @param dn
	 * @return
	 */
	public static String decode(String dn) {
		try {
			return deepDecode(dn, 0);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
			return dn;
		}
	}

	/**
	 * 深度迭代，迭代出","为止，超过五次就不做了，因为有死循环的可能
	 * 
	 * @param dn
	 *            要decode的dn
	 * @param deep
	 *            深度
	 * @return decode后dn
	 * */
	private static String deepDecode(String dn, int deep) throws UnsupportedEncodingException {
		if(dn==null){
			return null;
		}
		if (deep > MAX_DEEP) {
			return dn;
		} else {
			String result = URLDecoder.decode(dn, KEY.GLOBAL_ENCODE);
			if (!result.contains(",")) {
				return deepDecode(result, deep+1);
			}
			return result;
		}
	}
	/**获得第几个dn
	 * @param node
	 * @param i
	 * @return
	 */
	public static String getNodeName(String node,int i){
		return node.split(",")[i];
	}
	/**
	 * @param dn
	 * @return
	 */
	public static boolean isGroupSub(String dn){
		return dn.contains(",ou=group");
	}
	/**
	 * @param dn
	 * @return
	 */
	public static boolean isOrgSub(String dn){
		return dn.contains(",ou=org");
	}
	
	public static boolean isOrgDN(String dn){
		return new DistinguishedName(dn).size()==2&&isOrgSub(dn);
	}
	public static boolean isGroupDN(String dn){
		return new DistinguishedName(dn).size()==2&&isGroupSub(dn);
	}
	public static boolean isUserDN(String dn){
		return new DistinguishedName(dn).size()>2&&dn.startsWith(KEY.DB_UMTID);
	}
	public static boolean isDeptDN(String dn){
		return new DistinguishedName(dn).size()>2&&dn.startsWith(KEY.DB_VMT_SYMBOL);
	}
	public static boolean isTeamDN(String dn){
		return isOrgDN(dn)||isGroupDN(dn);
	}
	
	/**
	 * 获得dn的前两个，路径，一般为组织或机构的rootdn
	 * @param dn
	 * @return
	 */
	public static String getTeamDN(String dn){
		return getDN(dn, 2);
	}
	public static String getDeletedDeptDN(String baseDN){
		return KEY.DB_VMT_SYMBOL+"="+IDepartmentService.NEED_DELETE_DEPART+","+baseDN;
	}
	

}
