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
package net.duckling.vmt.sms.job;

import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.sms.ISmsGetStatusService;

import org.apache.log4j.Logger;
public class GetSmsStatusJob {
	private static final Logger LOG=Logger.getLogger(GetSmsStatusJob.class);
    /**
     * 业务逻辑处理
     */
    public void doBiz() {
    	try{
    		getStatusService().pullSmsStatus();
    	}catch(RuntimeException e){
    		LOG.info("may be call sms status too refrequently!["+e.getMessage()+"]");
    	}finally{
    	}
    }
    private ISmsGetStatusService statusService;
    public ISmsGetStatusService getStatusService(){
    	if(statusService==null){
    		statusService=BeanFactory.getBean(ISmsGetStatusService.class);
    	}
    	return statusService;
    }
}
