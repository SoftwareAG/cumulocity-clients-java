package com.cumulocity.sdk.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;


/**
 * Object that is used to manage underlying http client
 */
@Builder(builderMethodName = "httpConfig", toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Value
public class HttpClientConfig {

    private static final int DEFAULT_READ_TIMEOUT_IN_MILLIS = 180000;

    @Default
    private final ConnectionPoolConfig pool = ConnectionPoolConfig.connectionPool().build();

    @Default
    private final int httpReadTimeout = DEFAULT_READ_TIMEOUT_IN_MILLIS;

}
