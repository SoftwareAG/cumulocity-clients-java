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

package com.cumulocity.me.sdk.client;

import com.cumulocity.me.http.WebExceptionHandler;
import com.cumulocity.me.http.WebRequestWriter;
import com.cumulocity.me.http.WebResponseReader;
import com.cumulocity.me.http.impl.WebClientImpl;
import com.cumulocity.me.rest.RepresentationServicesFactory;
import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.validate.RepresentationValidationService;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.alarm.AlarmApi;
import com.cumulocity.me.sdk.client.alarm.AlarmApiImpl;
import com.cumulocity.me.sdk.client.audit.AuditRecordApi;
import com.cumulocity.me.sdk.client.audit.AuditRecordApiImpl;
import com.cumulocity.me.sdk.client.devicecontrol.DeviceControlApi;
import com.cumulocity.me.sdk.client.devicecontrol.DeviceControlApiImpl;
import com.cumulocity.me.sdk.client.event.EventApi;
import com.cumulocity.me.sdk.client.event.EventApiImpl;
import com.cumulocity.me.sdk.client.http.JsonRequestWriterImpl;
import com.cumulocity.me.sdk.client.http.JsonResponseReaderImpl;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.http.RestConnectorImpl;
import com.cumulocity.me.sdk.client.http.SDKWebExceptionHandler;
import com.cumulocity.me.sdk.client.identity.IdentityApi;
import com.cumulocity.me.sdk.client.identity.IdentityApiImpl;
import com.cumulocity.me.sdk.client.inventory.InventoryApi;
import com.cumulocity.me.sdk.client.inventory.InventoryApiImpl;
import com.cumulocity.me.sdk.client.measurement.MeasurementApi;
import com.cumulocity.me.sdk.client.measurement.MeasurementApiImpl;

public class PlatformImpl extends PlatformParameters implements Platform {

    private static final String PLATFORM_URL = "platform";

    public static final String CLIENT_PARAMETERS = "platformParameters";

    public static final String CUMULOCITY_PAGE_SIZE = "cumulocityPageSize";

    public static final String CUMULOCITY_PASSWORD = "cumulocityPassword";

    public static final String CUMULOCITY_USER = "cumulocityUser";

    public static final String CUMULOCITY_TENANT = "cumulocityTenant";

    public static final String CUMULOCITY_PORT = "cumulocityPort";

    public static final String CUMOLOCITY_HOST = "cumolocityHost";

    public static final String CUMOLOCITY_APPLICATION_KEY = "applicationKey";

    public static final String CUMOLOCITY_PROXY_HOST = "proxyHost";

    public static final String CUMULOCITY_PROXY_PORT = "proxyPort";

    public static final String CUMULOCITY_PROXY_USER = "proxyUser";

    public static final String CUMULOCITY_PROXY_PASSWORD = "proxyPassword";
    
    private JsonConversionService conversionService;
    
    private RepresentationValidationService validationService;

    private RestConnector restConnector;

    private TemplateUrlParser templateUrlParser;

    public PlatformImpl(String host, String tenantId, String user, String password, String applicationKey) {
        super(host, tenantId, user, password, applicationKey);
    }

    public PlatformImpl(String host, int port, String tenantId, String user, String password, String applicationKey) {
        super(getHostUrl(host, port), tenantId, user, password, applicationKey);
    }

    public PlatformImpl(String host, String tenantId, String user, String password, String applicationKey, int pageSize) {
        super(host, tenantId, user, password, applicationKey, pageSize);
    }

    public PlatformImpl(String host, int port, String tenantId, String user, String password, String applicationKey, int pageSize) {
        super(getHostUrl(host, port), tenantId, user, password, applicationKey, pageSize);
    }

    private static String getHostUrl(String host, int port) {
        return "http://" + host + ":" + port;
    }

    /**
     * This static method creates the Platform from the system parameters.
     * <p/>
     * System Properties
     * cumolocityHost     : ip address or name of the Cumulocity server
     * cumulocityPort     : port number of the Cumulocity server;
     * cumulocityTenant   : Tenant ID ;
     * cumulocityUser     : User ID ;
     * cumulocityPassword : Password ;
     * cumulocityPageSize : Page size for the paging parameters.
     * <p/>
     * proxyHost          : Proxy Host Name;
     * proxyPort          : Proxy Port Name
     * proxyUser          : Proxy User Name
     * proxyPassword      : Proxy Passowrd
     *
     * @return Platform for the handle to get other methods.
     * @throws SDKException
     */

