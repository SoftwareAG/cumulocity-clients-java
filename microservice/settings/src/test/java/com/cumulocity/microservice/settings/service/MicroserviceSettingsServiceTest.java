package com.cumulocity.microservice.settings.service;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.ContextServiceImpl;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.settings.service.impl.MicroserviceSettingsServiceImpl;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.CredentialsSwitchingPlatform;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.rest.representation.tenant.OptionsRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.google.common.base.Suppliers;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceSettingsServiceTest {

    @Mock
    private PlatformProperties platformProperties;
    @Mock
    private CredentialsSwitchingPlatform credentialsSwitchingPlatform;
    @Mock
    private RestOperations restOperations;
    private ContextService<MicroserviceCredentials> contextService = new ContextServiceImpl<>(MicroserviceCredentials.class);

    private MicroserviceSettingsService microserviceSettingsService;

    @Before
    public void setUp() {
        doReturn(bootstrapUser()).when(platformProperties).getMicroserviceBoostrapUser();
        doReturn(Suppliers.ofInstance("http://c8y:80")).when(platformProperties).getUrl();
        doReturn(restOperations).when(credentialsSwitchingPlatform).get();
        doReturn(new OptionsRepresentation()).when(restOperations).get(anyString(), eq(APPLICATION_JSON_TYPE), eq(OptionsRepresentation.class));
        microserviceSettingsService = new MicroserviceSettingsServiceImpl(platformProperties, contextService, credentialsSwitchingPlatform);
    }

    @Test
    public void mustRequestTenantOptionsInTenantContext() {
        // given
        // when
        contextService.runWithinContext(context("t100"), new Runnable() {
            public void run() {
                microserviceSettingsService.getAll();
            }
        });
        // then
        verify(credentialsSwitchingPlatform).switchTo(argThat(new TenantMatcher("t100")));
    }

    @Test
    public void mustFallbackToBootstrapUserWhenNotInContext() {
        // given
        // when
        microserviceSettingsService.getAll();
        // then
        verify(credentialsSwitchingPlatform).switchTo(argThat(new TenantMatcher("management")));
    }

    private MicroserviceCredentials context(String tenantId) {
        return MicroserviceCredentials.builder().tenant(tenantId).username("service_app").build();
    }

    private MicroserviceCredentials bootstrapUser() {
        return MicroserviceCredentials.builder()
                .tenant("management")
                .username("servicebootstrap_app")
                .password("paa33word_1")
                .build();
    }

    @RequiredArgsConstructor
    private class TenantMatcher extends ArgumentMatcher<CumulocityCredentials> {
        private final String tenantId;
        @Override
        public boolean matches(Object o) {
            if (!CumulocityCredentials.class.isAssignableFrom(o.getClass())) {
                return false;
            }
            return ((CumulocityCredentials)o).getTenantId().equals(tenantId);
        }
    }

}
