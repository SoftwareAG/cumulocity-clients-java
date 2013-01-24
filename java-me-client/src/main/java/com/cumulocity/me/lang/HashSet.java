package com.cumulocity.me.lang;

public class HashSet extends BaseCollection implements Set {

    private static final Object PRESENT = new Object();
    
    private final HashMap map;
    
    public HashSet() {
        this(16);
    }
    
    public HashSet(int initialCapacity) {
        map = new HashMap(initialCapacity);
    }
    
    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public Iterator iterator() {
        return map.keySet().iterator();
    }

    public boolean add(Object e) {
        map.put(e, PRESENT);
        return true;
    }

}
