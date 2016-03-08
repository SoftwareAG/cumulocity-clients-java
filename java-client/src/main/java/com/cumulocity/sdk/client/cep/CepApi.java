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

import java.io.InputStream;
import java.io.Reader;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.alarm.AlarmCollection;
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
    
    /**
     * Gets an cep module by id
     *
     * @param id of the cep module to search for
     * @return the cep module with the given id
     * @throws SDKException if the cep module is not found or if the query failed
     */
    CepModuleRepresentation get(String id);
    
    /**
     * Creates an cep module in the platform.
     *
     * @param content input stream to resource with cep module definition 
     * @return the created cep module with the generated id
     * @throws SDKException if the cep module could not be created
     */
    @Deprecated
    CepModuleRepresentation create(InputStream content);
    
    /**
     * Creates an cep module in the platform.
     *
     * @param content of cep module definition 
     * @return the created cep module with the generated id
     * @throws SDKException if the cep module could not be created
     */
    CepModuleRepresentation create(String content);
    /**
     * Updates an cep module in the platform.
     * The cep module to be updated is identified by the id.
     *
     * @param id of cep module to update
     * @param content input stream to resource with cep module definition 
     * @return the updated cep module
     * @throws SDKException if the cep module could not be updated
     */
    CepModuleRepresentation update(String id, InputStream content);
    
    CepModuleRepresentation update(CepModuleRepresentation module);

    /**
     * Gets all cep modules from the platform
     *
     * @return collection of cep modules with paging functionality
     * @throws SDKException if the query failed
     */
    CepModuleCollection getModules();
    
    /**
     * Deletes the cep module from the platform.
     *
     * @throws SDKException
     */
    void delete(CepModuleRepresentation module);
    
    /**
     * Deletes the cep module from the platform.
     *
     * @throws SDKException
     */
    void delete(String id);
    
}
