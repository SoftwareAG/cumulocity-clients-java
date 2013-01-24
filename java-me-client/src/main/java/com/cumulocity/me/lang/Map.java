package com.cumulocity.me.lang;

public interface Map {
	
	int size();

	boolean isEmpty();
	
	boolean containsKey(Object key);

	Object get(Object key);

	Object put(Object key, Object value);
	
	void putAll(Map newParams);

	Set keySet();
	
	Set entrySet();
	
	interface Entry {
		
		Object getKey();

		Object getValue();
	}
}
