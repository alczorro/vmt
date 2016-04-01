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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件解释器的抽象类
 * @author lvly
 * @since 2013-5-9
 */
public abstract class FileResolver {
	/**
	 * 默认第几行开始？
	 * */
	protected static final int START_INDEX=1;
	/**
	 * 数据流，不解释
	 * */
	private InputStream inputStream;
	/**
	 * 文件名
	 * */
	private String fileName;
	
	/**
	 * @param ins
	 * @param fileName
	 */
	public FileResolver(InputStream ins,String fileName){
		this.inputStream=new BufferedInputStream(ins);
		this.fileName=fileName;
	}
	/**
	 * 抽象方法，其他解释器，请自定义逻辑规则
	 * */
	public abstract FileReadResult getResult()throws IOException;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
}
