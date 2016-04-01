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
package net.duckling.vmt.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.vmt.common.readFile.FileReadResult;
import net.duckling.vmt.common.readFile.FileResolver;
import net.duckling.vmt.common.readFile.impl.CsvResolver;
import net.duckling.vmt.common.readFile.impl.UnkownFileResolver;
import net.duckling.vmt.common.readFile.impl.XlsResolver;
import net.duckling.vmt.web.helper.JSONHelper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lvly
 * @since 2013-5-9
 */
@Controller
@RequestMapping("/user/file")
public class FileController {
	/***
	 * FireFox上传文件
	 * */
	@RequestMapping(value="/upload/xls",method = RequestMethod.POST,headers = { "X-File-Name" })
	@ResponseBody
	public FileReadResult uploadXls(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader("X-File-Name") String fileName) throws IOException {
		return getFileResolver(request.getInputStream(),fileName).getResult();
	}
	/***
	 * IE上传文件
	 * */
	@RequestMapping(value="/upload/xls",method = RequestMethod.POST)
	@ResponseBody
	public void uploadXls(
			@RequestParam("qqfile") MultipartFile uplFile,
			HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		FileResolver resolver=  getFileResolver(uplFile.getInputStream(),uplFile.getOriginalFilename());
		response.setCharacterEncoding("UTF-8");
		//IE兼容，不这么整，会下载json
		JSONHelper.writeJSONObject(response, resolver.getResult().toJson());
	}
	
	private FileResolver getFileResolver(InputStream ins,String fileName){
		if(fileName.endsWith(".csv")){
			return new CsvResolver(ins,fileName);
		}else if(fileName.endsWith(".xls")||fileName.endsWith(".xlsx")){
			return new XlsResolver(ins,fileName);
		}else{
			return new UnkownFileResolver(ins);
		}
	}
	
}
