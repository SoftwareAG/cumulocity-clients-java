package com.cumulocity.me.agent.util;

public interface Comparator {
    public boolean isSupported(Object object);

    public int compare(Object first, Object second);
}
