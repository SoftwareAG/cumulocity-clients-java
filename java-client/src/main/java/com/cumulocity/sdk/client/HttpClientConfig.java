package com.cumulocity.sdk.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;


/**
 * Object that is used to managed underlying http client
 */
@Builder(builderMethodName = "httpConfig", toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Value
public class HttpClientConfig {
    @Default
    private final ConnectionPoolConfig pool = ConnectionPoolConfig.connectionPool().build();

}
