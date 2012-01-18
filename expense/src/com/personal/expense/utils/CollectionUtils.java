package com.personal.expense.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CollectionUtils {
	public static <T> List<T> emptyList(){
		return new LinkedList<T>();
	}

	public static <K,V> Map<K, V> emptyMap() {
		return new HashMap<K,V>();
	}
}
