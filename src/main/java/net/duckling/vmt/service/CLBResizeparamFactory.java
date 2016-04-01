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
package net.duckling.vmt.service;

import cn.vlabs.clb.api.image.ResizeParam;

/**
 * @author lvly
 * @since 2013-8-15
 */
public class CLBResizeparamFactory {
	public static ResizeParam getCommonResizeParam(){
		ResizeParam param=new ResizeParam();
		param.setSmallPoint(100);
		param.setMediumPoint(200);
		param.setLargePoint(400);
		param.setUseWidthOrHeight(ResizeParam.USE_HEIGHT);
		return param;
	}
	public static ResizeParam getAppForDchatResizeParam(){
		ResizeParam param=new ResizeParam();
		param.setSmallPoint(20);
		param.setMediumPoint(32);
		param.setLargePoint(64);
		param.setUseWidthOrHeight(ResizeParam.USE_HEIGHT);
		return param;
	}
}