    public static Platform createPlatform() {
        PlatformImpl platform = null;
        try {
            String host = System.getProperty(CUMOLOCITY_HOST);
            int port = Integer.parseInt(System.getProperty(CUMULOCITY_PORT));
            String tenantId = System.getProperty(CUMULOCITY_TENANT);
            String user = System.getProperty(CUMULOCITY_USER);
            String password = System.getProperty(CUMULOCITY_PASSWORD);
            String applicationKey = System.getProperty(CUMOLOCITY_APPLICATION_KEY);
            if (host == null || tenantId == null || user == null || password == null) {
                throw new SDKException("Cannot Create Platform as Mandatory Param are not set");
            }
            if (System.getProperty(CUMULOCITY_PAGE_SIZE) != null) {
                int pageSize = Integer.parseInt(System.getProperty(CUMULOCITY_PAGE_SIZE));
                platform = new PlatformImpl(host, port, tenantId, user, password, applicationKey, pageSize);
            } else {
                platform = new PlatformImpl(host, port, tenantId, user, password, applicationKey);
            }
            String proxyHost = System.getProperty(CUMOLOCITY_PROXY_HOST);
            int proxyPort = -1;
            if (System.getProperty(CUMULOCITY_PROXY_PORT) != null) {
                proxyPort = Integer.parseInt(System.getProperty(CUMULOCITY_PROXY_PORT));
            }
            String proxyUser = System.getProperty(CUMULOCITY_PROXY_USER);
            String proxyPassword = System.getProperty(CUMULOCITY_PROXY_PASSWORD);

            if (proxyHost != null && proxyPort > 0) {
                platform.setProxyHost(proxyHost);
                platform.setProxyPort(proxyPort);
            }

            if (proxyUser != null && proxyPassword != null) {
                platform.setProxyUserId(proxyUser);
                platform.setProxyPassword(proxyPassword);
            }
        } catch (NumberFormatException e) {
            throw new SDKException("Invalid Number :" + e.getMessage());
        }

        return platform;
    }

    public InventoryApi getInventoryApi() {
        return new InventoryApiImpl(getRestConnector(), getTemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }

    public MeasurementApi getMeasurementApi() {
        return new MeasurementApiImpl(getRestConnector(), getTemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }
    
    public AlarmApi getAlarmApi() {
        return new AlarmApiImpl(getRestConnector(), getTemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }
    
    public DeviceControlApi getDeviceControlApi() {
        return new DeviceControlApiImpl(getRestConnector(), getTemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }
    
    public IdentityApi getIdentityApi() {
        return new IdentityApiImpl(getRestConnector(), getTemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }

    public EventApi getEventApi() {
        return new EventApiImpl(getRestConnector(), getTemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }

    public AuditRecordApi getAuditRecordApi() {
        return new AuditRecordApiImpl(getRestConnector(), getTemplateUrlParser(), getHost() + PLATFORM_URL, getPageSize());
    }
    
    public void setConversionService(JsonConversionService conversionService) {
        assertNoApisCalled("ConversionService");
        this.conversionService = conversionService;
    }
    
    public JsonConversionService getConversionService() {
        if (conversionService == null) {
            conversionService = RepresentationServicesFactory.getInstance().getConversionService();
        }
        return conversionService;
    }
    
    public void setValidationService(RepresentationValidationService validationService) {
        assertNoApisCalled("ValidationService");
        this.validationService = validationService;
    }
    
    public RepresentationValidationService getValidationService() {
        if (validationService == null) {
            validationService = RepresentationServicesFactory.getInstance().getValidationService();
        }
        return validationService;
    }
    
    private RestConnector getRestConnector() {
        if (restConnector == null) {
            WebRequestWriter requestWriter = new JsonRequestWriterImpl(getValidationService(), getConversionService());
        	WebResponseReader responseReader = new JsonResponseReaderImpl(getConversionService());
        	WebExceptionHandler exceptionHandler = new SDKWebExceptionHandler();
        	WebClientImpl webClient = new WebClientImpl(requestWriter, responseReader, exceptionHandler);
            restConnector = new RestConnectorImpl(this, webClient);
        }
        return restConnector;
    }
    
    private TemplateUrlParser getTemplateUrlParser() {
        if (templateUrlParser == null) {
            templateUrlParser = new TemplateUrlParser();
        }
        return templateUrlParser;
    }
    
    private void assertNoApisCalled(String name) {
        if (restConnector != null) {
            throw new IllegalStateException(name + " can only by set before any API is called!");
        }
    }
}
