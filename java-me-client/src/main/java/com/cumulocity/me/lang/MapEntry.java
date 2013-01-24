package com.cumulocity.me.lang;

import com.cumulocity.me.lang.Map.Entry;

class MapEntry implements Entry {

	private final Object key;
	private final Object value;

	public MapEntry(Object key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public Object getKey() {
		return this.key;
	}

	public Object getValue() {
		return this.value;
	}
	
	public String toString() {
	    return key + "=" + value;
	}
}