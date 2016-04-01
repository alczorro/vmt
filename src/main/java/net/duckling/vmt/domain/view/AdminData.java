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
package net.duckling.vmt.domain.view;

/**
 * 设置页面，承载管理员内容用
 * @author lvly
 * @since 2013-6-21
 */
public class AdminData {
	/**
	 * @param umtId
	 * @param name
	 */
	public AdminData(String umtId,String name){
		this.umtId=umtId;
		this.name=name;
	}
	public AdminData(String umtId,String name,String cstnetId){
		this.umtId=umtId;
		this.name=name;
		this.email=cstnetId;
	}
	/**
	 * 管理员的真实姓名
	 */
	private String name;
	/**
	 * 管理员的umtId
	 */
	private String umtId;
	
	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
}
