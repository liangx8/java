package com.personal.expense.sqlite;

import java.util.List;

import android.database.Cursor;

import com.personal.expense.utils.CollectionUtils;
import com.personal.expense.utils.callback.Converter;

public class CursorUtils {

	public static <T> List<T> populate(Cursor c,EntityPopulated<T> ep) {
		List<T> list=CollectionUtils.emptyList();
		while(c.moveToNext()){
			T e=ep.convert(c);
			list.add(e);
		}
		return list;
	}
	public static interface EntityPopulated<T> extends Converter<Cursor, T>{}


}
