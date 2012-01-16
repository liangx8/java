package com.ruihui.dao.annotation;

@java.lang.annotation.Target(value = { java.lang.annotation.ElementType.METHOD })
@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Field {
	public String name();
	public FieldType type();
	public static enum FieldType {SET,GET};
}
