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
package net.duckling.vmt.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.duckling.vmt.common.config.VmtConfig;
import net.duckling.vmt.service.CLBResizeparamFactory;
import net.duckling.vmt.service.IClbService;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.CLBPasswdInfo;
import cn.vlabs.clb.api.CLBServiceFactory;
import cn.vlabs.clb.api.document.CreateInfo;
import cn.vlabs.clb.api.document.DocumentService;
import cn.vlabs.clb.api.document.UpdateInfo;
import cn.vlabs.clb.api.image.IResizeImageService;
import cn.vlabs.clb.api.image.ResizeParam;
import cn.vlabs.rest.IFileSaver;
import cn.vlabs.rest.stream.StreamInfo;

/**
 * @author lvly
 * @since 2013-6-8
 */
@Service
public class ClbServiceImpl implements IClbService{
	private static final Logger LOG=Logger.getLogger(ClbServiceImpl.class);
	
	@Autowired
	private VmtConfig config;
	
	public CLBConnection getConnection(){
		CLBPasswdInfo pwd=new CLBPasswdInfo();
		pwd.setUsername(config.getClbUserName());
		pwd.setPassword(config.getClbPassword());
		return new CLBConnection(config.getClbUrl(),pwd);
	}
	
	public IResizeImageService getResizeService(){
		return CLBServiceFactory.getResizeImageService(getConnection());
	}
	
	public DocumentService getDocService(){
		return CLBServiceFactory.getDocumentService(getConnection());
	}
	@Override
	public int uploadUnResize(InputStream ins, String fileName,int length) {
		try {
			StreamInfo stream=new StreamInfo();
			stream.setFilename(fileName);
			stream.setLength(length);
			stream.setInputStream(ins);
			CreateInfo info=new CreateInfo();
			info.setTitle(fileName);
			info.setIsPub(1);
			UpdateInfo updateInfo=getDocService().createDocument(info,stream);
			return updateInfo.getDocid();
		} catch (Exception e){
			LOG.error(e.getMessage(),e);
		}
		return -1;
	}
	
	@Override
	public int upload(InputStream ins, String fileName) {
		return upload(ins, fileName, CLBResizeparamFactory.getCommonResizeParam());
	}
	@Override
	public int upload(InputStream ins, String fileName, ResizeParam param) {

		try {
			StreamInfo stream=new StreamInfo();
			stream.setFilename(fileName);
			stream.setLength(ins.available());
			stream.setInputStream(ins);
			CreateInfo info=new CreateInfo();
			info.setTitle(fileName);
			info.setIsPub(1);
			UpdateInfo updateInfo=getDocService().createDocument(info,stream);
			getResizeService().resize(updateInfo.getDocid(),updateInfo.getVersion(),param);
			return updateInfo.getDocid();
		} catch (Exception e){
			LOG.error(e.getMessage(),e);
		}
		return -1;
	}
	@Override
	public void downloadLogo(int imgId,IFileSaver saver,String type){
		getResizeService().getContent(imgId, latest, type,saver);
	}
	@Override
	public void download(int fileId, FileSaverBridge fileSaverBridge) {
		getDocService().getContent(fileId, fileSaverBridge);
	}
	@Override
	public File downloadAsFile(int docId) {
		try {
			final File tmpFile=File.createTempFile(System.currentTimeMillis()+"", ".tmp");
			getDocService().getContent(docId, new IFileSaver() {
				@Override
				public void save(String fileName, InputStream ins) {
					try {
						FileOutputStream fos=new FileOutputStream(tmpFile);
						IOUtils.copy(ins, fos);
						fos.flush();
						fos.close();
					} catch ( IOException e) {
						LOG.error(e.getMessage(),e);
					}
				}
			});
			return tmpFile;
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
}
