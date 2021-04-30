package com.cumulocity.sdk.client.reliable;

import com.cumulocity.rest.representation.reliable.notification.NotificationTokenClaimsRepresentation;
import com.cumulocity.sdk.client.SDKException;

import java.util.Map;

public interface TokenApi {

    String create(NotificationTokenClaimsRepresentation tokenClaim) throws IllegalArgumentException, SDKException;

    TokenVerification verify(String token) throws SDKException ;
}
