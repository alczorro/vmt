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
package net.duckling.vmt.common.util;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;

/**
 * @author lvly
 * @since 2013-6-8
 */
public class BrowseUtils {
	private static final int MAX_UNICODE_FILE_LEN = 1900; //文件名编码为utf8后的最大Unicode长度
	private static final int MAX_FILE_LEN = 215; //文件名的长度：中文字，英文字均只算1
	
	private BrowseUtils(){}
	public static enum BrowserType {
		Chrome, Firefox, MsIE, Safari, Unknown
	};

	public static String encodeFileName(String agent, String filename)
			throws UnsupportedEncodingException {
		BrowserType type = recognizeBrowser(agent);
		String result;
		switch (type) {
		case MsIE:
		case Chrome:
			result = "attachment;filename=\"" + encodeWithURLEncoder(filename)
					+ "\"";
			break;
		case Firefox:
			result = "attachment;filename=\"" + encodeWithBase64(filename)
					+ "\"";
			break;
		case Safari:
			result = "attachment;filename=\"" +encodeWithISO(filename)+"\"";
			break;
		default:
			result = "attachment;filename=\"" + encodeWithURLEncoder(filename)
			+ "\"";
			break;
		}
		return result;
	}

	private static String encodeWithBase64(String filename)
			throws UnsupportedEncodingException {
		return MimeUtility.encodeText(filename, "UTF-8", "B");
	}

	private static String encodeWithISO(String filename) throws UnsupportedEncodingException{
		return new String(filename.getBytes("UTF-8"),"ISO8859-1");  
	}
	private static String encodeWithURLEncoder(String filename)
			throws UnsupportedEncodingException {
		
		String codedfilename = java.net.URLEncoder.encode(filename, "UTF-8");
		codedfilename = StringUtils.replace(codedfilename, "+", "%20");
		String tempFileName = filename;
		if(MAX_UNICODE_FILE_LEN < codedfilename.length() && MAX_FILE_LEN < filename.length()){ //文件名超长可能造成浏览器无法下载或下载文件名乱码
			int filenameLen = filename.length();
			int dotIndex = filename.lastIndexOf('.');
			int suffixLen = filenameLen - dotIndex;
			tempFileName = filename.substring(0, MAX_FILE_LEN - suffixLen - (filenameLen - MAX_FILE_LEN));
			tempFileName += filename.substring(dotIndex, filenameLen);
			codedfilename = java.net.URLEncoder.encode(tempFileName, "UTF-8");
			codedfilename = StringUtils.replace(codedfilename, "+", "%20");
		}
		return codedfilename;
	}

	public static BrowserType recognizeBrowser(String agent) {
		BrowserType type = BrowserType.Unknown;
		if (agent != null) {
			agent = agent.toLowerCase();
			if (-1 != agent.indexOf("msie")) {
				type = BrowserType.MsIE;
			} else if (-1 != agent.indexOf("chrome")) {
				type = BrowserType.Chrome;
			} else if (-1 != agent.indexOf("safari")) {
				type = BrowserType.Safari;
			} else if (-1 != agent.indexOf("firefox")) {
				type = BrowserType.Firefox;
			}
		}
		return type;
	}
}
