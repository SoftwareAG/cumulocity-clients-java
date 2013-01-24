/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.inventory;

import com.cumulocity.me.model.util.ExtensibilityConverter;

/**
 * A filter to be used in managed object queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code InventoryFilter filter = new InventoryFilter().byType(type).byFragmentType(fragmentType);}
 */
public class InventoryFilter {

    private String fragmentType;

    private String type;

    /**
     * Specifies the {@code fragmentType} query parameter
     * @param fragmentClass the class representation of the type of the managed object(s)
     * @return the managed object filter with {@code fragmentType} set
     */
    public InventoryFilter byFragmentType(Class fragmentClass) {
        this.fragmentType = ExtensibilityConverter.classToStringRepresentation(fragmentClass);
        return this;
    }

    /**
     * Specifies the {@code fragmentType} query parameter
     * @param fragmentType the string representation of the type of the managed object(s)
     * @return the managed object filter with {@code fragmentType} set
     */
    public InventoryFilter byFragmentType(String fragmentType) {
        this.fragmentType = fragmentType;
        return this;
    }

    /**
     * Specifies the {@code type} query parameter
     * @param type the type of the managed object(s)
     * @return the managed object filter with {@code type} set
     */
    public InventoryFilter byType(String type) {
        this.type = type;
        return this;
    }

    /**
     * @return the {@code fragmentType} parameter of the query
     */
    public String getFragmentType() {
        return fragmentType;
    }

    /**
     * @return the {@code type} parameter of the query
     */
    public String getType() {
        return type;
    }
}
