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

import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.authentication.CumulocityOAuthCredentials;
import com.cumulocity.rest.representation.tenant.TenantMediaType;
import com.cumulocity.sdk.client.PlatformImpl;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TenantCreator {

    private static final String TENANT_URI = "tenant/tenants";

    private final PlatformImpl platform;

    @Autowired
    public TenantCreator(PlatformImpl platform) {
        this.platform = platform;
    }

    public void createTenant() throws IOException {
        Client httpClient = new HttpClientFactory().createClient();
        try {
            createTenant(httpClient);
        } finally {
            httpClient.close();
        }
    }

    private void createTenant(Client httpClient) {
        Response tr = postNewTenant(httpClient);
        assertThat(tr.getStatus(), is(201));
    }

    public void removeTenant() throws IOException {
        Client httpClient = new HttpClientFactory().createClient();
        try {
            removeTenant(httpClient);
        } finally {
            httpClient.close();
        }
    }

    private void removeTenant(Client httpClient) {
        Response tenantResponse = deleteTenant(httpClient);
        assertThat(tenantResponse.getStatus(), is(204));
    }

    private Response postNewTenant(Client httpClient) {
        String host = platform.getHost();
        final Invocation.Builder resource = httpClient.target(host + TENANT_URI).request(TenantMediaType.TENANT_TYPE).accept(TenantMediaType.TENANT_TYPE);
        CumulocityCredentials.CumulocityCredentialsVisitor<Response> visitor = new CumulocityCredentials.CumulocityCredentialsVisitor<Response>() {
            @Override
            public Response visit(CumulocityBasicCredentials credentials) {
                String tenantJson = "{ " +
                        "\"id\": \"" + platform.getTenantId() + "\", " +
                        "\"domain\": \"sample-tenant.cumulocity.com\", " +
                        "\"company\": \"sample-tenant\", " +
                        "\"adminName\": \"" + credentials.getUsername() + "\", " +
                        "\"adminPass\": \"" + credentials.getPassword() + "\", " +
                        "\"adminEmail\": \"admin@sample-tenant.com\" " +
                        "}";
                Response result = resource.post(Entity.json(tenantJson));
                String newTenant = result.readEntity(String.class);
                Gson gson = new Gson();
                HashMap map = gson.fromJson(newTenant, HashMap.class);
                String newTenantId = (String)map.get("id");
                ((CumulocityBasicCredentials)platform.getCumulocityCredentials()).setTenantId(newTenantId);
                return result;
            }

            @Override
            public Response visit(CumulocityOAuthCredentials credentials) {
                throw new IllegalStateException("Cannot use credentials other than oauth yet");
            }
        };
        return platform.getCumulocityCredentials().accept(visitor);
    }

    private Response deleteTenant(Client httpClient) {
        String host = platform.getHost();
        WebTarget tenantResource = httpClient.target(host + TENANT_URI + "/" + platform.getTenantId()).queryParam("synchronous", "true");
        return tenantResource.request().delete();
    }
}
