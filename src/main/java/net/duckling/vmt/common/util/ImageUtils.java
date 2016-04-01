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
package net.duckling.vmt.common.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import net.duckling.vmt.domain.Rect;

import org.apache.log4j.Logger;

/**
 * 图片剪切， 缩放等工具类
 * 
 * @author lvly
 * @since 2012-08-09
 */
public final class ImageUtils {

	private ImageUtils() {
	}

	private static final Logger LOG = Logger.getLogger(ImageUtils.class);
	public static final String PATH = File.separator + "resources" + File.separator + "temp" + File.separator;
	public static final String CUT_PREFIX = "_cut";
	public static final String SCALE_PREFIX = "_scale";
	public static final String SCALE_ORG_PREFIX = "_orgscale";

	public static final int DEFAULT_SCALE_WIDTH = 400;
	public static final int DEFAULT_SCALE_HEIGHT = 400;
	
	public static final int DEFAULT_CUT_WIDTH = 200;
	public static final int DEFAULT_CUT_HEIGHT = 200;

	public static final String TYPE_IMG_GIF = "gif";
	public static final String TYPE_IMG_JPG = "jpg";
	public static final String TYPE_IMG_PNG = "png";
	public static final String TYPE_IMG_UNKOWN = "jpg";
	/**
	 * 默认裁剪图片
	 * */
	public static File defaultCut(File file){
		File result=null;
		try {
			BufferedImage src = ImageIO.read(file);
			int width=src.getWidth();
			int height=src.getHeight();
			
			int x=0;
			int y=0;
			
			if(width<DEFAULT_CUT_WIDTH&&height<DEFAULT_CUT_HEIGHT){
				result= cutting(file,0,0,width,height,1);
			}
			else if(width>height){
				x=(width-height)/2;
				width=height;
				result= cutting(file, x, y, width, height, 1);
			}else if(width<height){
				y=(height-width)/2;
				height=width;
				result= cutting(file,x,y,width,height,1);
			}else{
				result= cutting(file,0,0,width,height,1);
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		} 
		return scale(result);
	} 

	/**
	 * 剪切图片
	 * 
	 * @param file
	 *            需要剪切的文件
	 * @param x
	 *            剪切点的x坐标
	 * @param y
	 *            剪切点的y坐标
	 * @param width
	 *            剪切宽度
	 * @param height
	 *            剪切高度
	 * @param scale
	 *            很明显，大于1就是放大，小于1就是缩小
	 * @return
	 */
	public static File cutting(File file, int x, int y, int width, int height, float scale) {
		int xOrg = Math.round((x / scale));
		int yOrg = Math.round((y / scale));
		int widthOrg = Math.round((width / scale));
		int heightOrg = Math.round((height / scale));
		File newFile = null;
		InputStream is = null;
		ImageInputStream iis = null;
		ImageOutputStream out = null;
		try {
			is = new FileInputStream(file);
			iis = ImageIO.createImageInputStream(is);
			String endName = getType(file);
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(endName);
			ImageReader reader = readers.next();
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			param.setSourceRegion(new Rectangle(xOrg, yOrg, widthOrg, heightOrg));
			BufferedImage bi = reader.read(0, param);
			newFile =File.createTempFile(System.nanoTime()+"", ".end");
			out = ImageIO.createImageOutputStream(new FileOutputStream(newFile));
			ImageIO.write(bi, reader.getFormatName(), out);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {if (iis != null) {iis.close();}
				 if (is != null) {is.close();}
				 if (out != null) {out.close();}
			} catch (IOException e) {
				LOG.error(e);
			}
		}
		return newFile;
	}

	/** 获取暂存源文件 */
	/**
	 * @param cutPath
	 *            xxxx/xxxx/xx/xx.jpg_cut
	 * @return xxx/xxx/xx/xx.jpg
	 */
	public static String getOrgPath(String finalPath) {
		return finalPath.replace(CUT_PREFIX, "").replace(SCALE_PREFIX, "").replace(SCALE_ORG_PREFIX, "");
	}

	/** 获取暂存文件 */
	/**
	 * @param cutPath
	 *            xxxx/xxxx/xx/xx.jpg
	 * @return xxx/xxx/xx/xx.jpg_cut
	 */
	public static String getCutPath(String orgPath) {
		return getOrgPath(orgPath) + CUT_PREFIX;
	}

	/** 获取缩放文件地址 */
	/**
	 * @param orgPath
	 *            一般为裁剪完后的图片,xxx.jpg_cut_scale
	 * @return 返回缩放地址
	 */
	public static String getScalePath(String orgPath) {
		return getOrgPath(orgPath) + SCALE_PREFIX;

	}

	/**
	 * 把cut图删掉时候，把原图也删掉
	 * 
	 * @param file
	 *            最后生成的scale图
	 * */
	public static void delete(File fileScale) {
		File fileCut = new File(getCutPath(fileScale.getAbsolutePath()));
		File fileOrg = new File(getOrgPath(fileScale.getAbsolutePath()));
		fileScale.deleteOnExit();
		fileOrg.deleteOnExit();
		fileCut.deleteOnExit();

	}

	/**
	 * 缩放图像
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @return 返回缩放后的图片
	 */
	public static File scale(File srcImageFile) {
		return scale(srcImageFile, DEFAULT_SCALE_WIDTH, DEFAULT_SCALE_HEIGHT);
	}

	/**
	 * 缩放图像
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @return 返回缩放后的图片
	 */
	public static File scale(File srcImageFile, int defaultWidth, int defaultHeight) {
		try {
			BufferedImage src = ImageIO.read(srcImageFile); // 读入文件
			int width = src.getWidth();
			int height = src.getHeight();
			if (width > defaultWidth || height > defaultHeight) {
				Rect r = proportation(width, height, defaultWidth, defaultHeight);
				width = r.getWidth();
				height = r.getHeight();
			} else {
				return srcImageFile;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			File resultFile = File.createTempFile(System.nanoTime()+"", ".scale");
			ImageIO.write(tag, "JPEG", resultFile);// 输出到文件流
			return resultFile;
		} catch (IOException e) {
			LOG.error(e);
		}
		return null;
	}

	/**
	 * 填满原则，获得一个矩形实例
	 * 
	 * @param width
	 *            物体宽
	 * @param height
	 *            物体高
	 * @param maxWidth
	 *            容器宽
	 * @param maxHeight
	 *            容器高
	 * @return
	 */
	public static Rect proportation(int width, int height, int maxWidth, int maxHeight) {
		// 原图的高宽比例
		float proportion = height / (float) width;
		// 框的高宽比例
		float needProportation = maxHeight / (float) maxWidth;
		Rect r = new Rect();
		// 说明物体偏高了
		if (proportion > needProportation) {
			r.setHeight(maxHeight);
			r.setWidth((int) (maxHeight / proportion));
		}
		// 说明物体偏宽了
		else if (proportion < needProportation) {
			r.setHeight((int) (maxWidth * proportion));
			r.setWidth(maxWidth);
		} else {// 长和宽一样
			r.setHeight(maxHeight);
			r.setWidth(maxWidth);
		}
		return r;

	}
	private static String toStringBuffer(byte[] bytes,int...index){
		StringBuffer sb=new StringBuffer();
		for(int i:index){
			sb.append((char)bytes[i]);
		}
		return sb.toString().toUpperCase();
	}

	private static String getTypeFromLByte(byte[] b){
		if ("GIF".equals(toStringBuffer(b,0,1,2))){
			return TYPE_IMG_GIF;
		}else if ("PNG".equals(toStringBuffer(b,1,2,3))){
			return TYPE_IMG_PNG;  
		}else if ("JFIF".equals(toStringBuffer(b,6,7,8,9))){
			 return TYPE_IMG_JPG;
		}else if ("EXIF".equals(toStringBuffer(b,6,7,8,9))){
			 return TYPE_IMG_JPG;
		}else{
			return TYPE_IMG_JPG; 
			}
	}

	/***
	 * 根据文件头信息，判断图片格式
	 * 
	 * @param file
	 *            文件(img)
	 * */
	public static String getType(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] b = new byte[10];
			int byteNum = fis.read(b);
			if (byteNum < 10) {
				return TYPE_IMG_UNKOWN;
			} else {
				return getTypeFromLByte(b);
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			return TYPE_IMG_UNKOWN;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		}
	}
}
