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
package net.duckling.vmt.common.readFile.impl;

import java.io.IOException;
import java.io.InputStream;

import net.duckling.vmt.common.readFile.FileReadResult;
import net.duckling.vmt.common.readFile.FileResolver;

/**
 * 如果文件不可读，实例化此类
 * @author lvly
 * @since 2013-5-9
 */
public class UnkownFileResolver extends FileResolver {

	/**
	 * @param ins
	 */
	public UnkownFileResolver(InputStream ins) {
		super(ins,null);
	}

	@Override
	public FileReadResult getResult() throws IOException {
		return new FileReadResult();
	}

}
