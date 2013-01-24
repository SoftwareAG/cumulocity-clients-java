package com.cumulocity.me.lang;

import java.util.NoSuchElementException;

class EmptyIterator implements Iterator {

	public boolean hasNext() {
		return false;
	}

	public Object next() {
		throw new NoSuchElementException();
	}
	
}