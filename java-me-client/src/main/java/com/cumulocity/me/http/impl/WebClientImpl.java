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
