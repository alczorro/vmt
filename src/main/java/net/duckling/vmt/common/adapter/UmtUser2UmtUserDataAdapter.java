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
package net.duckling.vmt.common.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.duckling.vmt.domain.view.UmtUserData;
import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;

/**
 * UmtUser和UmtUserData的相互转换器
 * UmtUser umt api封装的数据对象
 * UmtUserData 添加人员搜索视图需要的数据对象
 * @author lvly
 * @since 2013-5-9
 */
public final class UmtUser2UmtUserDataAdapter {
	private UmtUser2UmtUserDataAdapter(){}
	/**
	 * 转换
	 * @param users
	 * @return
	 */
	public static List<UmtUserData> convert(Collection<UMTUser> users){
		if(users==null){
			return null;
		}
		List<UmtUserData> result=new ArrayList<UmtUserData>();
		for(UMTUser user:users){
			UmtUserData data=new UmtUserData();
			data.setCstnetId(user.getCstnetId());
			data.setTruename(user.getTruename());
			data.setUmtId(user.getUmtId());
			result.add(data);
		}
		return result;
	}
}
