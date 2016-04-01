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

import net.duckling.common.util.CommonUtils;

/**
 * @author lvly
 * @since 2013-8-27
 */
public class BatchCell {
	private String before;
	private String after;
	private String key;
	private boolean changed;
	private String status;


	public static final String STATUS_REQUIRED="required";
	public static final String STATUS_FORMAT_ERROR="formatError";
	public static final String STATUS_FATAL_ERROR="fatalError";
	
	
	public BatchCell(String before,String after,String key){
		this.before=before;
		this.after=after;
		this.key=key;
		changed=!isEquals();
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isEquals(){
		return CommonUtils.killNull(before).equals(CommonUtils.killNull(after));
	}
	
	public String getBefore() {
		return before;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAfter() {
		return after;
	}

	public boolean isChanged() {
		return changed;
	}
}
