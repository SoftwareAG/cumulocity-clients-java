package com.cumulocity.me.agent.smartrest.model;

import java.util.Enumeration;
import java.util.Vector;

public class RequestBufferItemList {
    Vector vector = new Vector();

    public synchronized void copyInto(RequestBufferItem[] anArray) {
        vector.copyInto(anArray);
    }

    public synchronized void trimToSize() {
        vector.trimToSize();
    }

    public synchronized void ensureCapacity(int minCapacity) {
        vector.ensureCapacity(minCapacity);
    }

    public synchronized void setSize(int newSize) {
        vector.setSize(newSize);
    }

    public int capacity() {
        return vector.capacity();
    }

    public int size() {
        return vector.size();
    }

    public boolean isEmpty() {
        return vector.isEmpty();
    }

    public synchronized Enumeration elements() {
        return vector.elements();
    }

    public boolean contains(RequestBufferItem elem) {
        return vector.contains(elem);
    }

    public int indexOf(RequestBufferItem elem) {
        return vector.indexOf(elem);
    }

    public synchronized int indexOf(RequestBufferItem elem, int index) {
        return vector.indexOf(elem, index);
    }

    public int lastIndexOf(RequestBufferItem elem) {
        return vector.lastIndexOf(elem);
    }

    public synchronized int lastIndexOf(RequestBufferItem elem, int index) {
        return vector.lastIndexOf(elem, index);
    }

    public synchronized RequestBufferItem elementAt(int index) {
        return (RequestBufferItem) vector.elementAt(index);
    }

    public synchronized RequestBufferItem firstElement() {
        return (RequestBufferItem) vector.firstElement();
    }

    public synchronized RequestBufferItem lastElement() {
        return (RequestBufferItem) vector.lastElement();
    }

    public synchronized void setElementAt(RequestBufferItem obj, int index) {
        vector.setElementAt(obj, index);
    }

    public synchronized void removeElementAt(int index) {
        vector.removeElementAt(index);
    }

    public synchronized void insertElementAt(RequestBufferItem obj, int index) {
        vector.insertElementAt(obj, index);
    }

    public synchronized void addElement(RequestBufferItem obj) {
        vector.addElement(obj);
    }

    public synchronized boolean removeElement(RequestBufferItem obj) {
        return vector.removeElement(obj);
    }

    public synchronized void removeAllElements() {
        vector.removeAllElements();
    }

    public synchronized String toString() {
        return vector.toString();
    }
}
