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

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Random;

import net.duckling.cloudy.common.CommonUtils;

/**
 * 字符串处理工具类
 * @author lvly
 * @since 2013-4-26
 */
/**
 * @author lvly
 * @since 2013-5-27
 */
public final class CharUtils {
	private static final Random RANDOM=new Random();
	private static final int DEFAULT_LENGTH=8;
	private static final String[] RANDOM_ARRAY={
		"a","b","c","d","e",
		"f","g","h","i","j",
		"k","l","m","n","o",
		"p","q","r","s","t",
		"u","v","w","x","y",
		"z","A","B","C","D",
		"E","F","G","H","I",
		"J","K","L","M","N",
		"O","P","Q","R","S",
		"T","U","V","W","X",
		"Y","Z","1","2","3",
		"4","5","6","7","8",
		"9","0"}; 
	
	/**
	 * 获得随机的字符串
	 * @param length 格式化字符串长度
	 * @return 生成的字符创
	 */
	public static String random(int length){
		int arrayLength=RANDOM_ARRAY.length;
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<length;i++){
			sb.append(RANDOM_ARRAY[(RANDOM.nextInt(arrayLength))]);
		}
		return sb.toString();
	}
	
	/**
	 * 获得随机生成的字符串，默认使用8位
	 * @return 随机生成的字符串
	 */
	public static String random(){
		return random(DEFAULT_LENGTH);
	}
	
	private CharUtils(){}
	
	/**
	 * 判断一个字符串第一个字符是否是字母
	 * @param str
	 * @return
	 */
	public static boolean isFirstCharIsLetter(String str){
		if(CommonUtils.isNull(str)){
			return false;
		}
		char c=str.charAt(0);
		return ((c>='a'&&c<='z')||(c>='A'&&c<='Z'));
	}
	public static final Comparator<String> getPinyinComparator(){
		return new Comparator<String>(){
			@Override
			public int compare(String s1, String s2) {
				return  Collator.getInstance(Locale.CHINESE).compare(s1, s2); 
			}
		};
	} 

}
