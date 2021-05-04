package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.reliable.notification.NotificationTokenClaimsRepresentation;
import com.cumulocity.sdk.client.SDKException;

public interface TokenApi {

    Token create(NotificationTokenClaimsRepresentation tokenClaim) throws IllegalArgumentException, SDKException;

    TokenDetails verify(String token) throws SDKException ;
}
