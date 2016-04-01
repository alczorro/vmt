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
package net.duckling.vmt.domain.ldap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.ldap.LdapMapping;
import net.duckling.vmt.common.ldap.LdapObjectClass;
import net.duckling.vmt.common.ldap.LdapType;
import net.duckling.vmt.common.util.HashCodeGenerator;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtApp;

/**
 * 组织结构的Bean
 * @author lvly
 * @since 2013-5-2
 */
/**
 * @author lvly
 * @since 2013-6-21
 */
@LdapMapping(objectClass={LdapObjectClass.CLASS_VMT_ORG,LdapObjectClass.CLASS_VMT_SETTING,LdapObjectClass.CLASS_VMT_NODE})
public class LdapOrg implements Comparable<LdapOrg>{
	/**
	 * 公开
	 */
	public static final String PRIVILEGE_ALL_OPEN="allOpen";
	/**
	 * 公开需审核
	 */
	public static final String PRIVILEGE_OPEN_REQUIRED="openRequired";
	
	/**
	 *允许普通成员添加成员 
	 */
	public static final String PRIVILEGE_PRIVATE_ALLOW_ADD="privateAllowAdd";
	/**
	 *不允许普通成员添加成员 
	 */
	public static final String PRIVILEGE_PRIVATE_NOT_ALLOW_ADD="privateNotAllow";
	/**
	 * 组织结构的dn
	 */
	@LdapMapping(type = LdapType.DN)
	private String dn;
	/**
	 * 当前显示路径，与组织名称相同
	 */
	@LdapMapping("vmt-current-display")
	private String currentDisplay;
	/**
	 * 组织结构标识
	 */
	@LdapMapping(value="vmt-symbol",type=LdapType.RDN)
	private String symbol;
	/**
	 * 组织结构名称
	 */
	@LdapMapping("vmt-name")
	private String name;
	/**
	 * 创建者，umtId
	 */
	@LdapMapping("vmt-creator")
	private String creator;
	/**
	 * 管理员，umtId[]
	 */
	@LdapMapping("vmt-admin")
	private String[] admins;
	/**
	 * 成员数量，全部
	 */
	@LdapMapping("vmt-count")
	private int count;
	/**
	 * 密码，系统自动生成
	 */
	@LdapMapping(type=LdapType.PASSWORD)
	private String password;
	/**
	 * 权限
	 */
	@LdapMapping("vmt-privilege")
	private String privilege=PRIVILEGE_PRIVATE_NOT_ALLOW_ADD;
	/**
	 * 是否成员可见
	 */
	@LdapMapping("vmt-member-visible")
	private boolean memberVisible=true;
	/**
	 * 是否可见未激活的用户
	 */
	@LdapMapping("vmt-unconfirm-visible")
	private boolean unconfirmVisible=false;
	/**
	 * 来源
	 */
	@LdapMapping(value=KEY.DB_VMT_FROM)
	private String from;
	
	
	@LdapMapping(value="vmt-logo")
	private int logoId;
	
	@LdapMapping(value="vmt-mobile-logo")
	private int mobileLogo;
	
	@LdapMapping(value="vmt-pc-logo")
	private int pcLogo;
	
	/**
	 * 升级
	 * */
	@LdapMapping(value="vmt-from-date")
	private long fromDate;
	@LdapMapping(value="vmt-to-date")
	private long toDate;
	
	/**域名映射*/
	@LdapMapping(value="vmt-domain")
	private String[] domains;
	
	@LdapMapping(value="vmt-is-coremail")
	private boolean isCoreMail;
	@LdapMapping(value="vmt-is-cas")
	private boolean isCas;
	@LdapMapping(value="vmt-org-type")
	private String type;
	/**
	 * 用户的提交申请状态，是否开通科信
	 * */
	@LdapMapping(value="vmt-apply-open-dchat")
	private String applyOpenDchat;

	public static final String APPLY_OPEN_DCHAT_APPLY="applyDchat";
	public static final String APPLY_OPEN_DCHAT_ACCEPT="applyAccept";
	public static final String APPLY_OPEN_DCHAT_REFUSE="applyRefuse";
	/**
	 * 是否已经开通科信
	 * */
	@LdapMapping(value="vmt-open-dchat")
	private boolean openDchat;
	
	/**冗余字段,用来存储app的信息,json*/
	@LdapMapping(value="vmt-apps-copy")
	private String vmtAppCopy;
	@LdapMapping(value="vmt-is-apps-open")
	private boolean isAppsOpen;
	/**隐藏手机号字段*/
	@LdapMapping("vmt-hide-mobile")
	private boolean hideMobile;
	
	@LdapMapping("vmt-description")
	private String description;
	
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isHideMobile() {
		return hideMobile;
	}

	public void setHideMobile(boolean hideMobile) {
		this.hideMobile = hideMobile;
	}

	public boolean isAppsOpen() {
		return isAppsOpen;
	}

	public void setAppsOpen(boolean isAppsOpen) {
		this.isAppsOpen = isAppsOpen;
	}

	public String getVmtAppCopy() {
		return vmtAppCopy;
	}

