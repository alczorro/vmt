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
package net.duckling.vmt.test;

import net.duckling.vmt.common.ldap.LdapMappingResolver;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.domain.ldap.LdapDepartment;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IVmtIndexService;

import org.springframework.ldap.core.LdapTemplate;

/**
 * @author lvly
 * @since 2013-6-7
 */
public class BuildData {
	private static LdapTemplate tmpl;
	private static LdapMappingResolver<LdapGroup> groupResolver=new LdapMappingResolver<LdapGroup>(LdapGroup.class);
	private static LdapMappingResolver<LdapOrg> orgResolver=new LdapMappingResolver<LdapOrg>(LdapOrg.class);
	private static LdapMappingResolver<LdapUser> userResolver=new LdapMappingResolver<LdapUser>(LdapUser.class);
	private static LdapMappingResolver<LdapDepartment> deptResolver=new LdapMappingResolver<LdapDepartment>(LdapDepartment.class);
	public static LdapTemplate getTmpl(){
		if(tmpl==null){
			tmpl=BeanFactory.getBean(LdapTemplate.class);
		}
		return tmpl;
	}
	public static IVmtIndexService getIndexService(){
		return BeanFactory.getBean(IVmtIndexService.class);
	}
	public static String resetOrg(){
		LdapOrg org =new LdapOrg();
		org.setName("JUNIT测试用组织勿动");
		org.setCount(6);
		org.setSymbol("testCeshiyongzuzhi"+System.nanoTime());
		org.setCurrentDisplay("JUNIT测试用组织勿动");
		org.setMemberVisible(true);
		org.setPassword("passw0rd");
		org.setCreator("10000079");
		org.setAdmins(new String[]{"10000080","10000079"});
		org.setFrom("ddl");
		getTmpl().bind(orgResolver.build(org, "ou=org"));
		
		LdapDepartment dept1=new LdapDepartment();
		dept1.setCreator("system");
		dept1.setCurrentDisplay("JUNIT测试用组织勿动,部门1");
		dept1.setName("部门1");
		dept1.setSymbol("bumen1");
		getTmpl().bind(deptResolver.build(dept1, org.getDn()));
		
		LdapUser user1=new LdapUser();
		user1.setCode("h");
		user1.setCstnetId("haha@cstnet.cn");
		user1.setName("haha君");
		user1.setPinyin("hahajun");
		user1.setRandom("oiuoijeslkjrlkndsfijdsof");
		user1.setRoot(org.getDn());
		user1.setStatus(LdapUser.STATUS_ACTIVE);
		user1.setUmtId("10000079");
		user1.setCurrentDisplay("JUNIT测试用群组勿动,部门1");
		getTmpl().bind(userResolver.build(user1, dept1.getDn()));
		
		LdapUser user2=new LdapUser();
		user2.setCode("a");
		user2.setCstnetId("aaa@cstnet.cn");
		user2.setName("aa君");
		user2.setPinyin("aajun");
		user2.setRandom("oiuoijeslkjddddrlkndsfijdsof");
		user2.setRoot(org.getDn());
		user2.setStatus(LdapUser.STATUS_ACTIVE);
		user2.setUmtId("10000080");
		user2.setCurrentDisplay("JUNIT测试用群组勿动,部门1");
		getTmpl().bind(userResolver.build(user2, dept1.getDn()));
		
		LdapUser user3=new LdapUser();
		user3.setCode("y");
		user3.setCstnetId("yangxuantest111@cstnet.cn");
		user3.setName("杨轩君");
		user3.setPinyin("yangxuanjun");
		user3.setRandom("oi123123kjrlkndsfijdsof");
		user3.setRoot(org.getDn());
		user3.setStatus(LdapUser.STATUS_REFUSE);
		user3.setUmtId("10000081");
		user3.setCurrentDisplay("JUNIT测试用群组勿动,部门1");
		getTmpl().bind(userResolver.build(user3, dept1.getDn()));
		
		LdapDepartment dept2=new LdapDepartment();
		dept2.setCreator("system");
		dept2.setCurrentDisplay("JUNIT测试用组织勿动,部门2");
		dept2.setName("部门2");
		dept2.setSymbol("bumen2");
		getTmpl().bind(deptResolver.build(dept2, org.getDn()));
		
		LdapUser user21=new LdapUser();
		user21.setCode("x");
		user21.setCstnetId("xuzhijian@cnic.cn");
		user21.setName("徐志坚君");
		user21.setPinyin("xuzhijianjun");
		user21.setRandom("oiuaswqreeslkjrlkndsfijdsof");
		user21.setRoot(org.getDn());
		user21.setStatus(LdapUser.STATUS_ACTIVE);
		user21.setUmtId("10000082");
		user21.setCurrentDisplay("JUNIT测试用群组勿动");
		getTmpl().bind(userResolver.build(user21, dept2.getDn()));
		
		LdapDepartment dept21=new LdapDepartment();
		dept21.setCreator("system");
		dept21.setCurrentDisplay("JUNIT测试用组织勿动,部门2,部门21");
		dept21.setName("部门21");
		dept21.setSymbol("bumen21");
		getTmpl().bind(deptResolver.build(dept21, dept2.getDn()));
		
		LdapUser user211=new LdapUser();
		user211.setCode("m");
		user211.setCstnetId("408599730@qq.com");
		user211.setName("某人的扣扣");
		user211.setPinyin("mourendekoukou");
		user211.setRandom("111oiuoijeslkjrlkndsfijdsof");
		user211.setRoot(org.getDn());
		user211.setStatus(LdapUser.STATUS_ACTIVE);
		user211.setUmtId("10000083");
		user211.setCurrentDisplay("JUNIT测试用组织勿动,部门2,部门21");
		getTmpl().bind(userResolver.build(user211, dept21.getDn()));
		
		LdapUser user212=new LdapUser();
		user212.setCode("c");
		user212.setCstnetId("408599730@sina.com");
		user212.setName("春节的尿尿");
		user212.setPinyin("chunjiedeniaoniao");
		user212.setRandom("111oitfghlkjrlkndsfijdsof");
		user212.setRoot(org.getDn());
		user212.setStatus(LdapUser.STATUS_TEMP);
		user212.setUmtId("10000084");
		user212.setCurrentDisplay("JUNIT测试用组织勿动,部门2,部门21");
		getTmpl().bind(userResolver.build(user212, dept21.getDn()));
		
		getIndexService().buildAIndexSynchronous(org.getDn());
		return org.getDn();
	}
	
	
	public static String resetGroup(){
		LdapGroup group =new LdapGroup();
		group.setName("JUNIT测试用群组勿动");
		group.setCount(5);
		group.setSymbol("testCeshiyongqunzu"+System.nanoTime());
		group.setCurrentDisplay("JUNIT测试用群组勿动");
		group.setMemberVisible(true);
		group.setFrom("ddl");
		group.setCreator("10000079");
		group.setPassword("passw0rd");
		group.setAdmins(new String[]{"10000080","10000079"});
		getTmpl().bind(groupResolver.build(group,"ou=group"));
		
		LdapUser user1=new LdapUser();
		user1.setCode("h");
		user1.setCstnetId("haha@cstnet.cn");
		user1.setName("haha君");
		user1.setPinyin("hahajun");
		user1.setRandom("oiuoijeslkjrlkndsfijdsof");
		user1.setRoot(group.getDn());
		user1.setStatus(LdapUser.STATUS_ACTIVE);
		user1.setUmtId("10000079");
		user1.setCurrentDisplay("JUNIT测试用群组勿动");
		getTmpl().bind(userResolver.build(user1, group.getDn()));
		
		LdapUser user2=new LdapUser();
		user2.setCode("a");
		user2.setCstnetId("aaa@cstnet.cn");
		user2.setName("aa君");
		user2.setPinyin("aajun");
		user2.setRandom("oiuoijeslkjddddrlkndsfijdsof");
		user2.setRoot(group.getDn());
		user2.setStatus(LdapUser.STATUS_ACTIVE);
		user2.setUmtId("10000080");
		user2.setCurrentDisplay("JUNIT测试用群组勿动");
		getTmpl().bind(userResolver.build(user2, group.getDn()));
		
		LdapUser user3=new LdapUser();
		user3.setCode("y");
		user3.setCstnetId("yangxuantest111@cstnet.cn");
		user3.setName("杨轩君");
		user3.setPinyin("yangxuanjun");
		user3.setRandom("oi123123kjrlkndsfijdsof");
		user3.setRoot(group.getDn());
		user3.setStatus(LdapUser.STATUS_ACTIVE);
		user3.setUmtId("10000081");
		user3.setCurrentDisplay("JUNIT测试用群组勿动");
		getTmpl().bind(userResolver.build(user3, group.getDn()));
		
		LdapUser user4=new LdapUser();
		user4.setCode("x");
		user4.setCstnetId("xuzhijian@cnic.cn");
		user4.setName("徐志坚君");
		user4.setPinyin("xuzhijianjun");
		user4.setRandom("oiuaswqreeslkjrlkndsfijdsof");
		user4.setRoot(group.getDn());
		user4.setStatus(LdapUser.STATUS_ACTIVE);
		user4.setUmtId("10000082");
		user4.setCurrentDisplay("JUNIT测试用群组勿动");
		getTmpl().bind(userResolver.build(user4, group.getDn()));
		
		LdapUser user5=new LdapUser();
		user5.setCode("m");
		user5.setCstnetId("408599730@qq.com");
		user5.setName("某人的扣扣");
		user5.setPinyin("mourendekoukou");
		user5.setRandom("111oiuoijeslkjrlkndsfijdsof");
		user5.setRoot(group.getDn());
		user5.setStatus(LdapUser.STATUS_ACTIVE);
		user5.setUmtId("10000083");
		user5.setCurrentDisplay("JUNIT测试用群组勿动");
		getTmpl().bind(userResolver.build(user5, group.getDn()));
		getIndexService().buildAIndexSynchronous(group.getDn());
		return group.getDn();
	}
	
	public static void remove(String dn){
		getTmpl().unbind(dn, true);
	}
	

}
