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

import static com.cumulocity.rest.representation.cep.CepMediaType.CEP_MODULE;

import java.io.InputStream;
import java.io.Reader;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.cep.CepMediaType;
import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.UrlProcessor;
import com.cumulocity.sdk.client.cep.notification.CepCustomNotificationsSubscriber;

public class CepApiImpl implements CepApi {

    private final PlatformParameters platformParameters;

    private final RestConnector restConnector;

    private final String url;

    private final int pageSize;

    private final UrlProcessor urlProcessor;

    public CepApiImpl(PlatformParameters platformParameters, RestConnector restConnector, UrlProcessor urlProcessor, int pageSize) {
        this.platformParameters = platformParameters;
        this.restConnector = restConnector;
        this.urlProcessor = urlProcessor;
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

    private String cepModulesUrl() {
        return url + "/modules";
    }

    @Override
    public CepModuleRepresentation update(String id, InputStream content) {
        return restConnector.putStream(cepModuleUrl(id), CEP_MODULE, content, CepModuleRepresentation.class);
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
    public CepModuleRepresentation get(String id) {
        return restConnector.get(cepModuleUrl(id),CEP_MODULE, CepModuleRepresentation.class);
    }

    @Override
    public CepModuleCollection getModules() {
        return new CepModuleCollectionImpl(restConnector, cepModulesUrl(), pageSize);
    }

}
