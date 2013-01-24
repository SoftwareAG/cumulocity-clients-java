package com.cumulocity.me.http.impl;

import com.cumulocity.me.http.WebClient;
import com.cumulocity.me.http.WebException;
import com.cumulocity.me.http.WebExceptionHandler;
import com.cumulocity.me.http.WebRequest;
import com.cumulocity.me.http.WebRequestBuilder;
import com.cumulocity.me.http.WebRequestWriter;
import com.cumulocity.me.http.WebResponse;
import com.cumulocity.me.http.WebResponseReader;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

public class WebClientImpl implements WebClient {

    private final WebRequestWriter requestWriter;
    private final WebResponseReader responseReader;
    private final WebExceptionHandler exceptionHandler;

    public WebClientImpl(WebRequestWriter requestWriter, WebResponseReader responseReader, WebExceptionHandler exceptionHandler) {
        this.requestWriter = requestWriter;
        this.responseReader = responseReader;
        this.exceptionHandler = exceptionHandler;
    }
    
    public WebRequestBuilder request(String path) {
        return new WebRequestBuilderImpl(this, requestWriter, path);
    }

    public WebResponse handle(WebRequest request) {
        WebRequestExecutor requestExecutor = null;
        try {
            requestExecutor = new WebRequestExecutor(request);
            return requestExecutor.execute();
        } catch (WebException e) {
            return exceptionHandler.handle(e);
        } finally {
            requestExecutor.close();
        }
    }
    
    public CumulocityResourceRepresentation handle(WebRequest request, int expectedStatus, Class responseEntityType) {
        WebResponse response = handle(request);
        return responseReader.read(response, expectedStatus, responseEntityType);
    }
}
