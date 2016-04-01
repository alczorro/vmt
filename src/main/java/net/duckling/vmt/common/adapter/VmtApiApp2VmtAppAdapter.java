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
package net.duckling.vmt.common.adapter;

import java.util.ArrayList;
import java.util.List;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.api.domain.VmtApiApp;
import net.duckling.vmt.common.config.GlobalConfig;
import net.duckling.vmt.domain.VmtApp;

import org.springframework.beans.BeanUtils;

public class VmtApiApp2VmtAppAdapter {
	public static List<VmtApiApp> convert(List<VmtApp> apps){
		if(CommonUtils.isNull(apps)){
			return new ArrayList<VmtApiApp>();
		}
		List<VmtApiApp> result=new ArrayList<VmtApiApp>();
		for(VmtApp app:apps){
			VmtApiApp apiApp=new VmtApiApp();
			BeanUtils.copyProperties(app,apiApp);
			fixAbsoluteUrl(apiApp);
			result.add(apiApp);
		}
		return result;
	}
	public static void fixAbsoluteUrl(VmtApiApp app){
		String passport=GlobalConfig.get("duckling.umt.site");
		String my=GlobalConfig.get("my.base.url");
		String prefix=null;
		if(VmtApp.APP_TYPE_LOCAL.equals(app.getAppType())){
			prefix=my+"/logo/";
		}else{
			prefix=passport;
		}
		if(!CommonUtils.isNull(app.getLogo100Url())){
			app.setLogo100Url(prefix+app.getLogo100Url());
		}
		if(!CommonUtils.isNull(app.getLogo64Url())){
			app.setLogo64Url(prefix+app.getLogo64Url());
		}
		if(!CommonUtils.isNull(app.getLogo32Url())){
			app.setLogo32Url(prefix+app.getLogo32Url());
		}
		if(!CommonUtils.isNull(app.getLogo16Url())){
			app.setLogo16Url(prefix+app.getLogo16Url());
		}
		if(!CommonUtils.isNull(app.getLogoCustomUrl())){
			app.setLogoCustomUrl(prefix+app.getLogoCustomUrl());
		}
	}
}
