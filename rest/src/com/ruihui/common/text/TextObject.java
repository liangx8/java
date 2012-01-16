package com.ruihui.common.text;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ruihui.common.utils.Builders;
import com.ruihui.common.utils.CollectionUtils;
import com.ruihui.common.utils.Pair;

public class TextObject {
	final private String tagName;
	final private List<TextObject> children;
	final private List<Pair<String,Object>> attributes;
	private String content;

	Iterator<Pair<String,Object>> attributeIterator(){
		return attributes.iterator();
	}
	public TextObject(String tag){
		if(tag == null){
			throw new NullPointerException();
		}
		tagName=tag;
		children=CollectionUtils.emptyList();
		attributes=CollectionUtils.emptyList();
	}
	public String getTagName(){
		return tagName;
	}
	public TextObject appendChild(TextObject child){
		if(child==null){
			throw new NullPointerException();
		}
		children.add(child);
		return this;
	}
	public Collection<TextObject> getChildren(){
		return Collections.unmodifiableCollection(children);
	}
	public TextObject setContent(String c){
		content=c;
		return this;
	}
	public String getContent(){
		return content;
	}
	public TextObject addAttribute(String name,Object value){
		attributes.add(Builders.makePair(name, value));
		return this;
	}
	public Object attributeValue(String name){
		for(Pair<String,Object> p :attributes){
			if(p.first().equals(name))
				return p.second();
		}
		return null;
	}
}
