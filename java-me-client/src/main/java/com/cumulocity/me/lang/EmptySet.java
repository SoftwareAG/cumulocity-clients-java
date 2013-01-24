package com.cumulocity.me.lang;

class EmptySet implements Set {

	public int size() {
		return 0;
	}

	public boolean isEmpty() {
		return true;
	}

	public boolean contains(Object o) {
		return false;
	}
	
	public boolean containsAll(Collection os) {
	    return false;
	}

	public Iterator iterator() {
		return Collections.EMPTY_ITERATOR;
	}

	public boolean add(Object e) {
		throw new UnsupportedOperationException();
	}
	
	public boolean addAll(Collection es) {
	    throw new UnsupportedOperationException();
	}
}