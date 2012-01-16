package com.ruihui.dao;

import java.util.List;

import com.ruihui.dao.utils.Order;

public interface GenericDao<T> {
	void insert(T entity);
	T fetch(Long id);
	List<T> all(Order order);
	List<T> list(String whereCondition);
	List<T> list(String whereCondition,Object []params);
	<C extends T> List<C> list(String whereCondition,Object []params, Class<C> type);
	T unique(String whereCondition);
	void update(T entity);
	int count();
}
