package com.cumulocity.sdk.client.notification;

import java.net.URI;

import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.PartialRequestBuilder;
import com.sun.jersey.client.impl.ClientRequestImpl;

class BayeuxRequestBuilder extends PartialRequestBuilder<BayeuxRequestBuilder> {

    private final URI uri;

    public BayeuxRequestBuilder(URI uri) {
        this.uri = uri;
    }

    public ClientRequest build(Object e) {
        ClientRequest ro = new ClientRequestImpl(uri, "POST", e, metadata);
        entity = null;
        metadata = null;
        return ro;
    }
}
