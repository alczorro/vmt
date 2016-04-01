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
package net.duckling.vmt.service.thread;

/**
 * 如果你认为，一个工作不需要同步完成，且不希望影响，前台相应时间，您有可能需要继承此类，然后放进JobThread中
 * @author lvly
 * @since 2013-5-29
 */
public interface Jobable {
	/**
	 *coreMail同步任务 
	 */
	String COREMAIL_JOB="synchronous-coreMail-orgId-";
	/**
	 * ddl同步任务
	 */
	String DDL_JOB="synchronous-ddl-teamCode-";
	/**
	 * 发送邮件
	 */
	String SEND_MAIL_JOB="send-email-to-";
	/**
	 * 作约定好的事情，例如，同步工作，发送邮件等
	 */
	void doJob();
	/**
	 * 如果您希望，队列里，一样的任务不要执行两次 ，则定义此方法
	 * @return
	 */
	String getJobId();
	/**
	 * 是否相同的方法，队列里面排重，主要依赖此方法
	 * @param job
	 * @return
	 */
	boolean isJobEquals(Jobable job);
}
