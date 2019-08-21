package com.cumulocity.microservice.security.token;

import com.cumulocity.agent.server.context.DeviceCredentials;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.sdk.client.CumulocityAuthenticationFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.jersey.client.apache.ApacheHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenAuthenticationProvider implements AuthenticationProvider, InitializingBean,
        MessageSourceAware {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Value("${C8Y.baseURL}")
    String baseUrl;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtTokenAuthentication.class.isAssignableFrom(authentication));
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final JwtTokenAuthentication jwtTokenAuthentication = (JwtTokenAuthentication) authentication;
        LocalClient client = LocalClient.from(baseUrl, jwtTokenAuthentication);
        jwtTokenAuthentication.setCurrentUserRepresentation(client.getCurrentUser());
        String tenantName = client.getTenantName();
        jwtTokenAuthentication.setDeviceCredentials(buildDeviceCredentials(tenantName, jwtTokenAuthentication));
        return jwtTokenAuthentication;
    }

    private DeviceCredentials buildDeviceCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
        if (jwtTokenAuthentication.getCredentials() instanceof JwtOnlyCredentials) {
            return DeviceCredentials.from(tenantName,
                    jwtTokenAuthentication.getCurrentUserRepresentation().getUserName(),
                    ((JwtCredentials) jwtTokenAuthentication.getCredentials()).getJwt().getEncoded(),
                    null
            );
        } else if (jwtTokenAuthentication.getCredentials() instanceof JwtAndXsrfTokenCredentials) {
            JwtAndXsrfTokenCredentials credentials = ((JwtAndXsrfTokenCredentials) jwtTokenAuthentication.getCredentials());
            return DeviceCredentials.from(tenantName,
                    jwtTokenAuthentication.getCurrentUserRepresentation().getUserName(),
                    credentials.getJwt().getEncoded(),
                    credentials.getXsrfToken()
            );
        }
        throw new IllegalStateException("Unknown jwtTokenAuthentication credentials type");
    }


    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    private static class LocalClient {

        ApacheHttpClient client;
        String baseUrl;

        static final LocalClient from(String baseUrl, JwtTokenAuthentication jwtTokenAuthentication) {
            ApacheHttpClient client = new ApacheHttpClient();
            if (jwtTokenAuthentication != null) {
                if (jwtTokenAuthentication.getCredentials() instanceof JwtAndXsrfTokenCredentials) {
                    JwtAndXsrfTokenCredentials credentials = (JwtAndXsrfTokenCredentials) jwtTokenAuthentication.getCredentials();
                    client.addFilter(new CumulocityAuthenticationFilter(
                            credentials.getJwt().getEncoded()
                            , credentials.getXsrfToken()
                    ));
                } else {
                    client.addFilter(new CumulocityAuthenticationFilter(
                            ((JwtCredentials) jwtTokenAuthentication.getCredentials()).getJwt().getEncoded(),
                            null
                    ));
                }
            }
            return new LocalClient(baseUrl, client);
        }

        public LocalClient(String baseUrl, ApacheHttpClient client) {
            this.client = client;
            this.baseUrl = baseUrl;
        }

        CurrentUserRepresentation getCurrentUser() {
            return client.resource(baseUrl + "/user/currentUser")
                    .accept(UserMediaType.CURRENT_USER)
                    .get(CurrentUserRepresentation.class);
        }

        String getTenantName() {
            SimplifiedCurrentTenantRepresentation currentTenantRepresentation = client.resource(baseUrl + "/tenant/currentTenant")
                    .accept(UserMediaType.CURRENT_TENANT)
                    .get(SimplifiedCurrentTenantRepresentation.class);
            return currentTenantRepresentation.name;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        protected static class SimplifiedCurrentTenantRepresentation {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}

