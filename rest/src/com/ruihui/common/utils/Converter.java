package com.ruihui.common.utils;
/**
 * 
 * @author Observer
 *
 * @param <I> Input Type
 * @param <O> Output type
 */
public interface Converter<I, O> {
	O covert(I input);
}
