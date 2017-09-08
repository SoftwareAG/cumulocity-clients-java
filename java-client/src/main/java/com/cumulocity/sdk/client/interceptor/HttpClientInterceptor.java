package com.cumulocity.sdk.client.interceptor;

import com.sun.jersey.api.client.WebResource.Builder;

/**
 * Interceptor will ce called before http method will be executed
 */
public interface HttpClientInterceptor {

    Builder apply(Builder builder);

}
