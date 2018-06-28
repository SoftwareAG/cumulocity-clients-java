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
package com.cumulocity.sdk.client.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.springframework.beans.factory.annotation.Autowired;

import com.cumulocity.rest.representation.tenant.TenantMediaType;
import com.cumulocity.sdk.client.PlatformImpl;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TenantCreator {

    private static final String TENANT_URI = "tenant/tenants";

    private final PlatformImpl platform;

    @Autowired
    public TenantCreator(PlatformImpl platform) {
        this.platform = platform;
    }

    public void createTenant() {
        Client httpClient = new HttpClientFactory().createClient();
        try {
            createTenant(httpClient);
        } finally {
            httpClient.destroy();
        }
    }

    private void createTenant(Client httpClient) {
        ClientResponse tr = postNewTenant(httpClient);
        assertThat(tr.getStatus(), is(201));
    }

    public void removeTenant() {
        Client httpClient = new HttpClientFactory().createClient();
        try {
            removeTenant(httpClient);
        } finally {
            httpClient.destroy();
        }
    }

    private void removeTenant(Client httpClient) {
        ClientResponse tenantResponse = deleteTenant(httpClient);
        assertThat(tenantResponse.getStatus(), is(204));
    }

    private ClientResponse postNewTenant(Client httpClient) {
        String host = platform.getHost();
        String tenantId = platform.getTenantId();
        WebResource.Builder resource = httpClient.resource(host + TENANT_URI).type(TenantMediaType.TENANT_TYPE);
        String tenantJson = "{ " +
                "\"id\": \"" + tenantId + "\", " +
                "\"domain\": \"sample-tenant.cumulocity.com\", " +
                "\"company\": \"sample-tenant\", " +
                "\"adminName\": \"" + platform.getUser() + "\", " +
                "\"adminPass\": \"" + platform.getPassword() + "\" " +
                "}";
        return resource.post(ClientResponse.class, tenantJson);
    }

    private ClientResponse deleteTenant(Client httpClient) {
        String host = platform.getHost();
        WebResource tenantResource = httpClient.resource(host + TENANT_URI + "/" + platform.getTenantId());
        return tenantResource.delete(ClientResponse.class);
    }
}
