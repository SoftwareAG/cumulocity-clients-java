package com.cumulocity.microservice.settings.service;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.settings.repository.CurrentApplicationSettingsApi;
import com.cumulocity.microservice.settings.service.impl.MicroserviceSettingsServiceImpl;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.rest.representation.tenant.OptionsRepresentation;
import com.google.common.base.Suppliers;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.Callable;


import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceSettingsServiceTest {

    @Mock
    private PlatformProperties platformProperties;
    @Mock
    private CurrentApplicationSettingsApi currentApplicationSettingsApi;
    @Mock
    private ContextService<MicroserviceCredentials> contextService;

    private MicroserviceSettingsService microserviceSettingsService;

    @Before
    public void setUp() {
        doReturn(bootstrapUser()).when(platformProperties).getMicroserviceBoostrapUser();
        doReturn(Suppliers.ofInstance("http://c8y:80")).when(platformProperties).getUrl();
        doReturn(new OptionsRepresentation()).when(contextService).callWithinContext(any(MicroserviceCredentials.class), any(Callable.class));
        microserviceSettingsService = new MicroserviceSettingsServiceImpl(platformProperties, contextService, currentApplicationSettingsApi);
    }

    @Test
    public void mustRequestTenantOptionsInTenantContext() {
        // given
        doReturn(true).when(contextService).isInContext();
        doReturn(context("t100")).when(contextService).getContext();
        // when
        microserviceSettingsService.getAll();
        // then
        verify(contextService).callWithinContext(argThat(new TenantMatcher("t100")), any(Callable.class));
    }

    @Test
    public void mustFallbackToBootstrapUserWhenNotInContext() {
        // given
        doReturn(false).when(contextService).isInContext();
        // when
        microserviceSettingsService.getAll();
        // then
        verify(contextService).callWithinContext(argThat(new TenantMatcher("management")), any(Callable.class));
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
    private class TenantMatcher extends ArgumentMatcher<MicroserviceCredentials> {
        private final String tenantId;
        @Override
        public boolean matches(Object o) {
            if (!MicroserviceCredentials.class.isAssignableFrom(o.getClass())) {
                return false;
            }
            return ((MicroserviceCredentials)o).getTenant().equals(tenantId);
        }
    }

}
