package com.ruihui.common.text;

import java.util.Collection;
import java.util.Iterator;

import com.ruihui.common.utils.Pair;

public class ToJson {
	final private TextObject tobj;
	public ToJson(TextObject obj){
		tobj=obj;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		toJSON(sb,tobj);
		return sb.toString();
	}
	private void toJSON(StringBuilder sb, TextObject tob) {
		sb.append("{\"_tagName\":\"");
		sb.append(tob.getTagName());
		sb.append("\",");
		Iterator<Pair<String,Object>> it=tob.attributeIterator();
		while(it.hasNext()){
			Pair<String,Object> p=it.next();
			sb.append('"');
			sb.append(p.first());
			sb.append("\":");
			Object v=p.second();
			if(v instanceof Number){
				sb.append(p.second());
			} else if (v.getClass() == String.class) {
				sb.append('"');
				sb.append((String)v);
				sb.append('"');
			} else {
				sb.append(p.second());
			}
			sb.append(",");
		}
		String content=tob.getContent();
		if(content!=null){
			sb.append("\"_content\":\"");
			sb.append(content);
			sb.append("\",");
		}
		Collection<TextObject> children=tob.getChildren();
		StringBuilder sbChild=new StringBuilder();
		for(TextObject child:children){
			if(sbChild.length()==0){
				sbChild.append('[');
			} else {
				sbChild.append(',');
			}
			toJSON(sbChild,child);
		}
		if(sbChild.length()>0){
			sb.append("\"_children\":");
			sb.append(sbChild);
			sb.append("]");
		}else{
			sb.setLength(sb.length()-1);
		}
		sb.append('}');
	}
}
