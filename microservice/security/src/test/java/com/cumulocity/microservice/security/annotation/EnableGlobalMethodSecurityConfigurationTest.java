package com.cumulocity.microservice.security.annotation;

import com.cumulocity.microservice.subscription.repository.application.ApplicationApi;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        EnableGlobalMethodSecurityConfiguration.class,
        EnableGlobalMethodSecurityConfigurationTest.TestConfiguration.class
})
public class EnableGlobalMethodSecurityConfigurationTest {

    interface TestService {
        boolean doSomethingWhatRequiresFeatureEnabled();

        boolean doSomethingWhatRequiresServiceUser();
    }

    @Configuration
    public static class TestConfiguration {
        @Bean
        public TestService testService() {
            return new TestService() {
                @PreAuthorize("isFeatureEnabled('feature-cep-custom-rule')")
                public boolean doSomethingWhatRequiresFeatureEnabled() {
                    return true;
                }

                @PreAuthorize("isServiceUser('smartrule')")
                public boolean doSomethingWhatRequiresServiceUser() {
                    return true;
                }
            };
        }
    }

    @Autowired
    private TestService testService;

    @MockBean
    private ApplicationApi applications;

    @Test
    @WithMockUser
    public void shouldPassValidationOfFeature() {
        when(applications.getByName(anyString())).thenReturn(Optional.of(new ApplicationRepresentation()));

        final boolean result = testService.doSomethingWhatRequiresFeatureEnabled();

        assertThat(result).isTrue();
    }

    @WithMockUser
    @Test(expected = AccessDeniedException.class)
    public void shouldFailValidationOfFeature() {
        when(applications.getByName(anyString())).thenReturn(Optional.<ApplicationRepresentation>absent());

        testService.doSomethingWhatRequiresFeatureEnabled();
    }

    @Test
    @WithMockUser(username = "service_smartrule")
    public void shouldPassValidationOfServiceUser() {
        final boolean result = testService.doSomethingWhatRequiresServiceUser();

        assertThat(result).isTrue();
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "service_smartrulessssss")
    public void shouldFailValidationOfServiceUser() {
        testService.doSomethingWhatRequiresServiceUser();
    }
}
