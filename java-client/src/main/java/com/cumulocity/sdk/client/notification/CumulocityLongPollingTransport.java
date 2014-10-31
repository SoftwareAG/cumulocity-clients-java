/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.sdk.client.notification;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.security.Authentication;
import org.eclipse.jetty.client.security.BasicAuthentication;
import org.eclipse.jetty.client.security.SimpleRealmResolver;

import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

class CumulocityLongPollingTransport extends LongPollingTransport {

    private PlatformParameters paramters;

    private final Authentication authentication;

    CumulocityLongPollingTransport(Map<String, Object> options, HttpClient httpClient, PlatformParameters paramters) {
        super(options, httpClient);
        this.paramters = paramters;
        try {
            this.authentication = new BasicAuthentication(new PlatformPropertiesRealm(paramters));
        } catch (IOException e) {
            throw new SDKException("authentication failed", e);
        }
    }

    @Override
    protected void customize(ContentExchange exchange) {
        super.customize(exchange);
        exchange.addRequestHeader(RestConnector.X_CUMULOCITY_APPLICATION_KEY, paramters.getApplicationKey());
        try {
            authentication.setCredentials(exchange);
        } catch (IOException e) {
            throw new SDKException("authentication failed", e);
        }
    }
}
