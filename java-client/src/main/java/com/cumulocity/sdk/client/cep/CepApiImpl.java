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

import com.cumulocity.rest.representation.CumulocityMediaType;
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
    public CepModuleRepresentation create(Reader content) {
        return restConnector.post(cepModulesUrl(), CumulocityMediaType.MULTIPART_FORM_DATA_TYPE, content);
    }

    private String cepModulesUrl() {
        return url+"/modules";
    }

    @Override
    public CepModuleRepresentation update(Reader content) {
        return restConnector.put(cepModulesUrl(), CumulocityMediaType.MULTIPART_FORM_DATA_TYPE, content);
    }

    @Override
    public void delete(CepModuleRepresentation module) {
        restConnector.delete(url+ "/" + module.getId());
    }

}
