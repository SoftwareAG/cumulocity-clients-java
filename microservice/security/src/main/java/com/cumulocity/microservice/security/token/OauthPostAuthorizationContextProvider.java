package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.security.filter.provider.PostAuthorizationContextProvider;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
class OauthPostAuthorizationContextProvider implements PostAuthorizationContextProvider<SecurityContext> {

    private final MicroserviceSubscriptionsService subscriptionsService;
    private final String applicationName;

    @Autowired(required = false)
    public OauthPostAuthorizationContextProvider(MicroserviceSubscriptionsService subscriptionsService,
                                                 @Value("${application.name:}") String applicationName) {
        this.subscriptionsService = subscriptionsService;
        this.applicationName = applicationName;
    }

    @Override
    public boolean supports(SecurityContext securityContext) {
        if (subscriptionsService == null) {
            log.warn("Subscription service not available.");
            return false;
        }

        if (securityContext == null || securityContext.getAuthentication() == null) {
            log.warn("Security context not available.");
            return false;
        }
        return securityContext.getAuthentication() instanceof JwtTokenAuthentication;
    }

    @Override
    public Credentials get(SecurityContext context) {
        final String tenant = getTenantName(context);
        if (StringUtils.hasText(tenant)) {
            if (subscriptionsService != null) {
                final Optional<MicroserviceCredentials> microservice = subscriptionsService.getCredentials(tenant);
                if (microservice.isPresent()) {
                    return microservice.get();
                } else {
                    throw new AccessDeniedException("Microservice " + applicationName + " is not subscribed by tenant " + tenant);
                }
            }
        }
        return null;
    }

    private String getTenantName(SecurityContext context) {
        JwtTokenAuthentication jwtTokenAuthentication = (JwtTokenAuthentication) context.getAuthentication();
        return jwtTokenAuthentication.getTenantName();
    }
}
