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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.duckling.vmt.common.reflect.ReflectUtils;

import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;

/**
 * 默认插入的个人简历，联系方式，首页内容
 * 
 * @author lvly
 * @since 2012-8-20
 */
public final class RenderUtils {
	private RenderUtils() {

	}

	public static final Logger LOG = Logger.getLogger(RenderUtils.class);
	public static final String PATH;
	/*** 已存在用户邮件 */
	public static final String ADD_USER_TEMP;
	/**不存在用户自动创建账户，邮件*/
	public static final String ADD_USER_REGIST_TEMP;
	public static final String APPLY_REGIST;
	public static final String APPLY_REGIST_OK;
	
	public static final String ADD_USER_SAY_PASSWD;
	public static final String APPLY_ADD;
	public static final Pattern REG = Pattern.compile("\\$\\{.+?\\}");
	public static final Pattern REG_FOR = Pattern.compile("^<for items=\"\\$\\{.+?\\}\">$");
	public static final int UP_DOWN_SKIP=32;
	public static final String APPLY_OPEN_DCHAT;
	public static final String APPLY_REGIST_REFUSE;
	public static final String SMS_USE_20P;
	static {
		String path = "";
		URL url = ClassUtils.getDefaultClassLoader().getResource("/");
		if (url != null) {
			path = url.getPath() + "template/";
		}
		PATH = path;
		ADD_USER_TEMP = PATH + "addUser.tmp";
		ADD_USER_REGIST_TEMP=PATH+"addUserNoAccount.tmp";
		APPLY_ADD=PATH+"applyAdd.tmp";
		APPLY_OPEN_DCHAT=PATH+"applyDchatOpen.tmp";
		ADD_USER_SAY_PASSWD =PATH+"addUserSayPasswd.tmp";
		APPLY_REGIST=PATH+"applyRegist.tmp";
		APPLY_REGIST_OK=PATH+"applyRegistOK.tmp";
		APPLY_REGIST_REFUSE=PATH+"applyRegistRefuse.tmp";
		SMS_USE_20P=PATH+"smsUse20Percent.tmp";
	}

	/**
	 * 从模版替换变量，生成的完整的html
	 * 
	 * @param map
	 *            需要替换的参数，for标签引用，请给出List
	 * @param tempFile
	 *            应该是本地变量 XXX_TEMP之中的一个
	 * @return 生成好的HTML
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws
	 * @throws IOException
	 * */
	public static String getHTML(Map<String, Object> map, String tempFile) {
		BufferedReader sr=null;
		try {
			sr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(tempFile)),"utf8")) ;
			StringBuffer sb = new StringBuffer();
			String str = null;
			while ((str = sr.readLine()) != null) {
				// 标签匹配 for
				if(doForReplace(sb,sr,str,map)){
					continue;
				}
				// 普通匹配
				sb.append(replaceMatch(str, map));

			}
			return sb.toString();
		}catch (IOException e) {
			LOG.error(e);
			return e.toString() + "[" + tempFile + "]";
		} catch (NoSuchFieldException e) {
			LOG.error(e);
			return e.toString() + "[" + tempFile + "]";
		}finally{
			if(sr!=null){
				try {
					sr.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		}
	}
	/**
	 * @return need loop
	 * */
	private static boolean doForReplace(StringBuffer sb,BufferedReader sr, String str,Map<String,Object> map) throws NoSuchFieldException, IOException{
		Matcher forMacher = REG_FOR.matcher(str);
		if (forMacher.find()) {
			Matcher matcher = REG.matcher(forMacher.group());
			StringBuffer forStr = new StringBuffer();
			String readLine="";
            while (true) {
                readLine = sr.readLine();
                if (readLine != null && !"</for>".equals(readLine)) {
                    forStr.append(readLine);
                } else {
                    break;
                }
            }
			matcher.find();
			String key = matcher.group();
			if (map == null) {
				return true;
			}
			List<?> list = (List<?>) map.get(key);
			StringBuffer result = new StringBuffer();
			if (list != null) {
				for (Object obj : list) {
					result.append(replaceForItemMatch(forStr.toString(), obj));
				}
			}
			sb.append(result);
			return true;
		}
		return false;
	}

	/**
	 * 普通匹配替换
	 */
	public static String replaceMatch(String line, Map<String, Object> map) {
		if (map == null) {
			return line;
		}
		Matcher matcher = REG.matcher(line);
		String result = line;
		while (matcher.find()) {
			String key = matcher.group();
			result = result.replace(key, (String) map.get(key) == null ? "" : (String) map.get(key));
		}
		return result;
	}

	/**
	 * For标签替换
	 * 
	 * @param 需要替换的line
	 *            xxx${NAME}XXX${ID}之类的
	 * @param t
	 *            javaBean 从里面读取值替换掉
	 * @return 替换好的内容
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * */
	public static <T> String replaceForItemMatch(String line, T t) throws NoSuchFieldException {
		Matcher matcher = REG.matcher(line);
		String result = line;
		while (matcher.find()) {
			String key = matcher.group();
			String fieldName = key.replace("${", "").replace("}", "").trim();
			Object value = getFieldValue(t, fieldName);
			result = result.replace(key, value == null ? "" : value.toString());
		}
		return result;
	}

	private static <T> Object getFieldValue(T t, String fieldName) throws NoSuchFieldException {
		Field field = t.getClass().getDeclaredField(fieldName.trim());
		return ReflectUtils.getValue(t, field.getName());
	}
}
