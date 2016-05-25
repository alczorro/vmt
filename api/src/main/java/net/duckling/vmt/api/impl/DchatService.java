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
package net.duckling.vmt.api.impl;

import java.util.List;
import java.util.Map;

import net.duckling.vmt.api.IRestDchatService;
import net.duckling.vmt.api.domain.VmtGroup;
import net.duckling.vmt.api.domain.VmtUser;
import cn.vlabs.duckling.api.umt.rmi.exception.LoginFailed;
import cn.vlabs.rest.ServiceException;

/**
 * 专门针对Dchat的一些业务逻辑，因调用普通的api会产生过多的链接
 * @author lvly
 * @since 2013-6-3
 */
@SuppressWarnings("unchecked")
public class DchatService extends BaseService implements IRestDchatService{
	/**
	 * Constructor
	 * @param baseUrl umt的serviceurl，如http://vmt.escience.cn/services
	 * @throws LoginFailed 与vmt发生通讯错误
	 * */
	public DchatService(String baseUrl){
		super(baseUrl);
	}
	@Override
	public void addBatch(List<String[]> batchAdd) throws ServiceException {
		sendService("Dchat.addBatch", new Object[]{batchAdd});
	}
	@Override
	public Map<VmtGroup,List<VmtUser>> getGroupAndMember(String umtId) throws ServiceException {
		return (Map<VmtGroup,List<VmtUser>>)sendService("Dchat.getGroupAndMember",umtId);
	}
	@Override
	public void deleteBatch(List<String[]> batchDelete) throws ServiceException {
		sendService("Dchat.deleteBatch", new Object[]{batchDelete});
		
	}

}
