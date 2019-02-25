package com.cumulocity.microservice.customdecoders.api.model;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DecoderInputData implements Serializable {
    private String serviceKey;

    private String value;

    private Map<String, String> args;

    private String sourceDeviceId;

    private ExecutionEventStatus status = ExecutionEventStatus.PENDING;

    public static DecoderInputData fromMap(Map<String, Object> map) {
        DecoderInputData data = new DecoderInputData();
        data.setServiceKey((String) map.get("serviceKey"));
        data.setValue((String) map.get("value"));
        data.setArgs((Map<String, String>) map.get("args"));
        String status = (String) map.get("status");
        if (status != null) {
            data.setStatus(ExecutionEventStatus.valueOf(status));
        }
        return data;
    }
}
