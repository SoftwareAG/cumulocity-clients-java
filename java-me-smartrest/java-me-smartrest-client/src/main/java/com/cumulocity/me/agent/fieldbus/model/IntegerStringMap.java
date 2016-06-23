package com.cumulocity.me.agent.fieldbus.model;

import java.util.Enumeration;
import java.util.Hashtable;

public class IntegerStringMap {
    private Hashtable delegate = new Hashtable();

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public synchronized Enumeration keys() {
        return delegate.keys();
    }

    public synchronized Enumeration elements() {
        return delegate.elements();
    }

    public synchronized boolean contains(String value) {
        return delegate.contains(value);
    }

    public synchronized boolean containsKey(Integer key) {
        return delegate.containsKey(key);
    }

    public synchronized String get(Integer key) {
        return (String) delegate.get(key);
    }

    public synchronized String put(Integer key, String value) {
        return (String) delegate.put(key, value);
    }

    public synchronized String remove(Integer key) {
        return (String) delegate.remove(key);
    }

    public synchronized void clear() {
        delegate.clear();
    }

    public synchronized String toString() {
        return delegate.toString();
    }
}
