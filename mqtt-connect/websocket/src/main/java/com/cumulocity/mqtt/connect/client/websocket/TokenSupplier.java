package com.cumulocity.mqtt.connect.client.websocket;

import com.cumulocity.rest.representation.reliable.notification.NotificationTokenRequestRepresentation;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

class TokenSupplier {

    private static long TOKEN_EXPIRATION_IN_MINUTES = 1440;
    private static long CACHE_EXPIRATION_IN_MINUTES = 1320;
    private static final String TOKEN_TYPE = "mqtt";

    private final TokenApi tokenApi;
    private final String topic;
    private final String subscriber;
    private final Supplier<String> memoizedTokenSupplier;

    TokenSupplier(TokenApi tokenApi, String topic, String subscriber) {
        this.tokenApi = tokenApi;
        this.topic = topic;
        this.subscriber = subscriber;
        this.memoizedTokenSupplier = Suppliers.memoizeWithExpiration(() -> create(), CACHE_EXPIRATION_IN_MINUTES, TimeUnit.MINUTES);
    }

    public String get() {
        return memoizedTokenSupplier.get();
    }

    private String create() {
        return tokenApi
                .create(getTokenRepresentation())
                .getTokenString();
    }

    private NotificationTokenRequestRepresentation getTokenRepresentation() {
        final NotificationTokenRequestRepresentation representation = new NotificationTokenRequestRepresentation();
        representation.setSubscriber(subscriber + randomAlphabetic(10));
        representation.setSubscription(topic);
        representation.setType(TOKEN_TYPE);
        representation.setSigned(true);
        representation.setExpiresInMinutes(TOKEN_EXPIRATION_IN_MINUTES);
        representation.setShared(false);
        representation.setNonPersistent(false);
        return representation;
    }

}
