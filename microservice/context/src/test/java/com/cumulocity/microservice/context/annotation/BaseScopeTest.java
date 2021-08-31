package com.cumulocity.microservice.context.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.context.scope.BaseScope;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static com.cumulocity.microservice.context.scope.BaseScope.DEFAULT_CACHE_EXPIRATION_TIMEOUT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EnableContextSupportConfiguration.class )
public class BaseScopeTest {

    @Autowired
    private ContextService<MicroserviceCredentials> microserviceContextService;

    @Autowired
    private ContextService<UserCredentials> userContextService;

    @ParameterizedTest
    @CsvSource({"-1," + Long.MAX_VALUE, "0," + Long.MAX_VALUE, "10000,10000"})
    public void shouldSetCorrectTimeoutBasingOnPropertyValue(Long timeout, String expectedTimeout) {
        EnableContextSupportConfiguration contextSupportConfiguration = new EnableContextSupportConfiguration();
        CustomScopeConfigurer customScopeConfigurer =
                contextSupportConfiguration.contextScopeConfigurer(microserviceContextService, userContextService, timeout);

        Map<String, Object> scopes = (Map<String, Object>) ReflectionTestUtils.getField(customScopeConfigurer, "scopes");

        assertThat(scopes.size()).isEqualTo(2);
        assertThat(((BaseScope) scopes.get("user")).getCacheExpirationTimeout()).isEqualTo(Long.valueOf(DEFAULT_CACHE_EXPIRATION_TIMEOUT));
        assertThat(((BaseScope) scopes.get("tenant")).getCacheExpirationTimeout()).isEqualTo(Long.valueOf(expectedTimeout));
    }
}
