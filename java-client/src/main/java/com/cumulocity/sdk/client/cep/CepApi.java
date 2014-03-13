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
package com.cumulocity.sdk.client.cep;

import java.io.Reader;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.cep.notification.CepCustomNotificationsSubscriber;

/**
 * API for integration with Custom Event Processing modules from the platform.
 *
 */
public interface CepApi {
    
    /**
     * Gets the notifications subscriber, which allows to receive notifications sent from cep.
     * <pre>
     * <code>
     * Example:
     * 
     *  Subscriber<String, Object> subscriber = deviceControlApi.getNotificationsSubscriber();
     *  
     *  subscriber.subscirbe( "channelId" , new SubscriptionListener<String, Object>() {
     *
     *      {@literal @}Override
     *      public void onNotification(Subscription<GId> subscription, Object operation) {
     *             //process notification from cep module 
     *      }
     *    
     *      {@literal @}Override
     *      public void onError(Subscription<GId> subscription, Throwable ex) {
     *          // handle subscribe error
     *      }
     *  });
     *  </code>
     *  </pre>
     * 
     * @return subscriber 
     * @throws SDKException
     */
    CepCustomNotificationsSubscriber getCustomNotificationsSubscriber();
    
    CepModuleRepresentation create(Reader content);
    
    CepModuleRepresentation update(Reader content);
    
    void delete(CepModuleRepresentation module);
    
}
