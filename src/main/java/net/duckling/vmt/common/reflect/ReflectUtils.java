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
package net.duckling.vmt.common.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/***
 * 反射工具类
 * 
 * @author lvly
 * @since 2012-11-6
 * */
public final class ReflectUtils {
	private ReflectUtils(){}
	private static final Logger LOG=Logger.getLogger(ReflectUtils.class);
	/**
	 * 设置某个属性
	 * @param obj 待设置属性的对象
	 * @param fieldName 要设置的属性名称
	 * @param value 要设进去的值
	 * */
	public static <T> boolean setValue(T obj, String fieldName, Object value) {
		if (obj == null) {
			return false;
		}
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (ReflectiveOperationException e) {
			Method method;
			try {
				method = obj.getClass().getMethod(getSetMethodName(fieldName),obj.getClass());
				method.invoke(obj,value);
			} catch (ReflectiveOperationException  e1) {
				LOG.error(e1);
				return false;
			}
		}
		return true;

	}
	/**
	 * 获取某个属性的值
	 * @param obj 待获取值的对象
	 * @param fieldName 属性名称
	 * */
	public static Object getValue(Object obj, String fieldName) {
		if (obj == null) {
			return null;
		}
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (ReflectiveOperationException e) {
			Method method;
			try {
				method = obj.getClass().getMethod(getGetMethodName(fieldName));
				obj.getClass().cast(obj);
				return method.invoke(obj);
			} catch (ReflectiveOperationException  e1) {
				LOG.error(e1.getMessage(),e1);
			}
			return null;
		}
	}

	private static String getGetMethodName(String fieldName) {
		return "get" + fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);
	}

	private static String getSetMethodName(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}


}
