package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.reliable.notification.NotificationTokenRequestRepresentation;
import com.cumulocity.sdk.client.SDKException;

/**
 * API for creating and verifying Tokens used for new Notifications
 */
public interface TokenApi {

    /**
     * Creates new access Token.
     *
     * @param tokenRequest containing claim - subscriber, subscription and desired validity duration for the Token.
     * @return generated Token with JWT Token string
     * @throws IllegalArgumentException if the tokenClaim is null
     * @throws SDKException if the Token could not be created
     */
    Token create(NotificationTokenRequestRepresentation tokenRequest) throws IllegalArgumentException, SDKException;

    /**
     * Verifies supplied Token.
     *
     * @param token to be verified
     * @return TokenClaim if the supplied Token was successfully verified
     * @throws SDKException if the Token failed verification or could not be verified
     */
    TokenClaims verify(Token token) throws SDKException;

    /**
     * Refreshes an expired Token.
     *
     * @param token to be refreshed
     * @return refreshed Token, valid for the same duration of time as the original Token
     * @throws IllegalArgumentException if the supplied Token is null
     * @throws SDKException if the Token wasn't valid or the operation fails
     */
    Token refresh(Token token) throws IllegalArgumentException, SDKException;
}
