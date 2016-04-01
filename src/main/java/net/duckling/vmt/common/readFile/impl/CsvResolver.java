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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.readFile.FileReadResult;
import net.duckling.vmt.common.readFile.FileResolver;
import net.duckling.vmt.common.readFile.Row;

import org.apache.log4j.Logger;

/**
 * CSV文件解释器
 * 
 * @author lvly
 * @since 2013-5-9
 */
public class CsvResolver extends FileResolver {
	private static final Logger LOG=Logger.getLogger(CsvResolver.class);
	/**
	 * 前台传的文件
	 * 
	 * @param ins
	 */
	public CsvResolver(InputStream ins, String fileName) {
		super(ins, fileName);
	}

	@Override
	public FileReadResult getResult() throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(), "GBK"));) {
			String line;
			int index = 0;
			List<Row> result = new ArrayList<Row>();
			while ((line = reader.readLine()) != null) {
				if (index++ < START_INDEX) {
					continue;
				}
				Row xlsRow = new Row(index);
				if (CommonUtils.isNull(line)) {
					continue;
				}
				for (String str : line.split(",")) {
					xlsRow.addData(str);
				}
				result.add(xlsRow.analysis());
			}
			reader.close();
			return new FileReadResult(result);
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
			return null;
		}

	}

}
