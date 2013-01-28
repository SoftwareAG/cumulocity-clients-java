/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
