package com.cumulocity.me.lang;

public abstract class BaseCollection implements Collection {

    public boolean containsAll(Collection os) {
        boolean result = true;
        Iterator i = os.iterator();
        while (result && i.hasNext()) {
            result &= contains(i.next());
        }
        return result;
    }
    
    public boolean addAll(Collection es) {
        boolean result = true;
        Iterator i = es.iterator();
        while (result) {
            result &= add(i.next());
        }
        return result;
    }
}
