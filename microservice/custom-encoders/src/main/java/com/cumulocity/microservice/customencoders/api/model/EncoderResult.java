package com.cumulocity.microservice.customencoders.api.model;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@Data
public class EncoderResult {
    private String encodedCommand;
    private Map<String, String> properties;

    private String message;

    private boolean success = true;

    public final EncoderResult setAsFailed(String message) {
        success = false;
        this.message = message;
        return this;
    }

    public static EncoderResult empty() {
        return new EncoderResult();
    }
}
