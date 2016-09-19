package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.model.FieldbusDevice;

import java.util.Enumeration;
import java.util.Vector;

public class FieldbusDeviceList{
    private final Vector delegate = new Vector();

    public synchronized void copyInto(FieldbusDevice[] anArray) {
        delegate.copyInto(anArray);
    }

    public synchronized void trimToSize() {
        delegate.trimToSize();
    }

    public synchronized void ensureCapacity(int minCapacity) {
        delegate.ensureCapacity(minCapacity);
    }

    public synchronized void setSize(int newSize) {
        delegate.setSize(newSize);
    }

    public int capacity() {
        return delegate.capacity();
    }

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public synchronized Enumeration elements() {
        return delegate.elements();
    }

    public boolean contains(FieldbusDevice elem) {
        return delegate.contains(elem);
    }

    public int indexOf(FieldbusDevice elem) {
        return delegate.indexOf(elem);
    }

    public synchronized int indexOf(FieldbusDevice elem, int index) {
        return delegate.indexOf(elem, index);
    }

    public int lastIndexOf(FieldbusDevice elem) {
        return delegate.lastIndexOf(elem);
    }

    public synchronized int lastIndexOf(FieldbusDevice elem, int index) {
        return delegate.lastIndexOf(elem, index);
    }

    public synchronized FieldbusDevice elementAt(int index) {
        return (FieldbusDevice) delegate.elementAt(index);
    }

    public synchronized FieldbusDevice firstElement() {
        return (FieldbusDevice) delegate.firstElement();
    }

    public synchronized FieldbusDevice lastElement() {
        return (FieldbusDevice) delegate.lastElement();
    }

    public synchronized void setElementAt(FieldbusDevice obj, int index) {
        delegate.setElementAt(obj, index);
    }

    public synchronized void removeElementAt(int index) {
        delegate.removeElementAt(index);
    }

    public synchronized void insertElementAt(FieldbusDevice obj, int index) {
        delegate.insertElementAt(obj, index);
    }

    public synchronized void addElement(FieldbusDevice obj) {
        if (delegate.contains(obj)) {
            delegate.removeElement(obj);
        }
        delegate.addElement(obj);
    }

    public synchronized boolean removeElement(FieldbusDevice obj) {
        return delegate.removeElement(obj);
    }

    public synchronized void removeAllElements() {
        delegate.removeAllElements();
    }

    public synchronized String toString() {
        return delegate.toString();
    }

    public synchronized void removeAll(FieldbusDeviceList toRemove){
        for (int i = 0; i < toRemove.size(); i++) {
            FieldbusDevice device = toRemove.elementAt(i);
            removeElement(device);
        }
    }

    public FieldbusDeviceList filterByProtocol(String fieldbusProtocol) {
        FieldbusDeviceList filtered = new FieldbusDeviceList();
        for (int i = 0; i < delegate.size(); i++) {
            FieldbusDevice currentDevice = this.elementAt(i);
            if (fieldbusProtocol.equals(currentDevice.getProtocol())) {
                filtered.addElement(currentDevice);
            }
        }
        return filtered;
    }

}
