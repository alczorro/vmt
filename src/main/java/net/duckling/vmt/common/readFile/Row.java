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
package net.duckling.vmt.common.readFile;

import java.util.ArrayList;
import java.util.List;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.common.util.HashCodeGenerator;

/**
 * 每一行的数据封装类
 * 
 * @author lvly
 * @since 2013-5-9
 */
public class Row {
	/** 表示数据没问题 */
	public static final String OK = "OK";
	/** 表示数据应为email格式，但是不是 */
	public static final String NOT_LIKE_EMAIL = "not_like_email";
	/**
	 * 表示数据的列数，不符合与其
	 * */
	public static final String COLUMNS_COUNT_NOT_MATCH = "colums_count_not_match";
	/**
	 * 从文件直接读出来的元数据
	 * */
	private List<String> orgData;
	/**
	 * 第几行
	 * */
	private int index;
	/**
	 * 数据是否可用
	 * */
	private String status;
	/** 邮箱 */
	private String cstnetId;
	/**
	 * 真是姓名
	 * */
	private String truename;
	/**
	 * 交给前台使用id
	 * */
	private String oid;

	/**
	 * 构造，
	 * @param index 第几行的数据
	 */
	public Row(int index) {
		this.index = index;
		this.orgData = new ArrayList<String>();
	};

	/**
	 * 增加数据
	 * @param data
	 */
	public void addData(String data) {
		orgData.add(data);
	}
	/**
	 * 判断某一行是否全是空
	 * @return
	 */
	public boolean isNullRow(){
		boolean flag=true;
		for (String data : orgData) {
			flag&=CommonUtils.isNull(data);
		}
		return flag;
	}

	/**
	 * 分析每行的数据
	 * @return
	 */
	public Row analysis() {
		if (CommonUtils.isNull(orgData)) {
			status = COLUMNS_COUNT_NOT_MATCH;
			return this;
		}
		List<String> need = new ArrayList<String>();
		for (String data : orgData) {
			if (!CommonUtils.isNull(data)) {
				need.add(data);
			}
		}
		if (need.size() != 2) {
			status = COLUMNS_COUNT_NOT_MATCH;
			return this;
		}
		this.cstnetId = need.get(1);
		this.truename = need.get(0);
		if (!EmailUtils.isEmail(need.get(1))) {
			status = NOT_LIKE_EMAIL;
			return this;
		}
		status = OK;
		return this;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String result) {
		this.status = result;
	}

	public String getCstnetId() {
		return cstnetId;
	}

	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}

	/**
	 * @return
	 */
	public String getTruename() {
		return this.truename;
	}

	public void setTruename(String name) {
		this.truename = name;
	}

	/**
	 * @return the oid
	 */
	public String getOid() {
		setOid();
		return oid;
	}

	/**
	 * 
	 */
	public void setOid() {
		this.oid = HashCodeGenerator.getCode(this.getCstnetId() + this.index);
	}

}
