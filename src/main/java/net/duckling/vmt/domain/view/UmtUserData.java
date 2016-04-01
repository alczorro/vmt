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
package net.duckling.vmt.domain.view;

import net.duckling.vmt.common.util.HashCodeGenerator;

/**
 * 针对umt用户信息的包装类
 * @author lvly
 * @since 2013-5-9
 */
public class UmtUserData {
	/**
	 * 用户邮箱
	 */
	private String cstnetId;
	/**
	 * 用户真实姓名
	 */
	private String truename;
	/**
	 * 标识，无意义
	 */
	private String oid;
	/**
	 * 用户的umtId
	 */
	private String umtId;
	
	private String mobile;
	
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCstnetId() {
		return cstnetId;
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	/**
	 * @return
	 */
	public String getOid() {
		this.oid=HashCodeGenerator.getCode(this.cstnetId);
		return this.oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	

}
