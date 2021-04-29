package com.cumulocity.sdk.client.reliable;

import com.cumulocity.rest.representation.reliable.notification.NotificationTokenClaimsRepresentation;

import java.util.Map;

public interface TokenApi {

    String create(NotificationTokenClaimsRepresentation tokenClaim);

    Map<String, Object> verify(String token);
}
