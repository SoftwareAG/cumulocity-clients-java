package com.cumulocity.microservice.platform.api.client;

import com.cumulocity.sdk.client.PlatformParameters;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import java.util.concurrent.Callable;

public class InternalTrafficDecorator<K> {

    private final String TOTP_SECRET = "Q436QSIBB3OWS5R7";
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();
    private final PlatformParameters platform;
    private final Callable<K> callable;

    private InternalTrafficDecorator(PlatformParameters platform, Callable<K> callable) {
        this.platform = platform;
        this.callable = callable;
    }

    private K asInternal() {
        K result = null;
        try {
            platform.registerInterceptor(InternalTrafficHeader.from(getTotpCode()));
            result = callable.call();
        } catch (Exception e) {
            Throwables.propagate(e);
        } finally {
            platform.unregisterInterceptor(InternalTrafficHeader.EMPTY);
        }
        return result;
    }

    private String getTotpCode() {
        return "" + gAuth.getTotpPassword(TOTP_SECRET);
    }

    public static class Builder {
        private PlatformParameters platform;

        private Builder() {

        }

        public static Builder internally() {
            return new Builder();
        }

        public static Builder internal(PlatformParameters platform) {
            return new InternalTrafficDecorator.Builder().onPlatform(platform);
        }

        public Builder onPlatform(PlatformParameters platform) {
            this.platform = platform;
            return this;
        }

        public <K> K doAction(Callable<K> callable) {
            Preconditions.checkNotNull(platform);
            return new InternalTrafficDecorator<>(platform, callable).asInternal();
        }

        public void doAction(final Runnable runnable) {
            doAction(new Callable<Object>() {
                public Object call() {
                    runnable.run();
                    return null;
                }
            });
        }
    }

}
