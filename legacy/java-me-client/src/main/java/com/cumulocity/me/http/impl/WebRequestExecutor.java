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

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.cumulocity.me.http.WebException;
import com.cumulocity.me.http.WebMethod;
import com.cumulocity.me.http.WebRequest;
import com.cumulocity.me.http.WebResponse;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.Map.Entry;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.util.IOUtils;
import com.cumulocity.me.util.StringUtils;

public class WebRequestExecutor {
    
    private final WebRequest request;
    private HttpConnection connection;

    public WebRequestExecutor(WebRequest request) {
        this.request = request;
    }
    
    public WebResponse execute() {
        try {
            return openConnection()
                .writeCommand()
                .writeHeaders()
                .writeBody()
                .buildResponse();
        } catch (IOException e) {
            throw new SDKException(e.getMessage(), e);
        }
    }

    private WebRequestExecutor openConnection() throws IOException {
        connection = (HttpConnection) Connector.open(request.getPath());
        return this;
    }

    private WebRequestExecutor writeCommand() throws IOException {
        connection.setRequestMethod(getRequestMethod().getName());
        return this;
    }

    private WebMethod getRequestMethod() throws IOException {
        WebMethod requestMethod = request.getMethod();
        if (requestMethod.isSupportedByJ2me()) {
            return requestMethod;
        } else {
            return WebMethod.POST;
        }
    }

    private WebRequestExecutor writeHeaders() throws IOException {
        Iterator entries = request.getHeaders().entrySet().iterator();
        while (entries.hasNext()) {
            Entry thisEntry = (Entry) entries.next();
            String headerName = StringUtils.toString(thisEntry.getKey());
            String headerValue = StringUtils.toString(thisEntry.getValue());
            connection.setRequestProperty(headerName, headerValue);
        }
        WebMethod requestMethod = request.getMethod();
        if (!requestMethod.isSupportedByJ2me()) {
            connection.setRequestProperty("X-HTTP-METHOD", requestMethod.getName());
        }
        return this;
    }
    
    private WebRequestExecutor writeBody() throws IOException {
        if (request.getData() == null) {
            return this;
        }
        IOUtils.writeData(request.getData(), connection.openOutputStream());
        return this;
    }

    private WebResponse buildResponse() throws IOException {
        WebResponseImpl response = new WebResponseImpl(connection);
        if (response.isSuccessful()) {
            return response;
        } else {
            throw new WebException(response);
        }
    }

    public void close() {
        IOUtils.closeQuietly(connection);
    }
}
