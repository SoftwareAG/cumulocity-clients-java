package com.cumulocity.me.lang;

public class Collections {
	
	public static final Iterator EMPTY_ITERATOR = new EmptyIterator();
	
	public static final Set EMPTY_SET = new EmptySet();
	
	public static final Map EMPTY_MAP = new EmptyMap();

	public static Map singletonMap(Object key, Object value) {
		return new SingletonMap(key, value);
	}
	
	public static Map emptyMap() {
		return EMPTY_MAP;
	}
}
