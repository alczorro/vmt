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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.falcon.api.serialize.JSONMapper;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.domain.HttpGet;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.domain.VmtSessionUser;
import net.duckling.vmt.service.thread.Import20140319ConsoleJob;
import net.duckling.vmt.service.thread.ImportAllAppsConsoleJob;
import net.duckling.vmt.service.thread.JobThread;
import net.duckling.vmt.service.thread.RecoveryFromDDL;
import net.duckling.vmt.service.thread.Remove20140318ConsoleJob;
import net.duckling.vmt.service.thread.RemoveLogoConsoleJob;
import net.duckling.vmt.service.thread.ReomveAllAppsConsoleJob;
import net.duckling.vmt.service.thread.SetAllAppOpenJob;
import net.duckling.vmt.service.thread.SynchronizeDDLWithOutMQJob;
import net.duckling.vmt.service.thread.VmtBuildIndexJob;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * 
 * 
 */
@Controller
@RequestMapping("/user/console")
@SessionAttributes(KEY.SESSION_LOGIN_USER)
@SecurityMapping(level=SecurityLevel.SUPER_ADMIN)
public class ConsoleController {
	private static final Logger LOG=Logger.getLogger(ConsoleController.class);
	
	private static List<String> msg=new ArrayList<String>();
	
	@RequestMapping("getLog")
	@ResponseBody
	public Object[] getLog(){
		if(CommonUtils.isNull(msg)){
			return new String[]{};
		}
		Object[] resultList=(Object[]) msg.toArray();
		msg.clear();
		return resultList;
	}
	
	
	public static void write(String msg){
		ConsoleController.msg.add(msg);
		LOG.info(msg);
	}
	public static void write(Exception e){
		int index=0;
		for(StackTraceElement st:e.getStackTrace()){
			write(((index++)!=0?"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;":"")+"<div class=\"error\">"+st.toString()+"<div><br>");
		}
		LOG.error(e);
	}
	@RequestMapping(value="cmd",params="func=ip")
	@ResponseBody
	public boolean ip(){
		HttpGet get=new HttpGet("http://costest.escience.cn/ip/search");
		write(get.connect());
		writeOver();
		return true;
	}
	public static void writeOver(){
		write("~~~Over~~~");
	}
	@RequestMapping(value="cmd")
	@ResponseBody
	public boolean fals(HttpServletRequest request){
		write("命令没找到,别瞎捅咕'_'!:"+request.getParameter("func"));
		writeOver();
		return true;
	}
	@RequestMapping(value="cmd",params="func=importDomain")
	@ResponseBody
	public boolean import140319(){
		JobThread.addJobThread(new Import20140319ConsoleJob());
		return true;
	}
	@RequestMapping(value="cmd",params="func=removeDomain")
	@ResponseBody
	public boolean remove140318(){
		JobThread.addJobThread(new Remove20140318ConsoleJob());
		return true;
	}
	@RequestMapping(value="cmd",params="func=removeAllApps")
	@ResponseBody
	public boolean removeAllApps(){
		JobThread.addJobThread(new ReomveAllAppsConsoleJob());
		return true;
	}
	@RequestMapping(value="cmd",params="func=importAllApps")
	@ResponseBody
	public boolean importAllApps(){
		JobThread.addJobThread(new ImportAllAppsConsoleJob());
		return true;
	}
	
	@RequestMapping(value="cmd",params="func=removeLogo")
	@ResponseBody
	public boolean removeLogo(){
		JobThread.addJobThread(new RemoveLogoConsoleJob());
		return true;
	}
	@RequestMapping
	@ResponseBody
	public ModelAndView console(){
		ConsoleController.msg.clear();
		return new ModelAndView("user/gm_console");
	}
	@RequestMapping(value="cmd",params="func=whoami")
	@ResponseBody
	public boolean whoami(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser user){
		write(JSONMapper.getJSONString(user));
		writeOver();
		return true;
	}
	@RequestMapping(value="cmd",params="func=test")
	@ResponseBody
	public boolean test(){
		writeOver();
		return true;
	}
	@RequestMapping(value="cmd",params="func=buildIndex")
	@ResponseBody
	public boolean rebuild(){
		JobThread.addJobThread(new VmtBuildIndexJob(true));
		return true;
	}
	@RequestMapping(value="cmd",params="func=setAllAppOpen")
	@ResponseBody
	public boolean setAllAppOpen(){
		JobThread.addJobThread(new SetAllAppOpenJob());
		return true;
	}
	@RequestMapping(value="cmd",params="func=syncDDLWithOutMQ")
	@ResponseBody
	public boolean syncDDLWithOutMQ(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser sessionUser){
		JobThread.addJobThread(new SynchronizeDDLWithOutMQJob(sessionUser.getUserInfo().getUmtId()));
		return true;
	}
	@RequestMapping(value="cmd",params="func=recoveryFromDDL")
	@ResponseBody
	public boolean recoveryFromDDL(@ModelAttribute(KEY.SESSION_LOGIN_USER)VmtSessionUser sessionUser){
		JobThread.addJobThread(new RecoveryFromDDL(sessionUser.getUserInfo().getUmtId()));
		return true;
	}
	
	@RequestMapping(value="cmd",params="func=help")
	@ResponseBody
	public boolean help(){
		StringBuffer sb=new StringBuffer();
		sb.append("<table>");
		sb.append("		<tr>");
		sb.append("			<td>removeDomain</td>");
		sb.append("			<td>删除所有域名,是否coreMail,是否院内用户的数据(Ldap)</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("			<td>importDomain</td>");
		sb.append("			<td>从Mysql导入所有域名,是否coreMail,是否院内用户的数据</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("			<td>buildIndex</td>");
		sb.append("			<td>重建mysql索引</td>");
		sb.append("			</tr>");
		sb.append("		<tr>");
		sb.append("			<td>removeLogo</td>");
		sb.append("			<td>清楚所有的LOGO(一般用于迁移测试机的clb,导致图片出不来的问题)</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("			<td>whoami</td>");
		sb.append("			<td>返回当前登录用户(超级管理员)的json数据</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("			<td>ip</td>");
		sb.append("			<td>返回正在访问的vmt节点的ip</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("			<td>return</td>");
		sb.append("			<td>返回到Vmt首页</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("			<td>cls</td>");
		sb.append("			<td>刷新当前页面</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("			<td>removeAllApps</td>");
		sb.append("			<td>删除所有的应用,并把应用都置为空</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("			<td>importAllApps</td>");
		sb.append("			<td>导入Mysql库里的所有应用,并把有值的组织应用开关打开</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("		<tr>");
		sb.append("			<td>setAllAppOpen</td>");
		sb.append("			<td>把所有的组织机构应用全开放</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("		<tr>");
		sb.append("			<td>syncDDLWithOutMQ</td>");
		sb.append("			<td>同步ddl的团队，但是不发送mq消息</td>");
		sb.append("		</tr>");
		sb.append("		<tr>");
		sb.append("			<td>recoveryFromDDL</td>");
		sb.append("			<td>从同步ddl同步，从vmt本地建立的团队</td>");
		sb.append("		</tr>");
		
		sb.append("	</table>");
		write(sb.toString());
		writeOver();
		return true;
		
	}
	
}
