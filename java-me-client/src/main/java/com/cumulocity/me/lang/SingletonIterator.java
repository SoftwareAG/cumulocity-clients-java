package com.cumulocity.me.lang;

import java.util.NoSuchElementException;

class SingletonIterator implements Iterator {

    private final Object value;
    private boolean hasNext = true;

    public SingletonIterator(Object value) {
        this.value = value;
    }
    
    public boolean hasNext() {
        return hasNext;
    }

    public Object next() {
        if (hasNext()) {
            hasNext = false;
            return value;
        }
        throw new NoSuchElementException();
    }
    
}