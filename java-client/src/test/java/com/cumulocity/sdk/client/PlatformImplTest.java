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
package com.cumulocity.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import org.junit.Test;

public class PlatformImplTest {

    @Test
    public void testCreationFromSystemParams() throws SDKException {

        //Given
        System.setProperty(PlatformImpl.CUMOLOCITY_HOST, "abdc");
        System.setProperty(PlatformImpl.CUMULOCITY_PORT, "123");
        System.setProperty(PlatformImpl.CUMULOCITY_TENANT, "tenant");
        System.setProperty(PlatformImpl.CUMULOCITY_USER, "user");
        System.setProperty(PlatformImpl.CUMULOCITY_PASSWORD, "password");

        // when
        PlatformImpl platformImpl = (PlatformImpl) PlatformImpl.createPlatform();

        //then
        assertEquals("http://abdc:123/", platformImpl.getHost());
        assertEquals("tenant", platformImpl.getTenantId());
        assertEquals("user", platformImpl.getUser());
        assertEquals("password", ((CumulocityBasicCredentials)platformImpl.getCumulocityCredentials()).getPassword());

    }

    @Test
    public void testCreationFromSystemParamsWithPageSize() throws SDKException {

        //Given
        System.setProperty(PlatformImpl.CUMOLOCITY_HOST, "abdc");
        System.setProperty(PlatformImpl.CUMULOCITY_PORT, "123");
        System.setProperty(PlatformImpl.CUMULOCITY_TENANT, "tenant");
        System.setProperty(PlatformImpl.CUMULOCITY_USER, "user");
        System.setProperty(PlatformImpl.CUMULOCITY_PASSWORD, "password");
        System.setProperty(PlatformImpl.CUMULOCITY_PAGE_SIZE, "9");

        // when
        PlatformImpl platformImpl = (PlatformImpl) PlatformImpl.createPlatform();

        //then
        assertEquals("http://abdc:123/", platformImpl.getHost());
        assertEquals("tenant", platformImpl.getTenantId());
        assertEquals("user", platformImpl.getUser());
        assertEquals("password", ((CumulocityBasicCredentials)platformImpl.getCumulocityCredentials()).getPassword());
        assertEquals(9, platformImpl.getPageSize());

    }

    @Test(expected = SDKException.class)
    public void testCreationFromSystemParamsInvalidPort() throws SDKException {

        //Given
        System.setProperty(PlatformImpl.CUMOLOCITY_HOST, "abdc");
        System.setProperty(PlatformImpl.CUMULOCITY_PORT, "XYz");
        System.setProperty(PlatformImpl.CUMULOCITY_TENANT, "tenant");
        System.setProperty(PlatformImpl.CUMULOCITY_USER, "user");
        System.setProperty(PlatformImpl.CUMULOCITY_PASSWORD, "password");

        // when
        PlatformImpl.createPlatform();

        //then
        fail();

    }

    @Test
    public void testCreationFromSystemParamsWithProxy() throws SDKException {

        //Given
        System.setProperty(PlatformImpl.CUMOLOCITY_HOST, "abdc");
        System.setProperty(PlatformImpl.CUMULOCITY_PORT, "123");
        System.setProperty(PlatformImpl.CUMULOCITY_TENANT, "tenant");
        System.setProperty(PlatformImpl.CUMULOCITY_USER, "user");
        System.setProperty(PlatformImpl.CUMULOCITY_PASSWORD, "password");
        System.setProperty(PlatformImpl.CUMOLOCITY_PROXY_HOST, "proxyHost");
        System.setProperty(PlatformImpl.CUMULOCITY_PROXY_PORT, "8");
        System.setProperty(PlatformImpl.CUMULOCITY_PROXY_USER, "proxyUser");
        System.setProperty(PlatformImpl.CUMULOCITY_PROXY_PASSWORD, "password");

        // when
        PlatformImpl platformImpl = (PlatformImpl) PlatformImpl.createPlatform();

        //then 
        assertEquals("proxyHost", platformImpl.getProxyHost());
        assertEquals(8, platformImpl.getProxyPort());
        assertEquals("proxyUser", platformImpl.getProxyUserId());
        assertEquals("password", platformImpl.getProxyPassword());

    }

}
