package com.ruihui.dao.utils;

import java.util.List;

import com.ruihui.common.utils.Builders;
import com.ruihui.common.utils.CollectionUtils;
import com.ruihui.common.utils.Pair;


public class Order {
	final private List<Pair<String,String>> orders;
	public Order(String dir,String sort){
		orders=CollectionUtils.emptyList();
		orders.add(Builders.makePair(dir, sort));
	}
	public Order append(String dir,String sort){
		orders.add(Builders.makePair(dir, sort));
		return this;
	}
	@Override
	public String toString() {
		StringBuilder sbOrder=new StringBuilder("order by ");
		for(Pair<String,String> p:orders){
			sbOrder.append(p.second());
			sbOrder.append(" ");
			sbOrder.append(p.first());
			sbOrder.append(',');
		}
		sbOrder.setLength(sbOrder.length()-1);
		return sbOrder.toString();
	}
}
