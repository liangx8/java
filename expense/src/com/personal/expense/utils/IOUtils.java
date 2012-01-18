package com.personal.expense.utils;

import java.io.IOException;
import java.io.InputStreamReader;

public class IOUtils {
	public static String readLine(InputStreamReader r) throws IOException{
		int c;
		StringBuilder sb=new StringBuilder();
		boolean isFinish=true;
		while((c=r.read())>=0){
			isFinish=false;
			if(c==10)continue;
			if(c==13){
				break;
			}
			sb.append((char)c);
		}
		return isFinish?null:sb.toString();
	}
}
