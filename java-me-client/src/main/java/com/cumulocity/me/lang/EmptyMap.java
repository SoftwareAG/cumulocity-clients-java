package com.cumulocity.me.lang;

class EmptyMap implements Map {

	public int size() {
		return 0;
	}

	public boolean isEmpty() {
		return true;
	}
	
	public boolean containsKey(Object key) {
	    return false;
	}

	public Object get(Object key) {
		return null;
	}

	public Object put(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map newParams) {
		throw new UnsupportedOperationException();
	}

	public Set keySet() {
		return Collections.EMPTY_SET;
	}

	public Set entrySet() {
		return Collections.EMPTY_SET;
	}
}