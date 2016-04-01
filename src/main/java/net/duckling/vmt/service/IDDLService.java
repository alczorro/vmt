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

import java.util.List;

import net.duckling.vmt.domain.ddl.DDLGroup;

/**
 * @author lvly
 * @since 2013-5-30
 */
public interface IDDLService {
	
	/**
	 * 分析ddl过来的数据，并分析称vmt本地的数据
	 * @return
	 */
	List<String> getAllTeamCode();
	
	/**
	 * 获得团队详细信息
	 * @param teamCode 团队代号
	 * */
	DDLGroup getTeamInfo(String teamCode);
	
	/**
	 * 获得所有我能看见的团队的id
	 * @param uid
	 * @return
	 */
	List<String> getMyTemCode(String uid);
}
