package com.cumulocity.me.rest.representation.devicecontrol;

public class RelayState {

    public static final RelayState OPEN = new RelayState("OPEN");
    
    public static final RelayState CLOSED = new RelayState("CLOSED");
    
    private String name;
    
    private RelayState(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
