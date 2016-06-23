package com.cumulocity.me.agent.util;

public class Arrays {
    public static void bubbleSort(Object[] toSort, Comparator comparator){
        if (toSort == null || toSort.length < 2) {
            return;
        }
        if (!comparator.isSupported(toSort[0])) {
            throw new RuntimeException("Comparator " + comparator + " does not support comparing " + toSort[0]);
        }
        doBubbleSort(toSort, comparator);
    }

    private static void doBubbleSort(Object[] toSort, Comparator comparator) {
        Object temp;
        boolean swapped = true; //assume we need to swap two elements the least
        while(swapped) {
            swapped = false;
            for (int i = 1; i < toSort.length; i++) {
                if(comparator.compare(toSort[i-1], toSort[i]) > 0){
                    temp = toSort[i-1];
                    toSort[i-1] = toSort[i];
                    toSort[i] = temp;
                    swapped = true;
                }
            }
        }
    }

    public static String toString(Object[] array){
        StringBuffer buffer = new StringBuffer("Object[]{[");
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                buffer.append(", ");
            }
            Object object = array[i];
            if (object == null) {
                buffer.append("null");
            } else {
                buffer.append(object.toString());
            }
        }
        buffer.append("]}");
        return buffer.toString();
    }

    public static String toString(byte[] array){
        StringBuffer buffer = new StringBuffer("byte[]{[");
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                buffer.append(", ");
            }
            byte currentByte = array[i];
            buffer.append(currentByte);
        }
        buffer.append("]}");
        return buffer.toString();
    }
}
