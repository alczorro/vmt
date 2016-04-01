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
package net.duckling.vmt.common.ldap;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.vmt.common.reflect.FieldUtils;
import net.duckling.vmt.common.reflect.ReflectUtils;
import net.duckling.vmt.domain.KEY;
import net.duckling.vmt.exception.LdapParseException;

import org.apache.log4j.Logger;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;

/**
 * 解析LdapMapping的类
 * 
 * @author lvly
 * @since 2013-5-27
 * @param <T>
 *            如果某个对象是Ldap相对应的Bean 请传这个类的泛型
 */
public class LdapMappingResolver<T> {
	private static final Logger LOGGER = Logger.getLogger(LdapMappingResolver.class);
	/**
	 * UserPassword字段,可以写死，Ldap就这么写的，不可能有变化
	 * */
	private static final String PASSWORD = "userPassword";
	/**
	 * 缓存，不用每次都解析field超级累
	 * */
	private static final Map<Class<?>, List<FieldAnnotation>> MAP = new HashMap<Class<?>, List<FieldAnnotation>>();
	/**
	 * Rdn相对应的字段
	 * */
	private FieldAnnotation rdnField;
	/**
	 * 构造传进来的class对象
	 * */
	private Class<T> classObj;

	/**
	 * 构造方法，需传递class对象，
	 * 
	 * @param classObj
	 *            需要解析对应的class对象，靠泛型获取不到class
	 */
	public LdapMappingResolver(Class<T> classObj) {
		this.classObj = classObj;
		buildFieldAnnotations();
	}

	/**
	 * 从对象里读取rdn，加到pdn，拼成一个最终的dn
	 * 
	 * @param obj
	 *            数据对象
	 * @param pdn
	 *            父节点的dn
	 * @return
	 */
	public Name getDN(T obj, String pdn) {
		DistinguishedName name = new DistinguishedName(pdn);
		name.add(getRdnKey(), getRdnValue(obj));
		return name;
	}

	/**
	 * 提供rdn的value,加到pdn ，拼成dn
	 * 
	 * @param rdnValue
	 *            rdn应有的值
	 * @param pdn
	 *            父节点的dn
	 * @return
	 */
	public Name getDN(String rdnValue, String pdn) {
		DistinguishedName name = new DistinguishedName(pdn);
		name.add(getRdnKey(), rdnValue);
		return name;
	}

	/**
	 * 从数据对象里面获取rdn的值，需要在相应的Filed用LdapMapping.type说明
	 * 
	 * @param obj
	 *            数据对象
	 * @return rdn 的值
	 * */
	public String getRdnValue(T obj) {
		Object value = ReflectUtils.getValue(obj, rdnField.field.getName());
		if (value == null) {
			throw new LdapParseException("the rdn value is null!");
		}
		return value.toString();
	}

	/**
	 * 从数据对象里面反射出来，rdn的key是什么
	 * 
	 * @return
	 */
	public String getRdnKey() {
		return rdnField.ldapAnno.value();
	}

	/**
	 * 从数据对象转换成Ldap需要的制定数据对象
	 * 
	 * @param obj
	 *            数据对象
	 * @param dn
	 *            dn
	 * @return insert数据的时候，Spring Ldap 需要的数据格式
	 */
	public DirContextOperations build(T obj, String dn) {
		return build(obj, new DistinguishedName(dn));
	}

	/**
	 * 从数据对象转换成Ldap需要的制定数据对象
	 * 
	 * @param obj
	 *            数据对象
	 * @param baseDN
	 *            需要把对象加到哪里去？
	 * @return insert数据的时候，Spring Ldap 需要的数据格式
	 */
	public DirContextOperations build(T obj, Name baseDN) {
		List<FieldAnnotation> annos = getField();
		LdapMapping objectClasses = classObj.getAnnotation(LdapMapping.class);

		if (objectClasses == null) {
			throw new LdapParseException("this bean " + classObj + " is not defined objectclass!!!");
		}
		DirContextAdapter result = new DirContextAdapter(baseDN);
		result.setAttributeValues(LdapObjectClass.OBJECTCLASS, objectClasses.objectClass());
		FieldAnnotation dnField=null;
		for (FieldAnnotation anno : annos) {
			if (LdapType.RDN.equals(anno.ldapAnno.type())) {
				DistinguishedName name = new DistinguishedName(baseDN);
				name.add(getRdnKey(), getRdnValue(obj));
				result.setDn(name);
				continue;
			}
			if (LdapType.DN.equals(anno.ldapAnno.type())) {
				dnField=anno;
				continue;
			}
			Object value = ReflectUtils.getValue(obj, anno.field.getName());
			if (value instanceof String&&CommonUtils.isNull((String)value)) {
				continue;
			}else if(value==null){
				continue;
			}
			setAttribute(value,anno,result);
		}
		if(dnField==null){
			throw new LdapParseException("the dn is null;please check");
		}
		ReflectUtils.setValue(obj, dnField.field.getName(), result.getDn().toString());
		return result;
	}
	private void setAttribute(Object value,FieldAnnotation anno,DirContextAdapter result){
		if (value.getClass().isArray()) {
			Object[] arrayValue = removeNull((Object[]) value);
			if (arrayValue.length != 0) {
				result.setAttributeValues(anno.ldapAnno.value(), (Object[]) arrayValue);
			}

		} else if (LdapType.PASSWORD.equals(anno.ldapAnno.type())) {
			result.setAttributeValue(PASSWORD, value);
		} else {
			result.setAttributeValue(anno.ldapAnno.value(), CommonUtils.trim(value.toString()));
		}
	}

