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
package net.duckling.vmt.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.duckling.vmt.service.thread.JobThread;

/**
 * VMT初始化的时候做的一些事情
 * @author lvly
 * @since 2013-5-13
 */
public class VMTStartUpListener implements ServletContextListener  {
	private JobThread jobThread;
	/**
	 * 
	 * 销毁方法，把邮件队列停用
	 * @param context
	 * */
	@Override
	public void contextDestroyed(ServletContextEvent context) {
		jobThread.shutdown();
		jobThread=null;
		
	}
	/**
	 * 初始化方法，初始化邮件队列
	 *@param context
	 */
	@Override
	public void contextInitialized(ServletContextEvent context) {
		jobThread=new JobThread();
		jobThread.start();
		
	}

}
