package com.cumulocity.generic.mqtt;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericMqttMessage {

    private byte[] payload;
    private GenericMqttMetadata metadata;

}
