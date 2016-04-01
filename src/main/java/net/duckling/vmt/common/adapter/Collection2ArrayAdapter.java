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
import java.util.Set;

import net.duckling.cloudy.common.ReflectUtils;
import net.duckling.common.util.CommonUtils;

/**
 * 数组和队列转换器
 * 
 * @author lvly
 * @since 2013-05-29
 * 
 */
public final class Collection2ArrayAdapter {
	private Collection2ArrayAdapter(){
	}
	/**
	 * 把List对象转换成数组
	 * @param list
	 * @return
	 */
	public static String[] convert(List<String> list) {
		if (CommonUtils.isNull(list)) {
			return new String[] {};
		}
		String[] result = new String[list.size()];
		int index = 0;
		for (String str : list) {
			result[index++] = str;
		}
		return result;
	}
	public static int[] convertInt(List<Integer> list){
		if (CommonUtils.isNull(list)) {
			return new int[] {};
		}
		int[] result = new int[list.size()];
		int index = 0;
		for (int str : list) {
			result[index++] = str;
		}
		return result;
	}
	/**
	 * 把队列里的某个成员变量返回出来
	 * @param list
	 * @param fieldName
	 * @return
	 */
	public static<T> String[] convertField(List<T> list,String fieldName) {
		if (CommonUtils.isNull(list)) {
			return new String[] {};
		}
		String[] result = new String[list.size()];
		int index = 0;
		for (T t : list) {
			Object obj=ReflectUtils.getValue(t, fieldName);
			result[index++] = obj==null?null:obj.toString();
		}
		return result;
	}
	/**
	 * 把set对象转换成list对象，如果还需转换，请使用convert(List<String> list) 转换，或者，扩展方法
	 * @param set
	 * @return
	 */
	public static <T> List<T> convert(Set<T> set){
		List<T> list=new ArrayList<T>();
		if(set==null){
			return list;
		}
		for(T t:set){
			list.add(t);
		}
		return list;
	}
}
