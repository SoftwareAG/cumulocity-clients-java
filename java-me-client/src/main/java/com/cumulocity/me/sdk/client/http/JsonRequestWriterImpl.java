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
package com.cumulocity.me.sdk.client.http;

import java.io.UnsupportedEncodingException;

import com.cumulocity.me.http.WebMethod;
import com.cumulocity.me.http.WebRequestWriter;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.CumulocityCharset;
import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationException;
import com.cumulocity.me.rest.validate.RepresentationValidationService;
import com.cumulocity.me.sdk.SDKException;

public class JsonRequestWriterImpl implements WebRequestWriter {

    private static final Map contextForMethod = new HashMap();
    
    static {
        contextForMethod.put(WebMethod.GET, CommandBasedRepresentationValidationContext.GET);
        contextForMethod.put(WebMethod.POST, CommandBasedRepresentationValidationContext.CREATE);
        contextForMethod.put(WebMethod.PUT, CommandBasedRepresentationValidationContext.UPDATE);
        contextForMethod.put(WebMethod.DELETE, CommandBasedRepresentationValidationContext.DELETE);
    }
    
    private final RepresentationValidationService validationService;
    private final JsonConversionService conversionService;

    public JsonRequestWriterImpl(RepresentationValidationService validationService, JsonConversionService conversionService) {
        this.validationService = validationService;
        this.conversionService = conversionService;
    }

    public byte[] write(WebMethod method, CumulocityResourceRepresentation entity) {
        if (entity == null) {
            return null;
        }
        validateEntity(method, entity);
        String json = conversionService.toJson(entity).toString();
        return getJsonData(json);
    }

    private void validateEntity(WebMethod method, CumulocityResourceRepresentation entity) {
        try {
            validationService.isValid(entity, (RepresentationValidationContext) contextForMethod.get(method));
        } catch (RepresentationValidationException e) {
            throw new SDKException(422, e.getMessage());
        }
    }

    private byte[] getJsonData(String json) {
        try {
            return json.getBytes(CumulocityCharset.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
