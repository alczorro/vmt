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

import java.util.List;

import net.duckling.common.util.CommonUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 返回给前台的文件分析结果
 * @author lvly
 * @since 2013-5-9
 */
public class FileReadResult {
	public static final String CAN_NOT_READ="can_not_read";
	private boolean success;
	private String errorMsg;
	private List<Row> result;
	
	/**
	 * 构造函数
	 * @param rows 分析好的列数据
	 */
	public FileReadResult(List<Row> rows){
		if(CommonUtils.isNull(rows)){
			this.success=false;
			this.errorMsg=CAN_NOT_READ;
			return;
		}
		for(Row row:rows){
			switch(row.getStatus()){
				case Row.COLUMNS_COUNT_NOT_MATCH:{
					this.success=false;
					this.errorMsg=row.getStatus();
					return;
				}
				default: break;
			}
		}
		this.result=rows;
		this.success=true;
	}
	/**
	 * 构造函数
	 */
	public FileReadResult(){
		this(null);
	}
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean ok) {
		this.success = ok;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String error) {
		this.errorMsg = error;
	}
	public List<Row> getResult() {
		return result;
	}
	public void setResult(List<Row> result) {
		this.result = result;
	}
	/**
	 * 把结果变成Json对象
	 * @return
	 */
	public JSONObject toJson(){
		JSONObject object = new JSONObject();
		object.put("success", this.getSuccess());
		object.put("errorMsg", this.getErrorMsg());
		List<Row> rows=this.getResult();
		JSONArray resultJson=new JSONArray();
		if(CommonUtils.isNull(rows)){
			return object;
		}
		for(Row row:rows){
			JSONObject obj=new JSONObject();
			obj.put("truename", row.getTruename());
			obj.put("cstnetId", row.getCstnetId());
			obj.put("index", row.getIndex());
			obj.put("oid", row.getOid());
			obj.put("status", row.getStatus());
			resultJson.add(obj);
		}
		object.put("result", resultJson);
		return object;
	}

}
