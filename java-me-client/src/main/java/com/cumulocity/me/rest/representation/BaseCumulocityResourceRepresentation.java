package com.cumulocity.me.rest.representation;

public class BaseCumulocityResourceRepresentation implements CumulocityResourceRepresentation {
    
    private String self;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }
}
