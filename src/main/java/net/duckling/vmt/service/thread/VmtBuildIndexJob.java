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

import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.service.IVmtIndexService;
import net.duckling.vmt.web.ConsoleController;

/**
 * @author lvly
 * @since 2013-7-16
 */
public class VmtBuildIndexJob implements Jobable{
	private IVmtIndexService service;
	private boolean isConsole;
	/**
	 * 构造函数，重建所有群组/机构的索引
	 * @param dn
	 */
	public VmtBuildIndexJob(){
		service=BeanFactory.getBean(IVmtIndexService.class);
	}
	public VmtBuildIndexJob(boolean isConsole){
		this();
		this.isConsole=isConsole;
	}
	@Override
	public void doJob() {
		long start=System.currentTimeMillis();
		if(isConsole){
			ConsoleController.write("重建索引开始!");
			
		}
		service.buildIndexSynchronous();
		long cost=System.currentTimeMillis()-start;
		if(isConsole){
			ConsoleController.write("索引建立完毕! 花费:"+(cost/1000)+"秒");
		}
		ConsoleController.writeOver();
	}
	@Override
	public String getJobId() {
		return "vmt-index-build-all";
	}
	@Override
	public boolean isJobEquals(Jobable job) {
		return job.getJobId().equals(this.getJobId());
	}
	

}
