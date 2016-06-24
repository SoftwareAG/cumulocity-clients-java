package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.FieldbusBufferKey;

import java.util.Hashtable;

public class FieldbusBuffer {
    private Hashtable map = new Hashtable();

    public byte[] get(FieldbusBufferKey key){
        return (byte[]) map.get(key);
    }

    public byte[] put(FieldbusBufferKey key, byte[] value) {
        return (byte[]) map.put(key, value);
    }
}
