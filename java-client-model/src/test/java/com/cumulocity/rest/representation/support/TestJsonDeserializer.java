package com.cumulocity.rest.representation.support;

import com.cumulocity.model.JSONBase;
import com.cumulocity.rest.representation.BaseResourceRepresentation;
import org.svenson.JSONParser;

public interface TestJsonDeserializer {

    <T extends BaseResourceRepresentation> T deserialize(String json, Class<T> clazz);

    class SvensonTestJsonDeserializer implements TestJsonDeserializer {

        private final JSONParser svenson = JSONBase.getJSONParser();

        @Override
        public <T extends BaseResourceRepresentation> T deserialize(String json, Class<T> clazz) {
            return svenson.parse(clazz, json);
        }
    }

}
