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
package net.duckling.vmt.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

public class HttpGet {
	private static final Logger LOG=Logger.getLogger(HttpGet.class);
	private String path;
	private String encode="UTF-8";
	public HttpGet(String url,String encode){
		this.path=url;
		this.encode=encode;
	}
	public HttpGet(String url){
		this.path=url;
	}
	public String connect(){
		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage()+",can't touch this url="+path, e);
			return null;
		}
		try (InputStream ins = url.openConnection().getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(ins, encode));) {
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
	

}
