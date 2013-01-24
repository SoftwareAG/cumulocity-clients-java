/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client;

import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.util.StringUtils;

public class PlatformParameters {

    private final String host;

    private final String tenantId;

    private final String user;

    private final String password;
    
    private final String applicationKey;
    
    private final int pageSize;

    private String proxyHost;

    private int proxyPort = -1;

    private String proxyUserId;

    private String proxyPassword;
    
    private boolean requireResponseBody = true;

    public PlatformParameters(String host, String tenantId, String user, String password, String applicationKey) {
        this(host, tenantId, user, password, applicationKey, PagedCollectionResource.DEFAULT_PAGE_SIZE);
    }

    public PlatformParameters(String host, String tenantId, String user, String password, String applicationKey, int pageSize) {
        this.host = StringUtils.ensureTail(host, "/");
        this.tenantId = tenantId;
        this.user = user;
        this.password = password;
        this.applicationKey = applicationKey;
        this.pageSize = pageSize;
    }

    public String getHost() {
        return host;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getApplicationKey() {
        return applicationKey;
    }

    public int getPageSize() {
        return pageSize;
    }
    
    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUserId() {
        return proxyUserId;
    }

    public void setProxyUserId(String proxyUserId) {
        this.proxyUserId = proxyUserId;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public boolean isRequireResponseBody() {
        return requireResponseBody;
    }

    public void setRequireResponseBody(boolean requireResponseBody) {
        this.requireResponseBody = requireResponseBody;
    }
}
