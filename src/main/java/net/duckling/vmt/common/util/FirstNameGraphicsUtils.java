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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import net.duckling.cloudy.common.CommonUtils;


public class FirstNameGraphicsUtils {
	public static final Color[] COLOR_ARRAY;
	static{
		COLOR_ARRAY=new Color[]{
				new Color(255,255,0),
				new Color(0,204,204),
				new Color(0,204,0),
				new Color(51,101,204),
				new Color(153,0,204),
				new Color(255,0,0),
				new Color(255,51,102),
				new Color(255,51,0),
				new Color(153,255,0)
		};
	}

	BufferedImage image;
	
	private Color getRandColor() {// 给定范围获得随机颜色
		return COLOR_ARRAY[new Random().nextInt(9)];
	}
	public void generate(String content,OutputStream output) throws FileNotFoundException, IOException{
		if(CommonUtils.isNull(content)){
			content="无";
		}
		// 在内存中创建图象
		int width = 200, height = 200;
		image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		char first=content.toUpperCase().charAt(0);
		// 获取图形上下文
		Graphics g = image.getGraphics();

		// 设定背景色
		g.setColor(getRandColor());
		g.fillRect(0, 0, width, height);

		// 设定字体
		g.setColor(Color.WHITE);
		g.setFont(getFont(first));
		draw(first, g);
		// 图象生效
		g.dispose();

		// 输出图象到页面
		ImageIO.write(image, "png", output);
	}
	public Font getFont(char str){
		if(isLetter(str)){
			int fontSize=230;
			if('Q'==str){
				fontSize=200;
			}else if('W'==str){
				fontSize=200;
			}else if('M'==str){
				fontSize=200;
			}
			return new Font("Heiti SC", Font.PLAIN, fontSize);
		}else if(isNum(str)){
			return new Font("Heiti SC", Font.PLAIN, 220);
		}else{
			return new Font("Heiti SC", Font.PLAIN, 180);
		}
	}
	
	public void draw(char str,Graphics g){
		if(isLetter(str)){
			switch(str){
				case 'B':{
					g.drawString(str+"", 30, 195);
					break;
				}
				case 'C':{
					g.drawString(str+"", 0, 195);
					break;
				}
				case 'E':{
					g.drawString(str+"", 27, 195);
					break;
				}
				case 'F':{
					g.drawString(str+"", 35, 195);
					break;
				}
				case 'G':{
					g.drawString(str+"", 0, 195);
					break;
				}
				case 'I':{
					g.drawString(str+"", 75, 195);
					break;
				}
				case 'J':{
					g.drawString(str+"", 35, 195);
					break;
				}
				case 'L':{
					g.drawString(str+"", 30, 195);
					break;
				}
				case 'O':{
					g.drawString(str+"", 0, 195);
					break;
				}
				case 'P':{
					g.drawString(str+"", 40, 195);
					break;
				}
				case 'M':{
					g.drawString(str+"", 15, 175);
					break;
				}
				case 'Q':{
					g.drawString(str+"", 10, 180);
					break;
				}
				case 'S':{
					g.drawString(str+"", 45, 195);
					break;
				}
				case 'T':{
					g.drawString(str+"", 50, 200);
					break;
				}
				case 'W':{
					g.drawString(str+"", 5, 180);
					break;
				}
				case 'X':{
					g.drawString(str+"",30, 195);
					break;
				}
				case 'Y':{
					g.drawString(str+"", 25, 195);
					break;
				}
				case 'Z':{
					g.drawString(str+"", 35, 200);
					break;
				}
				default:{
					g.drawString(str+"", 20, 195);
					break;
				}
			}
			
		}else if(isNum(str)){
			g.drawString(str+"", 40, 190);
		}else{
			g.drawString(str+"",11,160);
		}
	}
	private boolean isLetter(char str){
		return (str>='a'&&str<='z')||(str>='A'&&str<='Z');
	}
	private boolean isNum(char str){
		return str>='0'&&str<='9';
	}
	
}
