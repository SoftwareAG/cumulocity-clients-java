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
