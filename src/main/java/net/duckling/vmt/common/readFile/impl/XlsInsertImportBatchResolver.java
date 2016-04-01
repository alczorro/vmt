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
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.api.util.NameRuleUtils;
import net.duckling.vmt.common.readFile.BatchCell;
import net.duckling.vmt.common.readFile.BatchRow;
import net.duckling.vmt.common.spring.BeanFactory;
import net.duckling.vmt.common.util.EmailUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.common.util.OfficeUtils;
import net.duckling.vmt.common.util.PasswordUtils;
import net.duckling.vmt.domain.ldap.LdapGroup;
import net.duckling.vmt.domain.ldap.LdapOrg;
import net.duckling.vmt.service.ICoreMailService;
import net.duckling.vmt.service.IGroupService;
import net.duckling.vmt.service.IOrgDomainMappingService;
import net.duckling.vmt.service.IOrgService;
import net.duckling.vmt.service.IUserService;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.userv7.UserService;
import cn.vlabs.rest.ServiceException;


/**
 * 批量更新用户详细信息
 * @author lvly
 * @since 2013-8-27
 */
public class XlsInsertImportBatchResolver {
	private String fileName;
	private InputStream ins;
	private String teamDn;
	private IOrgService orgService;
	private UserService userService;
	private IUserService luserService;
	private IOrgDomainMappingService domainService;
	private IGroupService groupService;
	private ICoreMailService coreMailService;
	
