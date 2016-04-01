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

/**
 * @author lvly
 * @since 2013-7-16
 */
public class VmtBuildAIndexJob implements Jobable {
	private String dn;
	private IVmtIndexService service;
	
	/**
	 * 构造函数，重建某个群组/机构的索引
	 * @param dn
	 */
	public VmtBuildAIndexJob(String dn){
		this.dn=dn;
		service=BeanFactory.getBean(IVmtIndexService.class);
		
	}
	@Override
	public void doJob() {
		service.buildAIndexSynchronous(dn);
	}

	@Override
	public String getJobId() {
		return "build-index-["+dn+"]";
	}

	@Override
	public boolean isJobEquals(Jobable job) {
		return this.getJobId().equals(job.getJobId());
	}

}
