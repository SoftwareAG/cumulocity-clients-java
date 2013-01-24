package com.cumulocity.me.lang;

class SingletonMap implements Map {
    
    private final Object key;
    private final Object value;

    public SingletonMap(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public int size() {
        return 1;
    }

    public boolean isEmpty() {
        return false;
    }
    
    public boolean containsKey(Object key) {
        return key.equals(key);
    }

    public Object get(Object key) {
        if (containsKey(key)) {
            return value;
        } else { 
            return null;
        }
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map newParams) {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        return new SingletonSet(key);
    }

    public Set entrySet() {
        return new SingletonSet(new MapEntry(key, value));
    }
}