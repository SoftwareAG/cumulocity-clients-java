/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
