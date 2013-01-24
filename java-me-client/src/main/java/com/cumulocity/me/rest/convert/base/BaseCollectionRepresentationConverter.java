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
package com.cumulocity.me.rest.convert.base;

import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.PageStatisticsRepresentation;

public abstract class BaseCollectionRepresentationConverter extends BaseResourceRepresentationConverter {

    private static final String PROP_PAGE_STATISTICS = "statistics";
    private static final String PROP_PREV = "prev";
    private static final String PROP_NEXT = "next";

    protected void basePropertiesToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        super.basePropertiesToJson(representation, json);
        putObject(json, PROP_PAGE_STATISTICS, $(representation).getPageStatistics());
        putString(json, PROP_PREV, $(representation).getPrev());
        putString(json, PROP_NEXT, $(representation).getNext());
    }
    
    protected void basePropertiesFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        super.basePropertiesFromJson(json, representation);
        $(representation).setPageStatistics((PageStatisticsRepresentation) getObject(json, PROP_PAGE_STATISTICS, PageStatisticsRepresentation.class));
        $(representation).setPrev(getString(json, PROP_PREV));
        $(representation).setNext(getString(json, PROP_NEXT));
    }

    private BaseCollectionRepresentation $(BaseCumulocityResourceRepresentation representation) {
        return (BaseCollectionRepresentation) representation;
    }
}
