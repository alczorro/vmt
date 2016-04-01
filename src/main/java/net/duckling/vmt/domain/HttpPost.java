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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.duckling.cloudy.common.CommonUtils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpPost {
	private String url;
	private Map<String,List<String>> param=new HashMap<String,List<String>>();
	public void add(String key,String value){
		if(param.containsKey(key)){
			param.get(key).add(value);
		}else{
			List<String> values=new ArrayList<String>();
			values.add(value);
			param.put(key, values);
		}
		
	}
	public HttpPost(String url){
		this.url=url;
	}
	public String post(){
		if(CommonUtils.isNull(url)){
			throw new RuntimeException("why url is null");
		}
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		for(Entry<String,List<String>> en:param.entrySet()){
			List<String> values=en.getValue();
			if(CommonUtils.isNull(values)){
				continue;
			}
			StringBuffer sb=new StringBuffer();
			
			for(String value:en.getValue()){
				sb.append(value).append(",");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			postMethod.setParameter(en.getKey(),sb.toString());
			
		}
		HttpClient httpClient = new HttpClient();
		HttpConnectionManagerParams managerParams = httpClient
				.getHttpConnectionManager().getParams();
		managerParams.setConnectionTimeout(60000);
		managerParams.setSoTimeout(120000);
		try {
			httpClient.executeMethod(postMethod);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		int code = postMethod.getStatusCode();
		if(code==200){
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
						postMethod.getResponseBodyAsStream(), "utf-8"));
				StringBuffer resBuffer = new StringBuffer();
				String resTemp = "";
				while ((resTemp = br.readLine()) != null) {
					resBuffer.append(resTemp);
				}
				return resBuffer.toString();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}else{
			throw new RuntimeException("return http code is "+code);
		}
	}
}
