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
package net.duckling.vmt.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import net.duckling.cloudy.common.UrlUtils;
import net.duckling.common.util.CommonUtils;
import net.duckling.vmt.common.priv.SecurityLevel;
import net.duckling.vmt.common.priv.SecurityMapping;
import net.duckling.vmt.common.util.FirstNameGraphicsUtils;
import net.duckling.vmt.common.util.LdapUtils;
import net.duckling.vmt.domain.JsonResult;
import net.duckling.vmt.domain.VmtApp;
import net.duckling.vmt.service.CLBResizeparamFactory;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IClbService;
import net.duckling.vmt.service.IVmtAppService;
import net.duckling.vmt.service.IVmtAppSwitchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.vlabs.duckling.api.umt.rmi.oauth.OauthClient;
import cn.vlabs.duckling.api.umt.rmi.oauth.OauthService;

@Controller
@RequestMapping("/user/app")
@SecurityMapping(level=SecurityLevel.ADMIN)
public class AppManageController {
	
	@Autowired
	private IVmtAppService appService;
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private OauthService oauthService;
	@Autowired
	private IVmtAppSwitchService vmtAppSwitchService;
	@Autowired
	private IClbService clbService;
	/**
	 * 单个增加的是自己的应用
	 * @throws IOException 
	 * */
	@RequestMapping("add")
	@ResponseBody
	public JsonResult add(@RequestParam("dn")String dn,VmtApp app) throws IOException{
		String decodeDN=LdapUtils.decode(dn);
		if(VmtApp.APP_TYPE_OAUTH.equals(app.getAppType())&&appService.isOauthAppAdded(app.getAppClientId(), decodeDN)){
			return new JsonResult("此应用已添加");
		}
		if(CommonUtils.isNull(app.getAppClientUrl())||CommonUtils.isNull(app.getAppName())){
			return new JsonResult("缺乏必要的参数");
		}
		generateDefaultLogo(app);
		appService.addApp(app);
		attrService.update(decodeDN,"vmt-apps-copy" ,VmtApp.toJson(appService.searchAppByTeamDn(decodeDN)));
		return new JsonResult();
	}
	private void generateDefaultLogo(VmtApp app) throws IOException{
		if(CommonUtils.isNull(app.getLogo100Url())){
			FirstNameGraphicsUtils graphics=new FirstNameGraphicsUtils();
			File tmpFile=File.createTempFile(System.currentTimeMillis()+".fn.", ".png");
			graphics.generate(app.getAppName(),new FileOutputStream(tmpFile));
			int clbId=clbService.upload(new FileInputStream(tmpFile), tmpFile.getName(), CLBResizeparamFactory.getAppForDchatResizeParam());
			app.setLogo100Url(clbId+"");
		}
		
		app.setLogo64Url(UrlUtils.addParam(app.getLogo100Url(), "size", IClbService.large));
		app.setLogo32Url(UrlUtils.addParam(app.getLogo100Url(), "size", IClbService.medium));
		app.setLogoCustomUrl(UrlUtils.addParam(app.getLogo100Url(), "size", IClbService.small));
	}
	@RequestMapping("update/{appId}")
	@ResponseBody
	public JsonResult update(@PathVariable("appId")int appId,@RequestParam("dn")String dn,VmtApp app) throws IOException{
		String decodeDN=LdapUtils.decode(dn);
		if(VmtApp.APP_TYPE_OAUTH.equals(app.getAppType())){
			return new JsonResult("此应用无法修改");
		}
		if(!appService.checkAppIdAndDn(decodeDN, appId)){
			return new JsonResult("没有权限");
		}
		if(CommonUtils.isNull(app.getAppClientUrl())||CommonUtils.isNull(app.getAppName())){
			return new JsonResult("缺乏必要的参数");
		}
		app.setId(appId);
		generateDefaultLogo(app);
		appService.updateApp(app);
		
		attrService.update(decodeDN,"vmt-apps-copy" ,VmtApp.toJson(appService.searchAppByTeamDn(decodeDN)));
		return new JsonResult();
	}
	@RequestMapping("delete")
	@ResponseBody
	public JsonResult delete (@RequestParam("dn")String dn,@RequestParam("appId")int appId){
		String decodeDN=LdapUtils.decode(dn);
		if(!appService.checkAppIdAndDn(decodeDN, appId)){
			return new JsonResult("没有权限");
		}
		VmtApp app=new VmtApp();
		app.setId(appId);
		appService.deleteApp(app);
		attrService.update(decodeDN,"vmt-apps-copy" ,VmtApp.toJson(appService.searchAppByTeamDn(decodeDN)));
		return new JsonResult();
	}
	@RequestMapping("search")
	@ResponseBody
	public JsonResult search(@RequestParam("keyword")String keyword,@RequestParam("offset")int offset,@RequestParam("size")int size){
		String keyWord=CommonUtils.killNull(keyword).trim();
		List<OauthClient> list=oauthService.searchByKeyword(keyWord,offset,size);
		JsonResult result=new JsonResult();
		result.setData(list);
		return result;
	}
	@RequestMapping("{appId}")
	@ResponseBody
	public JsonResult getAppByID(@RequestParam("dn")String dn,@PathVariable("appId")int appId){
		String decodeDN=LdapUtils.decode(dn);
		if(!appService.checkAppIdAndDn(decodeDN, appId)){
			return new JsonResult("没有权限");
		}
		JsonResult result=new JsonResult();
		result.setData(appService.getAppById(appId));
		return result;
	}
	@RequestMapping("switch")
	@ResponseBody
	public JsonResult getDn(@RequestParam("dn")String dn,@RequestParam("isOpen")boolean isOpen){
		String decodeDN=LdapUtils.decode(dn);
		attrService.update(decodeDN,"vmt-is-apps-open" ,isOpen);
		if(isOpen){
			vmtAppSwitchService.ifNotExitsThenInsert(decodeDN);
		}else{
			vmtAppSwitchService.delete(decodeDN);
		}
		return new JsonResult();
	}
}
