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

import net.duckling.cloudy.common.CommonUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.log4j.Logger;

/**
 * @date 2011-11-15
 * @author JohnX
 */
public final class PinyinUtils {
	private PinyinUtils() {
	}

	public static final char CH_START = '\u4e00';
	public static final char CH_END = '\u9fa5';
	public static final char SPLIT_CHAR = ';';
	public static final int CH_CODE_VALUE = 128;
	public static final int DB_CHNAME_LEN = 3;

	private static final Logger LOG = Logger.getLogger(PinyinUtils.class);

	/**
	 * 获得拼音，如果不是汉字，则返回原字符串
	 * @param 需要转换成拼音的汉字
	 * @return pinyin;py
	 * */

	public static String getPinyin(String str) {
		if (CommonUtils.isNull(str)) {
			return "";
		}
		HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		StringBuilder sb = new StringBuilder();
		appendPinyin(str, sb, outputFormat);
		return sb.toString();
	}
	
	/**
	 * 将str转换成拼音，并将转化结果插入到sb中
	 * @param str 待转换的中文串(可中英混合)
	 * @param sb 保存结果的StringBuilder对象
	 * @param outputFormat 拼音格式化对象
	 */
	private static void appendPinyin(String str, StringBuilder sb, HanyuPinyinOutputFormat outputFormat){
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch < PinyinUtils.CH_START || ch > PinyinUtils.CH_END) {
				sb.append(ch);
			} else {
				String[] pinyinArray = null;
				try {
					pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, outputFormat);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					LOG.error(e.getMessage());
				}
				if (pinyinArray != null && pinyinArray.length > 0) {
					sb.append(pinyinArray[0]);
				}
			}
		}
	}
}
