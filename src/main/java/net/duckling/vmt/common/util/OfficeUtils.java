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

import java.text.DecimalFormat;

import net.duckling.cloudy.common.CommonUtils;

import org.apache.poi.ss.usermodel.Cell;

/**
 * @author lvly
 * @since 2013-8-27
 */
public final class OfficeUtils {
	private OfficeUtils(){}
	/**
	 * 判断是否是2003以前office的版本
	 * @return
	 */
	public static boolean isOffice2003(String fileName){
		if(CommonUtils.isNull(fileName)){
			return false;
		}
		return fileName.endsWith(".xls");
	}
	/**
	 * 判断是否是2003以后的office版本
	 * @return 
	 * */
	public static boolean isOffice2007(String fileName){
		if(CommonUtils.isNull(fileName)){
			return false;
		}
		return fileName.endsWith(".xlsx");
	}
	public static String getCellValue(Cell cell){
		if(cell==null){
			return "";
		}
		switch(cell.getCellType()){
			case Cell.CELL_TYPE_BLANK:{
				return "";
			}
			case Cell.CELL_TYPE_BOOLEAN:{
				return String.valueOf(cell.getBooleanCellValue());
			}
			case Cell.CELL_TYPE_NUMERIC:{ 
				DecimalFormat df = new DecimalFormat("0");  
				return  df.format(cell.getNumericCellValue()); 
			}
			case Cell.CELL_TYPE_STRING:{
				return String.valueOf(cell.getStringCellValue());
			}
			default:{
				return cell.toString();
			}
		}
	}

}
