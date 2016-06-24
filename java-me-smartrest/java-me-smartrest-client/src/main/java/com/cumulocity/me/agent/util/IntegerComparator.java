package com.cumulocity.me.agent.util;

public class IntegerComparator implements Comparator{

    public boolean isSupported(Object object) {
        return object instanceof Integer;
    }

    public int compare(Object first, Object second) {
        int firstInt = ((Integer) first).intValue();
        int secondInt = ((Integer) second).intValue();
        if (firstInt == secondInt) {
            return 0;
        }
        if (firstInt < secondInt) {
            return -1;
        } else {
            return 1;
        }
    }
}
