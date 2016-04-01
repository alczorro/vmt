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
package net.duckling.vmt.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.vmt.common.util.BrowseUtils;
import net.duckling.vmt.common.util.LdapUtils;

import org.apache.log4j.Logger;

import cn.vlabs.clb.api.io.impl.MimeType;
import cn.vlabs.rest.IFileSaver;

/**
 * 桥接至CLB的接口。
 * 
 * @date 2012-8-10
 * @author lvly
 */
public class FileSaverBridge implements IFileSaver {
	private static final int BUFFER_SIZE = 4096;
	private static final Logger LOG = Logger.getLogger(FileSaverBridge.class);
	private OutputStream out = null;

	private HttpServletResponse response = null;

	private HttpServletRequest request = null;
	/**
	 * 构造函数
	 * @param res
	 * @param req
	 */
	public FileSaverBridge(HttpServletResponse res, HttpServletRequest req) {
		this.response = res;
		this.request = req;
	}
	/**
	 * 保存文件
	 */
	public void save(String fileName, InputStream in) {
		try {
			setResponseHeader(fileName);
			writeData(in);
		} catch (FileNotFoundException e) {
			LOG.info("没有找到文件。fileName=["+fileName+"]",e);
		} catch (IOException e) {
			LOG.info("文件下载出错。fileName=["+fileName+"]",e);
		} finally {
			closeStream(in);
		}
	}
	
	private void setResponseHeader(String fileName) throws UnsupportedEncodingException{
		String filename = LdapUtils.decode(fileName);
		String mimetype = getMimeType(request, filename);
		response.setContentType(mimetype);
		String agent = request.getHeader("USER-AGENT");
		String suffix = filename.substring(filename.indexOf('.') + 1, filename.length());
		response.setContentType(MimeType.getContentType(suffix));
		if (filename.endsWith("swf")||filename.endsWith(("flv"))) {
			response.setContentType("application/x-shockwave-flash");
		} else {
			response.setHeader("Content-Disposition", BrowseUtils.encodeFileName(agent, filename));
		}
	}
	
	private void writeData(InputStream in) throws IOException{
		if (out == null) {
			out = response.getOutputStream();
		}
		int read = 0;
		byte buf[] = new byte[BUFFER_SIZE];
		while ((read = in.read(buf, 0, BUFFER_SIZE)) != -1) {
			out.write(buf, 0, read);
		}
	}
	
	private void closeStream(InputStream in){
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				LOG.error(e);
			}
		}
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				LOG.error(e);
			}
		}
	}

	private static String getMimeType(HttpServletRequest req, String fileName) {
		String mimetype = null;
		if (req != null) {
			ServletContext s = req.getSession().getServletContext();
			if (s != null) {
				mimetype = s.getMimeType(fileName);
			}
		}
		if (mimetype == null) {
			mimetype = "application/binary";
		}

		return mimetype;
	}
}
