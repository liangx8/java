package com.ruihui.common.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.sql.rowset.serial.SerialClob;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ruihui.dao.annotation.Field;
import com.ruihui.dao.annotation.Field.FieldType;
import com.ruihui.dao.annotation.Table;


/**
 * To parse web request body in XML format
 * @author Observer
 *
 */
public class XMLBody<T> {
	final private SAXParserFactory saxParserFactory=SAXParserFactory.newInstance();
	final private BindToObjectHandler<T> handler;
	public XMLBody(byte buf[]){
		handler=new BindToObjectHandler<T>();
		SAXParser parser;
		try {
			parser=saxParserFactory.newSAXParser();
		} catch (Exception e) {
			// Unknown error
			throw new RuntimeException(e);
		}
		ByteArrayInputStream in=new ByteArrayInputStream(buf);
		try {
			// 转换xml to object bean
			parser.parse(in, handler);
			in.close();
		} catch (SAXException e) {
			throw new RuntimeException("需要解析的xml内容不准确.",e);
		} catch (IOException e) {
			// impossible error
			throw new RuntimeException(e);
		}
	}
	public XMLBody(byte buf[],String key,Class<T> clz){
		SAXParser parser;
		try {
			parser=saxParserFactory.newSAXParser();
		} catch (Exception e) {
			// Unknown error
			throw new RuntimeException(e);
		}
		T obj;
		String msg1="类"+clz.getName()+"的构造函数不能被访问!";
		try {
			obj = clz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(msg1,e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(msg1,e);
		}

		ByteArrayInputStream in=new ByteArrayInputStream(buf);
		try {
			// 转换xml to object bean
			handler=new BindToObjectHandler<T>(obj,key);
			parser.parse(in, handler);
			in.close();
		} catch (SAXException e) {
			throw new RuntimeException("需要解析的xml内容不准确.",e);
		} catch (IOException e) {
			// impossible error
			throw new RuntimeException(e);
		}
	}
	public T getObject(){
		return handler.getTarget();
	}
	/**
	 * 转换xml到object
	 * @author Observer
	 *
	 */
	static class BindToObjectHandler<T> extends DefaultHandler{
		final private String tag;
		final private T targetObject;
		final private Map<String,String> parameters;
		public BindToObjectHandler(){
			targetObject=null;
			tag=null;
			parameters=CollectionUtils.emptyMap();
		}
		public BindToObjectHandler(T obj,String tagName) {
			tag=tagName;
			Class<?> clz=obj.getClass();
			Table t=clz.getAnnotation(Table.class);
			if(t==null){
				throw new RuntimeException("Target object is not a mapping object");
			}
			targetObject=obj;
			parameters=CollectionUtils.emptyMap();
		}
		public T getTarget(){
			return targetObject;
		}
		/**
		 * pName name of parameter
		 * @param pName
		 * @return
		 */
		public String fetchValue(String pName){
			return parameters.get(pName);
		}
		@Override
		public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
			if(qName.equals("parameter")){
				for(int i=0;i<attributes.getLength();i++){
					parameters.put(attributes.getQName(i), attributes.getValue(i));
				}
			}
			if(tag!=null && qName.equals(tag)){
				Method ms[]=targetObject.getClass().getMethods();
				for(int j=0;j<attributes.getLength();j++){
					String attName=attributes.getQName(j);
					for(int i=0;i<ms.length;i++){
						Field f=ms[i].getAnnotation(Field.class);
						if(f!=null && f.type()==FieldType.SET){
							if(attName.equals(f.name())){
								Class<?> types[]=ms[i].getParameterTypes();
								if(types.length!=1){
									throw new RuntimeException("方法"+ms[i].getName()+"不是正确格式的set方法");
								}
								try {
									ms[i].invoke(targetObject, stringToObject(types[0],attributes.getValue(j)));
								} catch (IllegalArgumentException e) {
									// impossible exception
									throw new RuntimeException(e);
								} catch (IllegalAccessException e) {
									throw new RuntimeException("错误原因:set方法是private",e);
								} catch (InvocationTargetException e) {
									// impossible exception
									throw new RuntimeException(e);
								} 
							}
						}
					}
				}
			}
		}
		private Object stringToObject(Class<?> type, String value){
			if(type==String.class){
				return value;
			}
			if(Number.class.isAssignableFrom(type)){
				try {
					Constructor<?> constructor=type.getConstructor(String.class);
					Object propValue=constructor.newInstance(value);
					return propValue;
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					// impossible exception
					throw new RuntimeException(e);
				} catch (InstantiationException e) {
					// Unknown exception
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					// impossible exception
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					// Unknown exception
					throw new RuntimeException(e);
				}
			}
			if(type==Clob.class){
				try {
					return new SerialClob(value.toCharArray());
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			if(type==Date.class){
				// value was expected as long number type;
				long val=Long.parseLong(value);
				return new Date(val);
			}
			throw new RuntimeException("Cann't recognize type of "+type.getName());
		}
	}
	public String parameter(String key) {
		return handler.parameters.get(key);
	}
}
