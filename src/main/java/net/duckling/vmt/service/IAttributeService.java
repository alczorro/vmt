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
package net.duckling.vmt.service;

import java.util.Map;

/**
 * 针对某个dn的attribute更改方法，通用
 * @author lvly
 * @since 2013-5-13
 */
public interface IAttributeService {
	/**
	 * 增加属性
	 * @param dn 要加入属性的dn
	 * @param attribute 属性名
	 * @param values 值
	 * */
	 void insert(String dn,String attrubuteName,String values[]);
	
	/**
	 * 删属性
	 * @param dn 欲删除属性的dn
	 * @param attribute 属性名
	 * @param values 要删除的值
	 * */
	 void delete(String dn,String attrubuteName,String values[]);
	
	/**
	 * 获得某个属性的值
	 * @param dn路径
	 * @param attributeName属性名
	 * */
	 String[] get(String dn,String attributeName);
	
	/**
	 *当前属性是否添加过
	 *@param String dn
	 *@param attributeName
	 *@param value 
	 * */
	 boolean isExists(String dn,String attributeName,String value);
	
	/***
	 * 单个属性使用，多个属性的时候会把所有都干掉，然后update成一个值
	 * @param dn
	 * @param attributeName 属性名
	 * @param value 要更新的值
	 * */
	 void update(String dn,String attributeName,Object value);
	 /**
	  * 查询某个根下的所有用户的属性
	  * @param dn
	  * @param attributeName
	  * @return
	  */
	 Map<String, String> search(String dn, String attributeName);
	 
}
