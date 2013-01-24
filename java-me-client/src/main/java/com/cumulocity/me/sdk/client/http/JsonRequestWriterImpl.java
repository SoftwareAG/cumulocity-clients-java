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