	public XlsInsertImportBatchResolver(String fileName,InputStream ins,String teamDn){
		this.fileName=fileName;
		this.ins=ins;
		this.teamDn=teamDn;
		orgService=BeanFactory.getBean(IOrgService.class);
		userService=BeanFactory.getBean(UserService.class);
		domainService=BeanFactory.getBean(IOrgDomainMappingService.class);
		luserService=BeanFactory.getBean(IUserService.class);
		groupService=BeanFactory.getBean(IGroupService.class);
		coreMailService=BeanFactory.getBean(ICoreMailService.class);
		
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
		LdapGroup group=null;
		if(LdapUtils.isOrgDN(teamDn)){
			org=orgService.getOrgByDN(teamDn);
		}else if(LdapUtils.isGroupDN(teamDn)){
			group=groupService.getGroupByDN(teamDn);
		}
		for (int i = 1; i <= rows; i++) {
			Row row = sheet.getRow(i);
			BatchRow xlsRow = new BatchRow();
			boolean isAllNull=true;
			for (int j = 0; j < 16; j++) {
				Cell cellValue = row.getCell(j);
				String value= OfficeUtils.getCellValue(cellValue);
				isAllNull&=CommonUtils.isNull(value);
				addCell2Row(j, xlsRow, value);
			}
			if(isAllNull){
				continue;
			}
			checkUserCanCreate(xlsRow,org,group);
			xlsRow.checkCells();
			
			result.add(xlsRow);
		}
		return result;
	}
	/***/
	private boolean checkAgain(BatchRow xlsRow,LdapOrg org,LdapGroup group){
		BatchCell c=xlsRow.getCells().get(0);
		String cstnetId=c.getAfter();
		String domain=EmailUtils.getDomain(cstnetId);
		boolean isCoreMailDomain=domainService.isDomainExists(domain);
		if(org!=null){
			boolean isMainDomain=org.isMainDomain(domain);
			if(isCoreMailDomain){
				if(isMainDomain){
					xlsRow.setCustom("regist.coreMail");
					return true;
				}else{
					c.setStatus("无效域名");
					return false;
				}
			}
		}else if(group!=null){
			if(isCoreMailDomain){
				c.setStatus("域名无效");
				return false;
			}
		}
		xlsRow.setCustom("regist.umt");
		return true;
	}
	private void checkUserCanCreate(BatchRow xlsRow,LdapOrg org,LdapGroup group){
		BatchCell c=xlsRow.getCells().get(0);
		String cstnetId=c.getAfter();
		if(!CommonUtils.isNull(cstnetId)){
			UMTUser user=userService.getUMTUserByLoginName(cstnetId);
			boolean isCoreMailButDeleted=false;
			//用户非空,可能需要注册(coreMail删除,umt还在的用户),需要导入(如果当前群组无此人),用户已存在
			if(user!=null){
				isCoreMailButDeleted=!"umt".equals(user.getAuthBy())&&!coreMailService.isUserExists(cstnetId);
				if(isCoreMailButDeleted){
					//注册逻辑
					checkAgain(xlsRow,org,group);
					return;
				}
				else if(CommonUtils.isNull(luserService.searchUsersByCstnetId(this.teamDn,new String[]{ cstnetId}))){
					xlsRow.setCustom("import");
					return;
				}else{
					c.setStatus("用户已存在");
					return;
				}
			}
			//注册逻辑
			else{
				checkAgain(xlsRow, org, group);
			}
			
			
		}
	}
	private void addCell2Row(int index,BatchRow xlsRow,String value){
		String error=null;
		//第一列为邮箱
		if(index==0){
			if(CommonUtils.isNull(value)){
				error=BatchCell.STATUS_REQUIRED;
			}else if(!EmailUtils.isEmail(value)||value.contains("..")){
				error=BatchCell.STATUS_FORMAT_ERROR;
			}
			xlsRow.addCell(null,value,"cstnetId",error);
		}
		
		//第二列为密码
		else if(index==1){
			if(CommonUtils.isNull(value)){
				error=BatchCell.STATUS_REQUIRED;
			}else if(!PasswordUtils.canUse(value)){
				error=BatchCell.STATUS_FORMAT_ERROR;
			}
			xlsRow.addCell(null,value,"password",error);
		}
		//第三列为真实姓名
		else if(index==2){
			if(CommonUtils.isNull(value)){
				error=BatchCell.STATUS_REQUIRED;
			}
			xlsRow.addCell(null,value,"name",error);
		}
		
		//第四列真实 部门
		else if(index==3){
			if(LdapUtils.isGroupDN(teamDn)){
				value="/";
			}
			if(CommonUtils.isNull(value)||!value.contains("/")||!NameRuleUtils.isNodeIDMatch(value)){
				error=BatchCell.STATUS_FORMAT_ERROR;
			}
			xlsRow.addCell(null, value,"currentDisplay",error);
		}
		//第五列为性别
		else if(index==4){
			if(!"男".equals(value)&&!"女".equals(value)){
				value="";
			}
			xlsRow.addCell(null, value,"sex");
		}
		//第6列为办公室
		else if(index==5){
			xlsRow.addCell(null, value,"office");
		}
		//第7列为办公室电话
		else if(index==6){
			xlsRow.addCell(null, value,"officePhone");
		}
		//第8列为手机
		else if(index==7){
			xlsRow.addCell(null,value,"telephone");
		}
		//第9列为职称
		else if(index==8){
			xlsRow.addCell(null, value,"title");
		}
		//第10列为权重
		else if(index==9){
			if(CommonUtils.getNumber(value)<=0){
				value="0";
			}
			xlsRow.addCell(null,value,"listRank");
		}
		//第11列为是否可见
		else if(index==10){
			if(CommonUtils.isNull(value)||(!Boolean.TRUE.toString().equals(value.toLowerCase())&&!Boolean.FALSE.toString().equals(value.toLowerCase()))){
				value=Boolean.TRUE.toString();
			}
			xlsRow.addCell(null, value,"visible");
		}
		//第12列为是否禁用科信
		else if(index==11){
			if(CommonUtils.isNull(value)||!Boolean.TRUE.toString().equals(value.toLowerCase())&&!Boolean.FALSE.toString().equals(value.toLowerCase())){
				value=Boolean.FALSE.toString();
			}
			xlsRow.addCell(null, value, "isDisableDchat");
		}
		//第13列位邮箱账号状态
		else if(index==12){
			if(!CommonUtils.isNull(value)&&!"正常".equals(value)&&!"锁定".equals(value)&&!"停用".equals(value)){
				error=BatchCell.STATUS_FORMAT_ERROR;
			}
			xlsRow.addCell(null,value,"accountStatus",error);
		}
		//第14列为过期时间
		else if(index==13){
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
			xlsRow.addCell(null,value,"expireTime",error);
		}
		//15列位自定义1
		else if(index==14){
			if(!CommonUtils.isNull(value)){
				if(value.length()>255){
					error=BatchCell.STATUS_FORMAT_ERROR;
				}
			}
			xlsRow.addCell(null,value,"custom1",error);
		}
		//第16列为自定义2
		else if(index==15){
			if(!CommonUtils.isNull(value)){
				if(value.length()>255){
					error=BatchCell.STATUS_FORMAT_ERROR;
				}
			}
			xlsRow.addCell(null,value,"custom2",error);
		}
	}
	
}
