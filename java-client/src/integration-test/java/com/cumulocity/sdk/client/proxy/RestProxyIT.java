package com.cumulocity.sdk.client.proxy;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RestProxyIT extends BaseProxyIT {
    @Test
    public void shouldSendRequestThroughNotAuthenticatedProxy() {
        // Given
        givenAuthenticatedProxyAndProxiedPlatform(null, null);

        // When
        List<ManagedObjectRepresentation> managedObjects = proxiedPlatform.getInventoryApi()
                .getManagedObjects()
                .get(5)
                .getManagedObjects();

        // Then
        assertNotNull(managedObjects);
        assertFalse(managedObjects.isEmpty());
    }

    @Test
    public void shouldSendRequestThroughAuthenticatedProxy() {
        // Given
        givenAuthenticatedProxyAndProxiedPlatform(PROXY_AUTH_USERNAME, PROXY_AUTH_PASSWORD);

        // When
        List<ManagedObjectRepresentation> managedObjects = proxiedPlatform.getInventoryApi()
                .getManagedObjects()
                .get(5)
                .getManagedObjects();

        // Then
        assertNotNull(managedObjects);
        assertFalse(managedObjects.isEmpty());
    }

    @Test
    public void shouldFailToSendRequestWhenInvalidAuthentication() {
        // Given
        givenAuthenticatedProxy(PROXY_AUTH_USERNAME, PROXY_AUTH_PASSWORD);
        givenAuthenticatedProxiedPlatform("invalid-user", "invalid-password");

        // When
        try {
            List<ManagedObjectRepresentation> managedObjects = proxiedPlatform.getInventoryApi()
                    .getManagedObjects()
                    .get(5)
                    .getManagedObjects();
            fail("Should throw exception");
        } catch (Exception e) {
            // Then
            assertTrue(e instanceof SDKException);
            assertEquals(((SDKException) e).getHttpStatus(), 407);
        }
    }
}
