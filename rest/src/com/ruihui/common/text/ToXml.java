package com.ruihui.common.text;

import java.util.Collection;
import java.util.Iterator;

import com.ruihui.common.utils.Pair;

public class ToXml {
	final private TextObject tobj;
	public ToXml(TextObject obj){
		tobj=obj;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		toXML(sb,tobj);
		return sb.toString();
	}
	private void toXML(StringBuilder sb,TextObject obj) {
		sb.append('<');
		sb.append(obj.getTagName());
		Iterator<Pair<String,Object>> it=obj.attributeIterator();
		while(it.hasNext()){
			Pair<String,Object> p=it.next();
			sb.append(' ');
			sb.append(p.first());
			sb.append("=\"");
			sb.append(p.second());
			sb.append('"');
		}
		Collection<TextObject> children=obj.getChildren();
		String content=obj.getContent();
		if(children.size()==0 && content==null){
			sb.append("/>");
		} else {
			sb.append(">");
			if(content!=null){
				sb.append(content);
			}
			if(children.size()>0){
				for(TextObject o:children){
					toXML(sb,o);
				}
			}
			sb.append("</");
			sb.append(obj.getTagName());
			sb.append('>');
		}
	}

}
