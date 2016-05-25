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
package net.duckling.vmt.api;

import java.util.List;
import java.util.Map;

import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.api.domain.VmtUser;
import cn.vlabs.rest.ServiceException;

/**
 * 针对Dchat写的接口，别的应用也可以调用，比较遵循和Dchat的约定
 * @author lvly
 * @since 2013-5-31
 */
public interface IRestDchatService {
	/**
	 * 批量增加用户，目的是减少通讯量
	 * @param batchAdd [[teamDn,userEmail],......]
	 * @throws ServiceException 
	 */
	void addBatch(List<String[]> batchAdd) throws ServiceException;
	
	/**
	 * 批量删除用户，目的是减少通讯量,如果删后发现只剩创建者了，就删掉
	 * @param batchDelete [[teamDn,userEmail],......]
	 * @throws ServiceException 
	 */
	void deleteBatch(List<String[]> batchDelete)throws ServiceException;
	
	/**
	 * 需要返回，creator创建的群组，且包含所有的群组成员
	 * @param umtId 群组创建者的umtId
	 * @return 匹配结果
	 * @throws ServiceException
	 */
	Map<VmtGroup,List<VmtUser>> getGroupAndMember(String umtId) throws ServiceException;
}
