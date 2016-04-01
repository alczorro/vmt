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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.api.util.NameRuleUtils;
import net.duckling.vmt.common.readFile.BatchCell;
import net.duckling.vmt.common.readFile.BatchRow;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.OfficeUtils;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.domain.ldap.LdapUser;
import net.duckling.vmt.service.IOrgService;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.vlabs.rest.ServiceException;


/**
 * 批量更新用户详细信息
 * @author lvly
 * @since 2013-8-27
 */
public class XlsUpdateImportBatchResolver {
	private String fileName;
	private InputStream ins;
	private Map<String,LdapUser> all=new HashMap<String,LdapUser>();
	private String teamDn;
	private IOrgService orgService;
	
	public XlsUpdateImportBatchResolver(List<LdapUser> users,String fileName,InputStream ins,String teamDn){
		this.fileName=fileName;
		this.ins=ins;
		this.teamDn=teamDn;
		orgService=BeanFactory.getBean(IOrgService.class);
		for(LdapUser user:users){
			all.put(user.getCstnetId(), user);
		}
	}
	public List<BatchRow> resolve()throws IOException, ServiceException{
		List<BatchRow> result=new ArrayList<BatchRow>();
		Workbook wb=null;
		if(OfficeUtils.isOffice2003(fileName)){
			wb= new HSSFWorkbook(ins);
		}else if(OfficeUtils.isOffice2007(fileName)){
			wb=new XSSFWorkbook(ins);
		}else{
			return result;
		}
		Sheet sheet = wb.getSheetAt(0);
		int rows = sheet.getLastRowNum();
		LdapOrg org=null;
		if(LdapUtils.isOrgDN(teamDn)){
			org=orgService.getOrgByDN(teamDn);
		}
		for (int i = 1; i <= rows; i++) {
			Row row = sheet.getRow(i);
			BatchRow xlsRow = new BatchRow();
			String cstnetId=OfficeUtils.getCellValue(row.getCell(0));
			LdapUser user=all.get(cstnetId);
			if(user==null){
				xlsRow.setNeedAdd(true);
			}else{
				xlsRow.setDn(user.getDn());
			}
			boolean isAllNull=true;
			for (int j = 0; j < 15; j++) {
				Cell cellValue = row.getCell(j);
				String value= OfficeUtils.getCellValue(cellValue);
				isAllNull&=CommonUtils.isNull(value);
				addCell2Row(j, xlsRow, user, value);
			}
			if(isAllNull){
				continue;
			}
			
			if(org!=null&&user!=null){
				if(org.getIsCoreMail()&&!CommonUtils.isEqualsContain(org.getDomains(), EmailUtils.getDomain(user.getCstnetId()))){
					xlsRow.setCanDo(false);
					xlsRow.getCells().get(0).setStatus("此用户的域名与组织域名不相同");
				}
			}
			xlsRow.checkCells();
			result.add(xlsRow);
		}
		return result;
	}
	private void addCell2Row(int index,BatchRow xlsRow,LdapUser user,String value){
		if(user==null){
			user=new LdapUser();
		}
		//第一列为邮箱
		if(index==0){
			String error=null;
			if(!xlsRow.isCanDo()){
				error=BatchCell.STATUS_FATAL_ERROR;
			}else if(CommonUtils.isNull(value)){
				error=BatchCell.STATUS_REQUIRED;
			}else if(!EmailUtils.isEmail(value)){
				error=BatchCell.STATUS_FORMAT_ERROR;
			}
			xlsRow.addCell(user.getCstnetId(),value,"cstnetId",error);
		}
		//第二列为真实姓名
		else if(index==1){
			String error=null;
			if(CommonUtils.isNull(value)){
				error=BatchCell.STATUS_REQUIRED;
			}
			xlsRow.addCell(user.getName(), value,"name",error);
		}
		
		//第三列部门
		else if(index==2){
			String error=null;
			if(LdapUtils.isGroupDN(teamDn)){
				value="/";
			}
			if(CommonUtils.isNull(value)||!value.contains("/")||!NameRuleUtils.isNodeIDMatch(value)){
				error=BatchCell.STATUS_FORMAT_ERROR;
			}
			xlsRow.addCell(XlsBatchUtils.dealCurrentDisplay(user.getCurrentDisplay()), value,"currentDisplay",error);
		}
		//第四列为性别
		else if(index==3){
			if(!"男".equals(value)&&!"女".equals(value)){
				value="";
			}
			xlsRow.addCell(user.getSexDisplay(), value,"sex");
		}
		//第五列为办公室
		else if(index==4){
			xlsRow.addCell(user.getOffice(), value,"office");
		}
		//第六列为办公室电话
		else if(index==5){
			xlsRow.addCell(user.getOfficePhone(), value,"officePhone");
		}
		//第七列为手机
		else if(index==6){
			xlsRow.addCell(user.getTelephone(),value,"telephone");
		}
		//第八列为职称
		else if(index==7){
			xlsRow.addCell(user.getTitle(), value,"title");
		}
		//第九列为权重
		else if(index==8){
			if(CommonUtils.getNumber(value)<=0){
				value="0";
			}
			xlsRow.addCell(user.getListRank()+"",value,"listRank");
		}
		//第十列为是否可见
		else if(index==9){
			if(CommonUtils.isNull(value)||(!Boolean.TRUE.toString().equals(value.toLowerCase())&&!Boolean.FALSE.toString().equals(value.toLowerCase()))){
				value=Boolean.TRUE.toString();
			}
			xlsRow.addCell(Boolean.toString(user.isVisible()), value,"visible");
		}
		//第十一列为是否禁用科信
		else if(index==10){
			if(CommonUtils.isNull(value)||!Boolean.TRUE.toString().equals(value.toLowerCase())&&!Boolean.FALSE.toString().equals(value.toLowerCase())){
				value=Boolean.FALSE.toString();
			}
			xlsRow.addCell(Boolean.toString(user.isDisableDchat()), value, "isDisableDchat");
		}
		//第十二列位邮箱账号状态
		else if(index==11){
			String error=null;
			if(!CommonUtils.isNull(value)&&!"正常".equals(value)&&!"锁定".equals(value)&&!"停用".equals(value)){
				error=BatchCell.STATUS_FORMAT_ERROR;
			}
			xlsRow.addCell(user.getAccountStatusDisplay(),value,"accountStatus",error);
		}
		//第十三列为过期时间
		else if(index==12){
			String error=null;
			if(!CommonUtils.isNull(value)){
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date=df.parse(value);
					if(date.getTime()<System.currentTimeMillis()){
						error="时间不能早于今天";
					}
				} catch (ParseException e) {
					error=BatchCell.STATUS_FORMAT_ERROR;
				}
				
			}
			xlsRow.addCell(user.getExpireTime(),value,"expireTime",error);
		}
		//十四列位自定义1
		else if(index==13){
			String error=null;
			if(!CommonUtils.isNull(value)){
				if(value.length()>255){
					error=BatchCell.STATUS_FORMAT_ERROR;
				}
			}
			xlsRow.addCell(user.getCustom1(),value,"custom1",error);
		}
		//第十五列为自定义2
		else if(index==14){
			String error=null;
			if(!CommonUtils.isNull(value)){
				if(value.length()>255){
					error=BatchCell.STATUS_FORMAT_ERROR;
				}
			}
			xlsRow.addCell(user.getCustom2(),value,"custom2",error);
		}
	}
	
}
