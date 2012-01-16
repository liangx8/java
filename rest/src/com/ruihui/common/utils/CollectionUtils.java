package com.ruihui.common.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {
	public static <T> List<T> emptyList(){
		return new LinkedList<T>();
	}
	public static <K,V> Map<K,V> emptyMap(){
		return new HashMap<K,V>();
	}
	public static <T> Set<T> emptySet(){
		return new HashSet<T>();
	}
}