	public void setVmtAppCopy(String vmtAppCopy) {
		this.vmtAppCopy = vmtAppCopy;
	}
	public List<VmtApp> getApps(){
		if(CommonUtils.isNull(vmtAppCopy)){
			return null;
		}
		return VmtApp.convert(vmtAppCopy);
	}

	public String getApplyOpenDchat() {
		return applyOpenDchat;
	}

	public void setApplyOpenDchat(String applyOpenDchat) {
		this.applyOpenDchat = applyOpenDchat;
	}

	public boolean isOpenDchat() {
		return openDchat;
	}

	public void setOpenDchat(boolean openDchat) {
		this.openDchat = openDchat;
	}

	public String[] getDomains() {
		return domains;
	}

	public void setDomains(String[] domains) {
		this.domains = domains;
	}

	public boolean getIsCoreMail() {
		return isCoreMail;
	}

	public void setCoreMail(boolean isCoreMail) {
		this.isCoreMail = isCoreMail;
	}

	public boolean getIsCas() {
		return isCas;
	}
	public boolean isMainDomain(String domain){
		if(CommonUtils.isNull(getDomains())){
			return false;
		}
		if(!getIsCoreMail()){
			return false;
		}
		return getDomains()[0].equals(CommonUtils.trim(domain));
	}

	public void setCas(boolean isCas) {
		this.isCas = isCas;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getFromDate() {
		return fromDate;
	}

	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}

	public long getToDate() {
		return toDate;
	}

	public void setToDate(long toDate) {
		this.toDate = toDate;
	}

	public int getLogoId() {
		return logoId;
	}

	public void setLogoId(int clbId) {
		this.logoId = clbId;
	}
	

	/**
	 * 当前用户是否是管理员
	 * @param umtId
	 * @return
	 */
	public boolean isAdmin(String umtId){
		if(CommonUtils.isNull(admins)){
			return false;
		}
		return CommonUtils.isEqualsContain(admins, umtId);
	}
	
	/**
	 * 当前用户是否能看见当前组织
	 * @param umtId
	 * @return
	 */
	public boolean canLook(String umtId){
		return memberVisible||isAdmin(umtId);
	}
	/**
	 * 当前用户是否能看见未激活的用户
	 * @param umtId
	 * @return
	 */
	public boolean canSeeUnConfirm(String umtId){
		return unconfirmVisible||isAdmin(umtId);
	}
	/**当前用户是否有权限添加成员
	 * @param umtId
	 * @return
	 */
	public boolean canAdd(String umtId){
		return PRIVILEGE_PRIVATE_ALLOW_ADD.equals(privilege)||isAdmin(umtId);
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	public boolean isMemberVisible() {
		return memberVisible;
	}
	public void setMemberVisible(boolean memberVisible) {
		this.memberVisible = memberVisible;
	}
	public boolean isUnconfirmVisible() {
		return unconfirmVisible;
	}
	public void setUnconfirmVisible(boolean unconfirmVisible) {
		this.unconfirmVisible = unconfirmVisible;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String oid;
	
	/**
	 * @return
	 */
	public String getOid() {
		this.oid=HashCodeGenerator.getCode(this.dn);
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return
	 */
	public String[] getAdmins() {
		return admins==null?null:admins.clone();
	}
	public void setAdmins(String[] admins) {
		this.admins = admins==null?null:admins.clone();
	}
	public String getCurrentDisplay() {
		return currentDisplay;
	}

	public void setCurrentDisplay(String currentDisplay) {
		this.currentDisplay = currentDisplay;
	}

	public String getSymbol() {
		return symbol;
	}
	@Override
	public int compareTo(LdapOrg o) {
		if(o==null){
			return -1;
		}
		return o.count-this.count;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	public int getMobileLogo() {
		return mobileLogo;
	}

	public void setMobileLogo(int mobileLogo) {
		this.mobileLogo = mobileLogo;
	}

	public int getPcLogo() {
		return pcLogo;
	}

	public void setPcLogo(int pcLogo) {
		this.pcLogo = pcLogo;
	}
	
	public static String[] extractOrgId(List<LdapOrg> org){
		if(CommonUtils.isNull(org)){
			return new String[]{};
		}
		String[] dns=new String[org.size()];
		int index=0;
		for(LdapOrg o:org){
			dns[index++]=o.getDn();
		}
		return dns;
	}
	
	public static List<LdapOrg> extractAdminGroup(List<LdapOrg> orgs,String umtId){
		if(CommonUtils.isNull(orgs)){
			return new ArrayList<LdapOrg>();
		}
		for(Iterator<LdapOrg> it=orgs.iterator();it.hasNext();){
			LdapOrg org=it.next();
			if(!org.isAdmin(umtId)){
				it.remove();
			}
		}
		return orgs;
	}

	public static List<String> extractAppOpenOrgId(List<LdapOrg> orgs) {
		List<String> dns=new ArrayList<String>();
		if(CommonUtils.isNull(orgs)){
			return dns;
		}
		for(LdapOrg o:orgs){
			if(o.isAppsOpen){
				dns.add(o.getDn());
			}
		}
		return dns;
	}
}
