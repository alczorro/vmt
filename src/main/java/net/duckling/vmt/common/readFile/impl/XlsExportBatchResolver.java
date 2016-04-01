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
import java.io.OutputStream;
import java.util.List;

import net.duckling.vmt.domain.ldap.LdapUser;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author lvly
 * @since 2013-8-27
 */
public class XlsExportBatchResolver {
	private List<LdapUser> users;
	private String name;
	public XlsExportBatchResolver(List<LdapUser> users,String name){
		this.users=users;
		this.name=name;
	}
	public void export(OutputStream os)throws IOException{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(name);
		sheet.autoSizeColumn(1,true); 
		HSSFRow row = sheet.createRow(0);
		int index=0;
		row.createCell(index++).setCellValue("邮件");
		row.createCell(index++).setCellValue("姓名");
		row.createCell(index++).setCellValue("部门");
		row.createCell(index++).setCellValue("性别");
		row.createCell(index++).setCellValue("办公室");
		row.createCell(index++).setCellValue("办公室电话");
		row.createCell(index++).setCellValue("手机");
		row.createCell(index++).setCellValue("职称");
		row.createCell(index++).setCellValue("权重");
		row.createCell(index++).setCellValue("是否可见");
		row.createCell(index++).setCellValue("是否禁用科信");
		row.createCell(index++).setCellValue("邮箱账号状态");
		row.createCell(index++).setCellValue("过期时间");
		row.createCell(index++).setCellValue("自定义1");
		row.createCell(index++).setCellValue("自定义2");
		int dataRowIndex=0;
		for(LdapUser u:users){
			HSSFRow dataRow = sheet.createRow(++dataRowIndex);
			int i=0;
			dataRow.createCell(i++).setCellValue(u.getCstnetId());
			dataRow.createCell(i++).setCellValue(u.getName());
			dataRow.createCell(i++).setCellValue(XlsBatchUtils.dealCurrentDisplay(u.getCurrentDisplay()));
			dataRow.createCell(i++).setCellValue(u.getSexDisplay());
			dataRow.createCell(i++).setCellValue(u.getOffice());
			dataRow.createCell(i++).setCellValue(u.getOfficePhone());
			dataRow.createCell(i++).setCellValue(u.getTelephone());
			dataRow.createCell(i++).setCellValue(u.getTitle());
			dataRow.createCell(i++).setCellValue(u.getListRank());
			dataRow.createCell(i++).setCellValue(u.isVisible());
			dataRow.createCell(i++).setCellValue(u.isDisableDchat());
			dataRow.createCell(i++).setCellValue(u.getAccountStatusDisplay());
			dataRow.createCell(i++).setCellValue(u.getExpireTime());
			dataRow.createCell(i++).setCellValue(u.getCustom1());
			dataRow.createCell(i++).setCellValue(u.getCustom2());
		}
		for(int i=0;i<10;i++){
			sheet.autoSizeColumn(i,true); 
		}
		wb.write(os);
		os.close();
	}
}
