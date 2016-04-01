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
package net.duckling.vmt.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.adapter.LdapUser2SearchLetterDataAdapter;
import net.duckling.vmt.common.adapter.UmtUser2LdapUserAdapter;
import net.duckling.vmt.common.adapter.UmtUser2UmtUserDataAdapter;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.domain.view.SearchLetterData;
import net.duckling.vmt.domain.view.UmtUserData;
import net.duckling.vmt.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.vlabs.duckling.api.umt.rmi.userv7.SearchField;
import cn.vlabs.duckling.api.umt.rmi.userv7.SearchScope;
import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;

/**
 * @author lvly
 * @since 2013-5-2
 */
@Controller
@RequestMapping("/user")
@SuppressWarnings("unchecked")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
public class SearchAllController {
	private static final int DEFAULT_OFFSET=0;
	private static final int DEFAULT_SIZE=-1;
	private static final int DEFAULT_SIZE_100=100;
	@Autowired
	private IUserService userService;
	@Autowired
	private UserService umtService;
	
	private static final int LIMIT_MAX=500;
	private static final String ALL="all";
	private static final String SCOPE="scope";
	/**
	 * 根据查询视图查询
	 * @param letter 字母
	 * @param baseDN 定位到的组织
	 * @param scope 用户的所属组织
	 * @param count 要查询的实体记录数是多少
	 * */
	@RequestMapping("view/{letter}")
	@ResponseBody
	@SecurityMapping(dnParam=SCOPE,level=SecurityLevel.VIEW,expect=ALL)
	public List<SearchLetterData> search(
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user,
			@PathVariable("letter")String letter,
			@RequestParam(SCOPE)String scope,
			@RequestParam("count") int count){
		if(CommonUtils.isNull(scope)){
			return null;
		}
		String[] searchScope=new String[]{LdapUtils.decode(scope)};
		if(ALL.equals(scope)){
			searchScope=userService.getMyAccessableTeam(user.getUserInfo().getUmtId());
		}
		if(count<LIMIT_MAX){
			return new LdapUser2SearchLetterDataAdapter(userService.searchUsersByAll(searchScope,user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId())).getData();
		}else{
			return new LdapUser2SearchLetterDataAdapter(userService.searchUsersByLetter(letter, searchScope,user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId())).getData(letter);
		}
	}
	
	/**
	 * 在ldap数据库里搜索
	 * @param keyword关键字
	 * @param user 登录用户信息
	 * */
	@ResponseBody
	@RequestMapping("/search/local")
	@SecurityMapping(dnParam=SCOPE,level=SecurityLevel.VIEW,expect=ALL)
	public List<LdapUser> search(
			@ModelAttribute(KEY.SESSION_LOGIN_USER) VmtSessionUser user,
			@RequestParam("keyword")String keyword,
			@RequestParam(SCOPE)String scope) {
		keyword=CommonUtils.killNull(keyword);
		String[] searchScope=new String[]{LdapUtils.decode(scope)};
		if(ALL.equals(scope)){
			searchScope=userService.getMyAccessableTeam(user.getUserInfo().getUmtId());
		}
		return userService.searchUsersByKeyword(keyword, searchScope,user.getIsSuperAdmin()?null:user.getUserInfo().getUmtId());
	}
	
	/**
	 * 搜索用户，现在的逻辑是如果域名匹配，则只差域名，且无条数限制
	 * @param q 关键字
	 * @param limit 限制多少条
	 * @param user 已登录用户
	 * */
	@ResponseBody
	@RequestMapping("/search/coreMail")
	public Collection<UmtUserData> searchCoreMail(
			@RequestParam("q") String keyword,
			@RequestParam("limit") int limit,
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(CommonUtils.isNull(keyword)){
			return null;
		}
		if(CommonUtils.isNull(user.getCoreMailDomain())){
			return null;
		}
		if(keyword.contains("@")){
			keyword=keyword.substring(0,keyword.lastIndexOf('@'));
		}
		
		if(user.getCoreMailDomain().equals(keyword)){
			return UmtUser2UmtUserDataAdapter.convert(umtService.searchUserByKeyword(keyword,user.getCoreMailDomain(), SearchScope.CORE_MAIL, SearchField.DOMAIN, DEFAULT_OFFSET, DEFAULT_SIZE));
		}else{
			return UmtUser2UmtUserDataAdapter.convert(getCoreMailUsers(keyword, user,limit));
		}
	}
	/**
	 * 自动提示完成框
	 * @param keyword
	 * @param limit
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/search/auto")
	public Collection<UmtUserData> searchAuto(
			@RequestParam("q") String keyword,
			@RequestParam("limit") int limit,
			@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		if(CommonUtils.isNull(keyword)){
			return null;
		}
		Collection<UMTUser> result=null;
		if(CommonUtils.isNull(user.getCoreMailDomain())){
			result=searchLocal(keyword,user);
		}else{
			result=getCoreMailUsers(keyword, user,limit);
			if(result.size()<limit){
				result=mergeCollection(result,searchLocal(keyword, user));
			}
		}
		
		return UmtUser2UmtUserDataAdapter.convert(result);
	}
	
	private Collection<UMTUser> getCoreMailUsers(String keyWord,VmtSessionUser user,int limit){
		boolean limitDefine=limit>0;
		Collection<UMTUser> emailList= umtService.searchUserByKeyword(keyWord,user.getCoreMailDomain(), SearchScope.CORE_MAIL, SearchField.CSTNET_ID, DEFAULT_OFFSET, limitDefine?limit:DEFAULT_SIZE_100);
		Collection<UMTUser> domainList=umtService.searchUserByKeyword(keyWord,user.getCoreMailDomain(), SearchScope.CORE_MAIL, SearchField.DOMAIN, DEFAULT_OFFSET, limitDefine?limit:DEFAULT_SIZE_100);
		Collection<UMTUser> trueNameList=umtService.searchUserByKeyword(keyWord,user.getCoreMailDomain(), SearchScope.CORE_MAIL, SearchField.TRUE_NAME, DEFAULT_OFFSET, limitDefine?limit:DEFAULT_SIZE_100);
		return mergeCollection(emailList,domainList,trueNameList);
	}
	private List<UMTUser> searchLocal(String keyword,VmtSessionUser user){
		return UmtUser2LdapUserAdapter.convert(search(user, keyword, ALL));
	}
	private Collection<UMTUser> mergeCollection(Collection<UMTUser>... collections){
		Set<String> repeat=new HashSet<String>();
		Collection<UMTUser> result=new ArrayList<UMTUser>();
		for(Collection<UMTUser> col:collections){
			if(col!=null){
				for(UMTUser umtUser:col){
					if(umtUser==null||repeat.contains(umtUser.getCstnetId())){
						continue;
					}
					repeat.add(umtUser.getCstnetId());
					result.add(umtUser);
				}
			}
		}
		return result;
	}
	
}
