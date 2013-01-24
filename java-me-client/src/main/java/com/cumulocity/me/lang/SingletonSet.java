package com.cumulocity.me.lang;

class SingletonSet extends BaseCollection implements Set {

    private final Object value;

    public SingletonSet(Object value) {
        this.value = value;
    }
    
    public int size() {
        return 1;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean contains(Object o) {
        return value.equals(o);
    }

    public Iterator iterator() {
        return new SingletonIterator(value);
    }

    public boolean add(Object e) {
        throw new UnsupportedOperationException();
    }
}