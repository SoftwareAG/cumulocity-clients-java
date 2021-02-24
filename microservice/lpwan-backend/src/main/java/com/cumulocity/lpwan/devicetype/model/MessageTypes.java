package com.cumulocity.lpwan.devicetype.model;

import java.util.Map;

import com.cumulocity.lpwan.mapping.model.MessageTypeMapping;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageTypes {

    Map<String, MessageTypeMapping> messageTypeMappings;
    
    public MessageTypeMapping getMappingIndexesByMessageType(String messageTypeId) {
        return messageTypeMappings.get(messageTypeId);
    }
}
