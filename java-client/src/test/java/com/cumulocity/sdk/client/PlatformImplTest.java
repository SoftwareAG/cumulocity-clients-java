package com.cumulocity.sdk.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        assertEquals("password", platformImpl.getPassword());

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
        assertEquals("password", platformImpl.getPassword());
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
