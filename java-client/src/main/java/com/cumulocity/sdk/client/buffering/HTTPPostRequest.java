package com.cumulocity.sdk.client.buffering;

import java.util.HashMap;
import java.util.Map;

import com.cumulocity.model.JSONBase;
import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.ResourceRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmMediaType;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventMediaType;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementMediaType;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;

public class HTTPPostRequest {
    
    private static final Map<String, CumulocityMediaType> toMediaTypeMappings = new HashMap<String, CumulocityMediaType>(); 
    private static final Map<CumulocityMediaType, String> fromMediaTypeMappings = new HashMap<CumulocityMediaType, String>();
    
    private static final Map<String, Class<? extends ResourceRepresentation>> toClassMappings = new HashMap<String, Class<? extends ResourceRepresentation>>(); 
    private static final Map<Class<? extends ResourceRepresentation>, String> fromClassMappings = new HashMap<Class<? extends ResourceRepresentation>, String>();
    
    static {
        toMediaTypeMappings.put("A", AlarmMediaType.ALARM);
        toMediaTypeMappings.put("E", EventMediaType.EVENT);
        toMediaTypeMappings.put("M", MeasurementMediaType.MEASUREMENT);
        
        fromMediaTypeMappings.put(AlarmMediaType.ALARM, "A");
        fromMediaTypeMappings.put(EventMediaType.EVENT, "E");
        fromMediaTypeMappings.put(MeasurementMediaType.MEASUREMENT, "M");
        
        toClassMappings.put("A", AlarmRepresentation.class);
        toClassMappings.put("E", EventRepresentation.class);
        toClassMappings.put("M", MeasurementRepresentation.class);
        
        fromClassMappings.put(AlarmRepresentation.class, "A");
        fromClassMappings.put(EventRepresentation.class, "E");
        fromClassMappings.put(MeasurementRepresentation.class, "M");
    }

    private String path;
    private CumulocityMediaType mediaType;
    private ResourceRepresentation representation;
    
    public HTTPPostRequest() {}

    public HTTPPostRequest(String path, CumulocityMediaType mediaType, ResourceRepresentation representation) {
        this.path = path;
        this.mediaType = mediaType;
        this.representation = representation;
    }

    public String getPath() {
        return path;
    }

    public CumulocityMediaType getMediaType() {
        return mediaType;
    }

    public ResourceRepresentation getRepresentation() {
        return representation;
    }
    
    public static HTTPPostRequest parseCsvString(String content) {
        String[] parts = content.split("##");
        return new HTTPPostRequest(parts[0], toMediaTypeMappings.get(parts[1]), JSONBase.fromJSON(parts[2], toClassMappings.get(parts[3])));
    }
    
    public String toCsvString() {
        return path + "##" + fromMediaTypeMappings.get(mediaType) + "##" + JSONBase.getJSONGenerator().forValue(representation) + "##" + fromClassMappings.get(representation.getClass());
    }
}
