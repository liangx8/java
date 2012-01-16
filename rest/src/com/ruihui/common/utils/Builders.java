package com.ruihui.common.utils;

import java.util.Map;

public class Builders {
	public static <K,V> Pair<K,V> makePair(K first,V second){
		return new PairImpl<K,V>(first,second);
	}
	static class PairImpl<K,V> implements Pair<K,V>,Comparable<Pair<K,V>>{
		final private K first;
		final private V second;
		public PairImpl(K k,V v){
			first=k;
			second=v;
		}
		@Override
		public int compareTo(Pair<K, V> o) {
			return o.first().hashCode()-first.hashCode();
		}

		@Override
		public K first() {
			return first;
		}

		@Override
		public V second() {
			return second;
		}
		
	}
	public static <K,V> Map.Entry<K, V> makeEntry(final K first,final V second){
		return new Map.Entry<K, V>(){
			private V value=second;
			@Override
			public K getKey() {
				return first;
			}

			@Override
			public V getValue() {
				return value;
			}

			@Override
			public V setValue(V v) {
				V oldone=value;
				value=v;
				return oldone;
			}
			
		};
	}
}
