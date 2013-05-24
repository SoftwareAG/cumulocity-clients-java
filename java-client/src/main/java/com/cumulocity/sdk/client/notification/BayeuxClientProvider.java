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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

public abstract class BayeuxClientProvider {
    
    public static BayeuxClientProvider getInstance(){
        return BayeuxClientProviderSingleton.instance;
    }
    
    public abstract BayeuxClient get(String url);
    
    
    private static class BayeuxClientProviderSingleton {
        private static final BayeuxClientProvider instance =  new BayeuxClientProvider(){
            
            @Override
            public BayeuxClient get(String url) {
                final Map<String,Object> options = new HashMap<String,Object>();
                options.put(ClientTransport.JSON_CONTEXT, new SvensonJSONContextClient());
                return new BayeuxClient(url, LongPollingTransport.create(options, HttpClientSingleton.instance));
            }
            
        };
    }

    private static class HttpClientSingleton {
        private static final HttpClient instance = new HttpClient();
    }
}
