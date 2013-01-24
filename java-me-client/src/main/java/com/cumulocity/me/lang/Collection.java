package com.cumulocity.me.lang;

public interface Collection {

	int size();

	boolean isEmpty();

	boolean contains(Object o);
	
	boolean containsAll(Collection os);

	Iterator iterator();

	boolean add(Object e);
	
	boolean addAll(Collection es);

}
