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
package net.duckling.vmt.domain;

/**
 * 简单的邮件数据封装类
 * @author lvly
 * @since 2013-1-4
 */
public class SimpleEmail {
	/**收件人列表*/
	private String[] address;

	/**
	 * 邮件内容
	 * */
	private String content;
	
	/**
	 * 邮件主题
	 * */
	private String title;
	
	/**
	 * 构造方法
	 * @param address 收件人
	 * @param content 内容
	 * @param title 邮件主题
	 * */
	public SimpleEmail(String address,String content,String title){
		this.address=new String[]{address};
		this.content=content;
		this.title=title;
	}
	/**
	 * 构造方法
	 * @param address 收件人，多个时
	 * @param content 内容
	 * @param title 邮件主题
	 * */
	public SimpleEmail(String[] address,String content,String title){
		if(address!=null){
			this.address=address.clone();
		}
		this.content=content;
		this.title=title;
	}

	/**
	 * @return
	 */
	public String[] getAddress() {
		return address==null?address:address.clone();
	}

	/**
	 * @param address
	 */
	public void setAddress(String[] address) {
		this.address=address==null?null:address.clone();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
