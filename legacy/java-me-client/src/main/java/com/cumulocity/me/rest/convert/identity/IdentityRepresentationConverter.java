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
package com.cumulocity.me.rest.convert.identity;

import com.cumulocity.me.lang.UnsupportedOperationException;
import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseResourceRepresentation;
import com.cumulocity.me.rest.representation.identity.IdentityRepresentation;

public class IdentityRepresentationConverter extends BaseResourceRepresentationConverter {

    private static final String PROP_EXTERNAL_IDS_OF_GLOBAL_ID = "externalIdsOfGlobalId";

    private static final String PROP_EXTERNAL_ID = "externalId";

    protected void instanceToJson(BaseResourceRepresentation representation, JSONObject json) {
        throw new UnsupportedOperationException();
    }

    protected void instanceFromJson(JSONObject json, BaseResourceRepresentation representation) {
        $(representation).setExternalId(getString(json, PROP_EXTERNAL_ID));
        $(representation).setExternalIdsOfGlobalId(getString(json, PROP_EXTERNAL_IDS_OF_GLOBAL_ID));
    }

    protected Class supportedRepresentationType() {
        return IdentityRepresentation.class;
    }

    private IdentityRepresentation $(BaseResourceRepresentation baseRepresentation) {
        return (IdentityRepresentation) baseRepresentation;
    }

}
