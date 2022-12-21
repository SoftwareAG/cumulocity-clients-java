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

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.cep.notification.CepCustomNotificationsSubscriber;

import java.io.InputStream;

import static com.cumulocity.rest.representation.cep.CepMediaType.CEP_MODULE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class CepApiImpl implements CepApi {

    private final PlatformParameters platformParameters;

    private final RestConnector restConnector;

    private final String url;

    private final int pageSize;


    public CepApiImpl(PlatformParameters platformParameters, RestConnector restConnector, int pageSize) {
        this.platformParameters = platformParameters;
        this.restConnector = restConnector;
        this.pageSize = pageSize;
        this.url = platformParameters.getHost() + "cep";
    }

    @Override
    public CepCustomNotificationsSubscriber getCustomNotificationsSubscriber() {
        return new CepCustomNotificationsSubscriber(platformParameters);
    }

    @Override
    public CepModuleRepresentation create(InputStream content) {
        return restConnector.postStream(cepModulesUrl(), CEP_MODULE, content, CepModuleRepresentation.class);
    }
    
    @Override
    public CepModuleRepresentation create(String content) {
        return restConnector.postText(cepModulesUrl(), content, CepModuleRepresentation.class);
    }

    private String cepModulesUrl() {
        return url + "/modules";
    }

    @Override
    public CepModuleRepresentation update(String id, InputStream content) {
        return restConnector.putStream(cepModuleUrl(id), CEP_MODULE, content, CepModuleRepresentation.class);
    }

    @Override
    public CepModuleRepresentation update(String id, String content) {
        return restConnector.putText(cepModuleUrl(id), content, CepModuleRepresentation.class);
    }

    @Override
    public CepModuleRepresentation update(CepModuleRepresentation module) {
        return restConnector.put(cepModuleUrl(module.getId()), CEP_MODULE, module);
    }

    private String cepModuleUrl(String id) {
        return cepModulesUrl()+"/"+ id;
    }

    @Override
    public void delete(CepModuleRepresentation module) {
        delete(module.getId());
    }
    
    @Override
    public void delete(String id) {
        restConnector.delete(cepModuleUrl(id));
    }

    @Override
    public <T> T health(Class<T> clazz) {
        return restConnector.get(url + "/health", APPLICATION_JSON_TYPE, clazz);
    }

    @Override
    public CepModuleRepresentation get(String id) {
        return restConnector.get(cepModuleUrl(id), CEP_MODULE, CepModuleRepresentation.class);
    }
    
    @Override
    public String getText(String id) {
        return restConnector.get(cepModuleUrl(id), CumulocityMediaType.TEXT_PLAIN_TYPE, String.class);
    }

    @Override
    public CepModuleCollection getModules() {
        return new CepModuleCollectionImpl(restConnector, cepModulesUrl(), pageSize);
    }

}