	private Object[] removeNull(Object[] objs) {
		List<Object> result = new ArrayList<Object>();
		for (Object obj : objs) {
			if (obj == null) {
				continue;
			}
			result.add(obj);
		}
		Object[] objResult = new Object[result.size()];
		int index = 0;
		for (Object obj : result) {
			objResult[index++] = obj;
		}
		return objResult;
	}

	/**
	 * 直接索取跟jdbc里面的rowmapping一样的对象
	 * 
	 * @return
	 */
	public ContextMapper getContextMapper() {
		return new ContextMapper() {
			@Override
			public Object mapFromContext(Object arg0) {
				DirContextAdapter adap = (DirContextAdapter) arg0;
				return convert(adap);
			}

		};
	}

	/**
	 * 把attributes数据格式，转换成内部的数据封装类,可以想象为从ResultSet转换成Object
	 * 
	 * @param context
	 * @return
	 */
	public T convert(DirContextAdapter context) {
		List<FieldAnnotation> annos = getField();
		T t = null;
		try {
			t = classObj.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new LdapParseException("this object[" + classObj + "] is not a no arguments constructor", e);
		}
		Attributes attrs = context.getAttributes();
		for (FieldAnnotation anno : annos) {
			String fieldName = anno.field.getName();
			switch (anno.ldapAnno.type()) {
			case DN: {
				ReflectUtils.setValue(t, fieldName, context.getDn().toString());
				break;
			}
			case PASSWORD:{
				Attribute attr = attrs.get(PASSWORD);
				try {
					if(attr!=null){
						ReflectUtils.setValue(t, fieldName, new String((byte[]) attr.get(), KEY.GLOBAL_ENCODE));
					}
				} catch (NamingException | UnsupportedEncodingException e) {
					LOGGER.error(e.getMessage(), e);
				}
				break;
			}
			default: {
				mapping(t, attrs, fieldName, anno);
				break;
			}
			}

		}
		return t;
	}

	private void mapping(T t, Attributes attrs, String fieldName, FieldAnnotation anno) {
		try {
			Attribute info = attrs.get(anno.ldapAnno.value());
			if (info == null) {
				return;
			}
			if (FieldUtils.isInt(anno.field)) {
				ReflectUtils.setValue(t, fieldName, Integer.parseInt(info.get().toString()));
			} else if (FieldUtils.isString(anno.field)) {
				ReflectUtils.setValue(t, fieldName, info.get().toString());
			} else if(FieldUtils.isLong(anno.field)){
				ReflectUtils.setValue(t, fieldName, Long.parseLong(info.get().toString()));
			}else if (FieldUtils.isBoolean(anno.field)) {
				ReflectUtils.setValue(t, fieldName, Boolean.parseBoolean(info.get().toString()));
			} else if (FieldUtils.isIntArray(anno.field)) {
				ReflectUtils.setValue(t, fieldName, buildIntArray(info));
			} else if (FieldUtils.isStringArray(anno.field)) {
				ReflectUtils.setValue(t, fieldName, buildStrArray(info));
			} else if (FieldUtils.isBooleanArray(anno.field)) {
				ReflectUtils.setValue(t, fieldName, buildBooleanArray(info));
			} else {
				throw new LdapParseException("unkown field mapping ldap:" + anno.field.getName());
			}
		} catch (NamingException e) {
			LOGGER.info(e.getMessage(), e);
		}
	}

	private Integer[] buildIntArray(Attribute attribute) {
		String[] strResult = buildStrArray(attribute);
		Integer[] result = new Integer[strResult.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(strResult[i]);
		}
		return result;
	}

	private Boolean[] buildBooleanArray(Attribute attribute) {
		String[] strResult = buildStrArray(attribute);
		Boolean[] result = new Boolean[strResult.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Boolean.parseBoolean(strResult[i]);
		}
		return result;
	}

	private String[] buildStrArray(Attribute attribute) {
		try {
			int size = attribute.size();
			String[] result = new String[size];
			for (int i = 0; i < size; i++) {
				result[i] = attribute.get(i).toString();
			}
			return result;
		} catch (NamingException e) {
			throw new LdapParseException("cant enum the type:" + attribute, e);
		}
	}

	private List<FieldAnnotation> buildFieldAnnotations() {
		Field[] allFields = classObj.getDeclaredFields();
		List<FieldAnnotation> privateFields = new ArrayList<FieldAnnotation>();
		for (int i = 0; i < allFields.length; i++) {
			if (!Modifier.isStatic(allFields[i].getModifiers()) && Modifier.isPrivate(allFields[i].getModifiers())
					&& allFields[i].getAnnotation(LdapMapping.class) != null) {
				FieldAnnotation anno = new FieldAnnotation(allFields[i]);
				privateFields.add(anno);
				if (LdapType.RDN.equals(anno.ldapAnno.type())) {
					rdnField = anno;
				}
			}
		}
		MAP.put(classObj, privateFields);
		return privateFields;
	}

	private List<FieldAnnotation> getField() {
		List<FieldAnnotation> annos = MAP.get(classObj);
		if (annos == null) {
			throw new LdapParseException("this object[" + classObj + "] is not a type of LdapMappingBean!");
		}
		return annos;
	}

	static class FieldAnnotation {
		private Field field;
		private LdapMapping ldapAnno;

		/**
		 * 构造方法，field
		 * @param field
		 */
		public FieldAnnotation(Field field) {
			this.field = field;
			this.ldapAnno = field.getAnnotation(LdapMapping.class);
		}

		public Field getField() {
			return field;
		}

		public LdapMapping getLdapAnno() {
			return ldapAnno;
		}

	}

}
