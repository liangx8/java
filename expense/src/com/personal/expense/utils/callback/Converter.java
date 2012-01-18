package com.personal.expense.utils.callback;

public interface Converter<I, O> {
	O convert(I input);
}
