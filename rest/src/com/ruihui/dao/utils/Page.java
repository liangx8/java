package com.ruihui.dao.utils;

import java.util.Collection;

public class Page {
	private int total;
	private int start;
	private int limit;
	private Collection<? extends Object> data;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Collection<? extends Object> getData() {
		return data;
	}
	public void setData(Collection<? extends Object> data) {
		this.data = data;
	}
	public int getPrev(){
		return start-limit<0?0:start-limit;
	}
	public int getNext(){
		return start+limit>=total?start:start+limit;
	}
	public int getLast(){
		return total-limit;
	}
}
