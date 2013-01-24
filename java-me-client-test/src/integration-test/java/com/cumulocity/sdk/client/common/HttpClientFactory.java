package com.cumulocity.sdk.client.common;

import com.cumulocity.me.sdk.client.http.RestConnector;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;

public class HttpClientFactory {

    public Client createClient() {
        Client client = ApacheHttpClient.create();
        client.setFollowRedirects(true);
        client.addFilter(new HTTPBasicAuthFilter("management/admin", "Pyi1bo1r"));
        client.addFilter(new ApplicationKeyFilter());
        return client;
    }

    private static class ApplicationKeyFilter extends ClientFilter {
        @Override
        public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
            cr.getHeaders().add(RestConnector.X_CUMULOCITY_APPLICATION_KEY, "managementSecretKey123");
            return getNext().handle(cr);
        }
    }
}
