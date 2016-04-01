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
package net.duckling.vmt.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.util.ImageUtils;
import net.duckling.vmt.service.CLBResizeparamFactory;
import net.duckling.vmt.service.IAttributeService;
import net.duckling.vmt.service.IClbService;
import net.duckling.vmt.service.impl.FileSaverBridge;
import net.duckling.vmt.service.mq.MQMessageSenderExt;
import net.duckling.vmt.web.helper.JSONHelper;

import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author lvly
 * @since 2013-8-9
 */
@Controller
@RequestMapping("/logo")
public class LogoController {
	private static final Logger LOGGER=Logger.getLogger(LogoController.class);
	@Autowired
	private IClbService clbService;
	@Autowired
	private IAttributeService attrService;
	@Autowired
	private MQMessageSenderExt sender;
	
	@RequestMapping("/save")
	public RedirectView save(@RequestParam("logoTeamDN")String dn,@RequestParam("logoId") int clbId,@RequestParam("uploadImgType")String type){
		String attrName="vmt-logo";
		if("mobile".equals(type)){
			attrName="vmt-mobile-logo";
		}else if("pc".equals(type)){
			attrName="vmt-pc-logo";
		}
		attrService.update(dn, attrName, clbId);
		sender.sendUpdateMessage(dn);
		return new RedirectView("../user/setting");
	}
	
	@RequestMapping("/{logoId}")
	public void readImg(@PathVariable("logoId") int fileId,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		if (fileId<=0) {
            String defaultLogo=request.getParameter("default");
            if(CommonUtils.isNull(defaultLogo)){
            	defaultLogo="defaultApp.png";
            }
            request.getRequestDispatcher("../resource/images/"+defaultLogo).forward(request, response);
        }else{
        	String size=IClbService.latest;
    		String requestSize=request.getParameter("size");
    		if(IClbService.large.equals(requestSize)||IClbService.medium.equals(requestSize)||IClbService.small.equals(requestSize)){
    			size=requestSize;
    		}
    		
    		clbService.downloadLogo(fileId, new FileSaverBridge(response,request),size);
        }
	}
	/***
	 * FireFox上传文件
	 * */
	@RequestMapping(value="/upload",method = RequestMethod.POST,headers = { "X-File-Name" })
	@ResponseBody
	public UploadResult uploadXls(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader("X-File-Name") String fileName){
		try {
			String type=request.getParameter("type");
		    switch (type){
		    case "common":{
		    	return new UploadResult(clbService.upload(getCutting(request.getInputStream()), fileName));
		    }
		    case "mobile":{
		    	File tmpFile=getCopy(request.getInputStream());
		    	InputStream cIns=new FileInputStream(tmpFile);
		    	BufferedImage src = ImageIO.read(cIns);
		    	if(src.getWidth()!=194||src.getHeight()!=194){
		    		return new UploadResult("请上传194*194大小的图片");
		    	}
		    	return new UploadResult(clbService.upload(new FileInputStream(tmpFile), fileName));
		    }
		    case "pc":{
		    	File tmpFile=getCopy(request.getInputStream());
		    	InputStream cIns=new FileInputStream(tmpFile);
		    	BufferedImage src = ImageIO.read(cIns);
		    	if(src.getWidth()!=145||src.getHeight()!=156){
		    		return new UploadResult("请上传145*156大小的图片");
		    	}
		    	return new UploadResult(clbService.upload(new FileInputStream(tmpFile), fileName));
		    }
		    default:{
		    	return new UploadResult(clbService.upload(getCutting(request.getInputStream()), fileName,CLBResizeparamFactory.getAppForDchatResizeParam()));
		    }
		    
		    }
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			return new UploadResult(0);
		}
	}
	private File getCopy(InputStream ins)throws IOException{
		File f=File.createTempFile(System.currentTimeMillis()+".", "tmp");
		FileOutputStream os=new FileOutputStream(f);
		StreamUtils.copy(ins, os);
		os.flush();
		os.close();
		return f;
	}
	private InputStream getCutting(InputStream ins)throws IOException{
		File cuted=ImageUtils.defaultCut(getCopy(ins));
		return new FileInputStream(cuted);
		
	}
	class UploadResult{
		private boolean success;
		private String message;
		private int clbId;
		public boolean isSuccess() {
			return success;
		}
		public UploadResult(int clbId){
			if(clbId>0){
				success=true;
				this.clbId=clbId;
			}
		}
		public UploadResult(String message){
			this.message=message;
		}
		
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public void setSuccess(boolean success) {
			this.success = success;
		}
		public int getClbId() {
			return clbId;
		}
		public void setClbId(int clbId) {
			this.clbId = clbId;
		}
		public JSONObject toJSON(){
			JSONObject obj=new JSONObject();
			obj.put("success", this.success);
			obj.put("clbId", this.clbId);
			obj.put("message", this.message);
			return obj;
		}
	}
	/***
	 * IE上传文件
	 * */
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody
	public void uploadXls(
			@RequestParam("qqfile") MultipartFile uplFile,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		//IE兼容，不这么整，会下载json
		try{
			JSONHelper.writeJSONObject(response, new UploadResult(clbService.upload(getCutting(uplFile.getInputStream()), uplFile.getOriginalFilename())).toJSON());
		}catch(Exception e){
			LOGGER.error("",e);
			JSONHelper.writeJSONObject(response, new UploadResult(0).toJSON());
		}
		
	}
	

}
