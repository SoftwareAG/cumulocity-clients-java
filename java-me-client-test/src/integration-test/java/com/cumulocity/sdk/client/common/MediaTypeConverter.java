package com.cumulocity.sdk.client.common;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.cumulocity.me.rest.representation.CumulocityMediaType;

public class MediaTypeConverter {

    @SuppressWarnings("unchecked")
    public static MediaType asJaxRsMediaType(CumulocityMediaType cumulocityType) {
        MediaType type = MediaType.valueOf(cumulocityType.getTypeString());
        if (type == null) {
            return new MediaType(cumulocityType.getType(), cumulocityType.getSubtype(), (Map<String, String>) cumulocityType.getParameters());
        } else {
            return type;
        }
    }
}
