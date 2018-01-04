package com.cumulocity.microservice.platform.api.client;

import com.cumulocity.sdk.client.interceptor.HttpClientInterceptor;
import com.sun.jersey.api.client.WebResource;

public class InternalTrafficHeader implements HttpClientInterceptor {

    public static final InternalTrafficHeader EMPTY = from("EMPTY");
    private final String key = "X-Cumulocity-Internal-Traffic";
    private final String value;

    private InternalTrafficHeader(String value) {
        this.value = value;
    }

    public static InternalTrafficHeader from(String value) {
        return new InternalTrafficHeader(value);
    }

    @Override
    public WebResource.Builder apply(WebResource.Builder builder) {
        return builder.header(key, value);
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof InternalTrafficHeader)) return false;
        final InternalTrafficHeader other = (InternalTrafficHeader) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$key = this.key;
        final Object other$key = other.key;
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $key = this.key;
        result = result * PRIME + ($key == null ? 43 : $key.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof InternalTrafficHeader;
    }
}
