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
package net.duckling.vmt.common.priv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于配置拦截URL参数用
 * @author lvly
 * @since 2013-5-23
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityMapping {
	/**
	 * 拦截登记，详看 SecurityLevel.XXX
	 * */
	SecurityLevel level() default SecurityLevel.VIEW;
	/**
	 * 前台传过来的欲操作的dn，parameter key
	 * */
	String dnParam() default "dn";
	/**
	 * 某些时候前台传的dn并不一定是dn，比如传的all，表示所有，这时候需要标识，否则查询存不存在dn的时候，会报错
	 * */
	String expect() default "";
}
