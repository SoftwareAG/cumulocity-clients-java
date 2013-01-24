package com.cumulocity.me.lang;

import java.util.Vector;

public class ArrayList extends BaseCollection implements List {

	private final Vector backend;
	
	public ArrayList() {
		this(16);
	}
	
	public ArrayList(int initialCapacity) {
		this.backend = new Vector(initialCapacity);
	}
	
	public int size() {
		return backend.size();
	}

	public boolean isEmpty() {
		return backend.isEmpty();
	}

	public boolean contains(Object o) {
		return backend.contains(o);
	}
	
	public Object get(int idx) {
	    return backend.elementAt(idx);
	}
	
	public Iterator iterator() {
		return new EnumerationIterator(backend.elements());
	}

	public boolean add(Object e) {
		backend.addElement(e);
		return true;
	}
	
	public int hashCode() {
	    return backend.hashCode();
	}
	
	public boolean equals(Object obj) {
	    return backend.equals(obj);
	}
	
	public String toString() {
	    return backend.toString();
	}
}
