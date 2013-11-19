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

package com.cumulocity.sdk.client.inventory;

import static com.cumulocity.model.util.ExtensibilityConverter.classToStringRepresentation;

import java.util.List;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.sdk.client.Filter;
import com.cumulocity.sdk.client.ParamName;

/**
 * A filter to be used in managed object queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code InventoryFilter filter = new InventoryFilter().byType(type).byFragmentType(fragmentType);}
 */
public class InventoryFilter extends Filter {

    @ParamName("fragmentType")
    private String fragmentType;

    @ParamName("type")
    private String type;
    
    @ParamName("owner")
    private String owner;
    
    @ParamName("text")
    private String text;
    
    @ParamName("ids")
    private String ids;

    /**
     * Specifies the {@code fragmentType} query parameter
     *
     * @param fragmentClass the class representation of the type of the managed object(s)
     * @return the managed object filter with {@code fragmentType} set
     */
    public InventoryFilter byFragmentType(Class<?> fragmentClass) {
        this.fragmentType = classToStringRepresentation(fragmentClass);
        return this;
    }

    /**
     * Specifies the {@code fragmentType} query parameter
     *
     * @param fragmentType the string representation of the type of the managed object(s)
     * @return the managed object filter with {@code fragmentType} set
     */
    public InventoryFilter byFragmentType(String fragmentType) {
        this.fragmentType = fragmentType;
        return this;
    }

    /**
     * @return the {@code fragmentType} parameter of the query
     */
    public String getFragmentType() {
        return fragmentType;
    }

    /**
     * Specifies the {@code type} query parameter
     *
     * @param type the type of the managed object(s)
     * @return the managed object filter with {@code type} set
     */
    public InventoryFilter byType(String type) {
        this.type = type;
        return this;
    }

    /**
     * @return the {@code type} parameter of the query
     */
    public String getType() {
        return type;
    }
    
    /**
     * Specifies the {@code owner} query parameter
     *
     * @param owner the owner of the managed object(s)
     * @return the managed object filter with {@code owner} set
     */
    public InventoryFilter byOwner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * @return the {@code owner} parameter of the query
     */
    public String getOwner() {
        return owner;
    }
    
    /**
     * Specifies the {@code text} query parameter
     *
     * @param text the text of the managed object(s)
     * @return the managed object filter with {@code text} set
     */
    public InventoryFilter byText(String text) {
        this.text = text;
        return this;
    }

    /**
     * @return the {@code text} parameter of the query
     */
    public String getText() {
        return text;
    }
    
    /**
     * Specifies the {@code ids} query parameter
     *
     * @param ids the ids of the managed object(s)
     * @return the managed object filter with {@code ids} set
     */
    public InventoryFilter byIds(List<GId> ids) {
        this.ids = createCommaSeparatedStringFromGids(ids);
        return this;
    }
    
    private String createCommaSeparatedStringFromGids(List<GId> ids) {
        boolean atLeastOneItemProcessed = false;
        StringBuilder builder = new StringBuilder();

        for (GId gid : ids) {
            atLeastOneItemProcessed = true;
            builder.append(gid.getValue());
            builder.append(",");
        }

        // remove last comma if needed
        if (atLeastOneItemProcessed) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    /**
     * @return the {@code ids} parameter of the query
     */
    public String getIds() {
        return ids;
    }

}
