package com.cumulocity.rest.representation;

import org.svenson.JSONProperty;

import java.util.Map;

public class CustomPropertiesMapRepresentation extends BaseResourceRepresentation {

    private Map<String, Object> customProperties;

    @JSONProperty(ignoreIfNull = true)
    public Map<String, Object> getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(Map<String, Object> customProperties) {
        this.customProperties = customProperties;
    }
}
