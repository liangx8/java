package com.ruihui.common.text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import com.ruihui.dao.annotation.Field;
import com.ruihui.dao.annotation.Field.FieldType;
import com.ruihui.dao.annotation.Id;
import com.ruihui.dao.annotation.Table;

public class TextObjectUtils {
	public static TextObject toTextObject(Object o) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		
		Class<?> clz=o.getClass();
		Table table=clz.getAnnotation(Table.class);
		if(table == null){
			throw new RuntimeException("Not a mapping object");
		}
		TextObject tobj=new TextObject(o.getClass().getSimpleName().toLowerCase());
		Method ms[]=clz.getMethods();
		for(int i=0;i<ms.length;i++){
			Id id=ms[i].getAnnotation(Id.class);
			String retName;
			Object retVal;
			if(id==null){
				Field f=ms[i].getAnnotation(Field.class);
				if(f==null){
					continue;
				}
				if(f.type()==FieldType.SET){
					continue;
				}
				retName=f.name();
			} else {
				retName=id.name();
			}
			retVal=ms[i].invoke(o);
			if(retVal == null) continue;
			if(retVal instanceof Date){
				tobj.addAttribute(retName, ((Date) retVal).getTime());
			} else {
				tobj.addAttribute(retName, retVal);
			}
		}
		return tobj;
	}
}
