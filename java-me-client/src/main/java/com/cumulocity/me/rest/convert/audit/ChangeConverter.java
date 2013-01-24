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
package com.cumulocity.me.rest.convert.audit;

import com.cumulocity.me.model.audit.Change;
import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;

public class ChangeConverter extends BaseRepresentationConverter {

    private static final String PROP_ATTRIBUTE = "attribute";
    private static final String PROP_TYPE = "type";
    private static final String PROP_PREVIOUS_VALUE = "previousValue";
    private static final String PROP_NEW_VALUE = "newValue";
    
    public JSONObject toJson(Object representation) {
        JSONObject json = new JSONObject();
        json.putOpt(PROP_ATTRIBUTE, ((Change)representation).getAttribute());
        json.putOpt(PROP_TYPE, ((Change)representation).getType());
        json.putOpt(PROP_PREVIOUS_VALUE, (getConversionService().toJson(((Change)representation).getPreviousValue())));
        json.putOpt(PROP_NEW_VALUE, (getConversionService().toJson(((Change)representation).getNewValue())));
        return json;
    }

    public Object fromJson(JSONObject json) {
        Change change = new Change();
        change.setAttribute(getString(json, PROP_ATTRIBUTE));
        change.setType(getString(json, PROP_TYPE));
        change.setPreviousValue(getObject(json, PROP_PREVIOUS_VALUE, Change.class));
        change.setNewValue(getObject(json, PROP_NEW_VALUE, Change.class));
        return change;
    }

    protected Class supportedRepresentationType() {
        return Change.class;
    }
}
