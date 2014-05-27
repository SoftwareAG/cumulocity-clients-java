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
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

public class BufferedRequest {
    
    private static final Map<String, CumulocityMediaType> toMediaTypeMappings = new HashMap<String, CumulocityMediaType>(); 
    private static final Map<CumulocityMediaType, String> fromMediaTypeMappings = new HashMap<CumulocityMediaType, String>();
    
    private static final Map<String, Class<? extends ResourceRepresentation>> toClassMappings = new HashMap<String, Class<? extends ResourceRepresentation>>(); 
    private static final Map<Class<? extends ResourceRepresentation>, String> fromClassMappings = new HashMap<Class<? extends ResourceRepresentation>, String>();
    
    static {
        toMediaTypeMappings.put("A", AlarmMediaType.ALARM);
        toMediaTypeMappings.put("E", EventMediaType.EVENT);
        toMediaTypeMappings.put("M", MeasurementMediaType.MEASUREMENT);
        toMediaTypeMappings.put("D", DeviceControlMediaType.OPERATION);
        
        fromMediaTypeMappings.put(AlarmMediaType.ALARM, "A");
        fromMediaTypeMappings.put(EventMediaType.EVENT, "E");
        fromMediaTypeMappings.put(MeasurementMediaType.MEASUREMENT, "M");
        fromMediaTypeMappings.put(DeviceControlMediaType.OPERATION, "D");
        
        toClassMappings.put("A", AlarmRepresentation.class);
        toClassMappings.put("E", EventRepresentation.class);
        toClassMappings.put("M", MeasurementRepresentation.class);
        toClassMappings.put("D", OperationRepresentation.class);
        
        fromClassMappings.put(AlarmRepresentation.class, "A");
        fromClassMappings.put(EventRepresentation.class, "E");
        fromClassMappings.put(MeasurementRepresentation.class, "M");
        fromClassMappings.put(OperationRepresentation.class, "D");
    }

    private String path;
    private CumulocityMediaType mediaType;
    private ResourceRepresentation representation;
    private String method;
    
    public BufferedRequest() {}

    private BufferedRequest(String method, String path, CumulocityMediaType mediaType, ResourceRepresentation representation) {
        this.method = method;
        this.path = path;
        this.mediaType = mediaType;
        this.representation = representation;
    }
    
    public static BufferedRequest create(String method, String path, CumulocityMediaType mediaType, ResourceRepresentation representation) {
        if (!fromClassMappings.containsKey(representation.getClass())) {
            throw new IllegalArgumentException("Cannot create buffered request from class " + representation.getClass());
        }
        return new BufferedRequest(method, path, mediaType, representation);
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
    
    public String getMethod() {
        return method;
    }
    
    public static BufferedRequest parseCsvString(String content) {
        String[] parts = content.split("##", 5);
        return new BufferedRequest(parts[0], parts[1], toMediaTypeMappings.get(parts[2]), JSONBase.fromJSON(parts[4], toClassMappings.get(parts[3])));
    }
    
    public String toCsvString() {
        return method + "##" + path + "##" + fromMediaTypeMappings.get(mediaType) + "##"
                + fromClassMappings.get(representation.getClass()) + "##" + JSONBase.getJSONGenerator().forValue(representation);
    }
}
