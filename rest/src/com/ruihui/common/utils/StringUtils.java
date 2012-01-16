package com.ruihui.common.utils;


public class StringUtils {
	public static String arrayToString(Object[] array){
		StringBuilder sb=new StringBuilder('[');
		for(int i=0;i<array.length;i++){
			sb.append(array[i].toString());
			sb.append(',');
		}
		sb.setLength(sb.length()-1);
		sb.append(']');
		
		return sb.toString();
	}
}
