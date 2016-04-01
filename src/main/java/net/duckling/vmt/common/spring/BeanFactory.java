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
package net.duckling.vmt.common.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 不建议使用此方法获取bean，当无法用正常的方法（@Autuwired）获取时，请使用此方法
 * @author lvly
 * @since 2013-6-8
 */
@Service
public final class BeanFactory implements ApplicationContextAware{
    private BeanFactory() {
    }
    
    private static ApplicationContext appContext;
	
    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext)  {
    	BeanFactory.setApplication(applicationContext);
    }
    /**
     * @param applicationContext
     */
    private static void setApplication(ApplicationContext applicationContext){
    	appContext=applicationContext;
    }
	/**
	 * 获得容器里的bean，无论是注解的还是配置的
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz){
		 return (T)appContext.getBean(clazz);
	}
	

}
