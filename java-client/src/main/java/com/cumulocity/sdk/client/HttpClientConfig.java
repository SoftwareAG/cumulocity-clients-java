package com.cumulocity.sdk.client;

import lombok.*;
import lombok.Builder.Default;


/**
 * Object that is used to manage underlying http client
 */
@Data
@Builder(builderMethodName = "httpConfig", toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class HttpClientConfig {

    private static final int DEFAULT_READ_TIMEOUT_IN_MILLIS = 180000;

    @Default
    private ConnectionPoolConfig pool = ConnectionPoolConfig.connectionPool().build();

    @Default
    private int httpReadTimeout = DEFAULT_READ_TIMEOUT_IN_MILLIS;

}
