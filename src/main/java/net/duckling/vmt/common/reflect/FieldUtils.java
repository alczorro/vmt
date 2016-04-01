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
package net.duckling.vmt.common.reflect;

import java.lang.reflect.Field;

/**反射工具类，主要判断成员变量
 * @author lvly
 * @since 2013-4-27
 */
public final class FieldUtils {
	private FieldUtils(){}
	
    /**
     * 成员变量是否是String类型
     * @param field
     * @return
     */
    public static  boolean isString(Field field) {
        return typeEquals(field,String.class);
    }
    /**
     * 成员变量是否是String数组类型
     * @param field
     * @return
     */
    public static boolean isStringArray(Field field){
    	return typeEquals(field,String[].class);
    }
    /**
     * 成员变量是否是Int类型
     * @param field
     * @return
     */
    public static boolean isInt(Field field) {
        return typeEquals(field,int.class) || typeEquals(field,Integer.class);
    }
    
    public static boolean isLong(Field field){
    	 return typeEquals(field,long.class) || typeEquals(field,Long.class);
    }
    /**
     * 成员变量是否是int数组类型
     * @param field
     * @return
     */
    public static boolean isIntArray(Field field) {
        return typeEquals(field,int[].class) || typeEquals(field,Integer[].class);
    }
    /**
     * 成员变量是否是boolean类型
     * @param field
     * @return
     */
    public static boolean isBoolean(Field field) {
        return typeEquals(field,boolean.class) ||typeEquals(field,Boolean.class);
    }
    /**
     * 成员变量是否是boolean类型
     * @param field
     * @return
     */
    public static boolean isBooleanArray(Field field) {
        return typeEquals(field,boolean[].class) ||typeEquals(field,Boolean[].class);
    }
    private static boolean typeEquals(Field field,Class<?> classObj){
    	return field.getType().equals(classObj);
    }
}
