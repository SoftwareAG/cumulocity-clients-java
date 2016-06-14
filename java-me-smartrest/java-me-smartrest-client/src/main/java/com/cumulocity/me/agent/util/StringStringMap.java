package com.cumulocity.me.agent.util;

import java.util.Enumeration;
import java.util.Hashtable;

public class StringStringMap {
    private Hashtable map = new Hashtable();

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public synchronized Enumeration keys() {
        return map.keys();
    }

    public synchronized Enumeration elements() {
        return map.elements();
    }

    public synchronized boolean contains(String value) {
        return map.contains(value);
    }

    public synchronized boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public synchronized String get(String key) {
        return (String) map.get(key);
    }

    public synchronized String put(String key, String value) {
        return (String) map.put(key, value);
    }

    public synchronized String remove(String key) {
        return (String) map.remove(key);
    }

    public synchronized void clear() {
        map.clear();
    }

    public synchronized String toString() {
        return map.toString();
    }
}
