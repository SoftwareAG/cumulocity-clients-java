package com.cumulocity.model.builder;

/**
 * Domain object builder pattern.
 */
public class DomainObjectBuilder {

    public static GIdBuilder aGId() {
        return new GIdBuilder();
    }

}
