/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.customdecoders.api.model;

import com.cumulocity.microservice.customdecoders.api.util.DecoderUtils;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Decoder implements Serializable{
    public static final String DECODER_EVENT_TYPE = "c8y_decoder_event";
    public static final String DECODER_DATA_FRAGMENT = "decoderData";

    private String serviceKey;
    private String displayName;

    public EventRepresentation createEvent(byte[] value, ManagedObjectRepresentation deviceMo, Map<String, String> args) {
        EventRepresentation event = new EventRepresentation();

        event.setText("External decoder data received from device. Decoder: "+serviceKey+" ("+displayName+")");
        event.setType(DECODER_EVENT_TYPE);
        event.setDateTime(new DateTime());
        event.setSource(deviceMo);
        event.set(new DecoderInputData(serviceKey,
                DecoderUtils.toHexString(value), args, deviceMo.getId().getValue(), ExecutionEventStatus.PENDING), DECODER_DATA_FRAGMENT);
        return event;
    }
}
