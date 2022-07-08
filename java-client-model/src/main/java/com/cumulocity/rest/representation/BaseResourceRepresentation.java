package com.cumulocity.rest.representation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.svenson.JSONProperty;

import com.cumulocity.model.JSONBase;

public class BaseResourceRepresentation implements ResourceRepresentation {

    private String self;

    public void setSelf(String self) {
        this.self = self;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getSelf() {
        return self;
    }

    @JSONProperty(ignore = true, readOnly = true)
    public String getSelfDecoded() {
        if (self == null) {
            return null;
        }
        try {
            return URLDecoder.decode(self, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return self;
        }
    }

    @Override
    public String toString() {
        return toJSON();
    }

    @JSONProperty(ignore = true)
    public String toJSON() {
        return JSONBase
                .getJSONGenerator()
                .forValue(this);
    }
}
