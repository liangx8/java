package com.ruihui.common.utils;
/**
 * 
 * @author Observer
 *
 * @param <I1> input 1
 * @param <I2> input 2
 * @param <O> output
 */
public interface Combinator<I1, I2, O> {
	O combinate(I1 i1,I2 i2);
}
