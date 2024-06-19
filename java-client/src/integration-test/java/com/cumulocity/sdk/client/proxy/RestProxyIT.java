package com.cumulocity.sdk.client.proxy;

import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestProxyIT extends BaseProxyIT {

    @BeforeEach
    public void setup() {
        ManagedObjectRepresentation rep = aSampleMo().build();
        platform.getInventoryApi().create(rep);
    }

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
        assertThat(managedObjects).isNotNull();
        assertThat(managedObjects).isNotEmpty();
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
        assertThat(managedObjects).isNotNull();
        assertThat(managedObjects).isNotEmpty();
    }

    @Test
    public void shouldFailToSendRequestWhenInvalidAuthentication() {
        // Given
        givenAuthenticatedProxy(PROXY_AUTH_USERNAME, PROXY_AUTH_PASSWORD);
        givenAuthenticatedProxiedPlatform("invalid-user", "invalid-password");

        // When
        assertThatThrownBy(() -> proxiedPlatform.getInventoryApi()
                .getManagedObjects()
                .get(5)
                .getManagedObjects())
                // Then
                .isInstanceOf(SDKException.class)
                .extracting(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) {
                        return ((SDKException) throwable).getHttpStatus();
                    }
                })
                .isEqualTo(407);
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }
}
