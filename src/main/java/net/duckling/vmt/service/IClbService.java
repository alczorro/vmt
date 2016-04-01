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

import java.io.File;
import java.io.InputStream;

import net.duckling.vmt.service.impl.FileSaverBridge;
import cn.vlabs.clb.api.image.ResizeParam;
import cn.vlabs.rest.IFileSaver;

/**
 * 针对clb的可操作类
 * @author lvly
 * @since 2013-6-8
 */
public interface IClbService {
	String small="small";
	String medium="medium";
	String large="large";
	String latest="latest";
	
	/**
	 * 往clb上传文件
	 * @param ins 文件流
	 * @param fileName 文件名
	 * @return docId
	 */
	int upload(InputStream ins,String fileName);
	int upload(InputStream ins,String fileName,ResizeParam param);
	/**
	 * 往clb上传文件
	 * @param ins 文件流
	 * @param fileName 文件名
	 * @return docId
	 */
	int uploadUnResize(InputStream ins,String fileName, int length);
	
	/**
	 * 上传涂鸦
	 * */

	/**
	 * 下载文件
	 * @param imgId
	 * @param saver
	 * @return
	 */
	void downloadLogo(int imgId,IFileSaver saver,String type);
	
	File downloadAsFile(int docId);
	
	void download(int fileId, FileSaverBridge fileSaverBridge);
}
