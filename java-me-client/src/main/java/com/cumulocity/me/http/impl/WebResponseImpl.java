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

import javax.microedition.io.HttpConnection;

import com.cumulocity.me.http.WebResponse;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.util.IOUtils;

public class WebResponseImpl implements WebResponse {
    
    private final int status;
    private final String message;
    private final Map headers;
    private final byte[] data;

    public WebResponseImpl(HttpConnection connection) throws IOException {
        this.status = connection.getResponseCode();
        this.message = connection.getResponseMessage();
        this.headers = readHeaders(connection);
        this.data = readData(connection);
    }

    public boolean isSuccessful() {
        return status < 300;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Map getHeaders() {
        return headers;
    }
    
    public byte[] getData() {
        return data;
    }
    
    private Map readHeaders(HttpConnection connection) throws IOException {
        Map headers = new HashMap();
        String key = null;
        int i = 0;
        while ((key = connection.getHeaderFieldKey(i)) != null) {
            headers.put(key, connection.getHeaderField(i));
            i++;
        }
        return headers;
    }
    
    private byte[] readData(HttpConnection connection) throws IOException {
        if (!isSuccessful()) {
            return null;
        }
        int length = (int) connection.getLength();
        if (length > 0) {
            return IOUtils.readData(length, connection.openInputStream());
        } else {
            return null;
        }
    }
}