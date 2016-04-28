package com.cumulocity.me.agent.smartrest;

import com.cumulocity.me.smartrest.client.SmartResponse;
import java.util.Enumeration;
import java.util.Vector;

public class SmartResponseList {
    Vector list = new Vector();

    public synchronized void copyInto(SmartResponse[] anArray) { 
        list.copyInto(anArray);
    }

    public synchronized void trimToSize() {
        list.trimToSize();
    }

    public synchronized void ensureCapacity(int minCapacity) {
        list.ensureCapacity(minCapacity);
    }

    public synchronized void setSize(int newSize) {
        list.setSize(newSize);
    }

    public int capacity() {
        return list.capacity();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public synchronized Enumeration elements() {
        return list.elements();
    }

    public boolean contains(SmartResponse elem) {
        return list.contains(elem);
    }

    public int indexOf(SmartResponse elem) {
        return list.indexOf(elem);
    }

    public synchronized int indexOf(SmartResponse elem, int index) {
        return list.indexOf(elem, index);
    }

    public int lastIndexOf(SmartResponse elem) {
        return list.lastIndexOf(elem);
    }

    public synchronized int lastIndexOf(SmartResponse elem, int index) {
        return list.lastIndexOf(elem, index);
    }

    public synchronized SmartResponse elementAt(int index) {
        return (SmartResponse) list.elementAt(index);
    }

    public synchronized SmartResponse firstElement() {
        return (SmartResponse) list.firstElement();
    }

    public synchronized SmartResponse lastElement() {
        return (SmartResponse) list.lastElement();
    }

    public synchronized void setElementAt(SmartResponse obj, int index) {
        list.setElementAt(obj, index);
    }

    public synchronized void removeElementAt(int index) {
        list.removeElementAt(index);
    }

    public synchronized void insertElementAt(SmartResponse obj, int index) {
        list.insertElementAt(obj, index);
    }

    public synchronized void addElement(SmartResponse obj) {
        list.addElement(obj);
    }

    public synchronized boolean removeElement(SmartResponse obj) {
        return list.removeElement(obj);
    }

    public synchronized void removeAllElements() {
        list.removeAllElements();
    }

    public synchronized String toString() {
        return list.toString();
    }
    
}
