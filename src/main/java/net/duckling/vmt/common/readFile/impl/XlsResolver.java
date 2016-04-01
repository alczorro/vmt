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
import java.util.ArrayList;
import java.util.List;

import net.duckling.vmt.common.readFile.FileReadResult;
import net.duckling.vmt.common.readFile.FileResolver;
import net.duckling.vmt.common.readFile.Row;
import net.duckling.vmt.common.util.OfficeUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 如果为Excel文件格式，请用此解析
 * @author lvly
 * @since 2013-5-9
 */
public class XlsResolver extends FileResolver {
	
	/**Constructor 
	 * @param ins 数据流
	 * @param fileName 文件名，用于判断是office版本
	 */
	public XlsResolver(InputStream ins,String fileName) {
		super(ins,fileName);
	}
	
	
	/**
	 * 分析文件，并且获得分级结果
	 * @throws IOException
	 * @return 分析结果，是否完全不可读，或者数据格式有误，或者某个列有问题
	 * */
	public FileReadResult getResult() throws IOException {
		List<Row> result = new ArrayList<Row>();
		Workbook wb=null;
		if(OfficeUtils.isOffice2003(getFileName())){
			wb= new HSSFWorkbook(getInputStream());
		}else if(OfficeUtils.isOffice2007(getFileName())){
			wb=new XSSFWorkbook(getInputStream());
		}else{
			return new FileReadResult(result);
		}
		Sheet sheet = wb.getSheetAt(0);
		int rows = sheet.getLastRowNum();
		for (int i = START_INDEX; i < rows+1; i++) {
			org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
			Row xlsRow = new Row(i + 1);
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell cellValue = row.getCell(j);
				xlsRow.addData(OfficeUtils.getCellValue(cellValue));
			}
			if(xlsRow.isNullRow()){
				continue;
			}
			xlsRow.analysis();
			result.add(xlsRow);
		}
		return new FileReadResult(result);
	};
	

}
