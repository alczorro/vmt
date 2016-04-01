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
package net.duckling.vmt.dao;

/**
 * 跟实体无关的，一些dn操作
 * @author lvly
 * @since 2013-5-15
 */
public interface ICommonDAO {
	/**
	 * 通过dn，删除某个实体
	 * @param dn  要删除的实体的dn
	 */
	 void unbind(String dn);
	
	/**
	 * 判断指定的dn是否存在
	 * @param dn
	 * @return 
	 */
	 boolean isExist(String dn);

}
