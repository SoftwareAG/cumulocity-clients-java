package com.cumulocity.me.lang;

import java.util.Enumeration;

class EnumerationIterator implements Iterator {

	private final Enumeration enumeration;

	public EnumerationIterator(Enumeration enumeration) {
		this.enumeration = enumeration;
	}
	
	public boolean hasNext() {
		return enumeration.hasMoreElements();
	}

	public Object next() {
		return enumeration.nextElement();
	}
	
}