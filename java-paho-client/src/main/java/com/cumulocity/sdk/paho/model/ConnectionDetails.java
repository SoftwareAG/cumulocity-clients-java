package com.cumulocity.sdk.paho.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
@AllArgsConstructor
public class ConnectionDetails {

    @NotNull
    private String host;

    @NotNull
    private String clientId;

    @NotNull
    private String userName;

    @NotNull
    private String password;

    private boolean cleanSession;
}
