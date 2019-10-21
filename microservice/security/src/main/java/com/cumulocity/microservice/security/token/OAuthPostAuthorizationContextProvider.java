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
class OAuthPostAuthorizationContextProvider implements PostAuthorizationContextProvider<SecurityContext> {

    private final String applicationName;

    //Optional dependency
    private MicroserviceSubscriptionsService subscriptionsService;


    public OAuthPostAuthorizationContextProvider(@Value("${application.name:}") String applicationName) {
        this.applicationName = applicationName;
    }

    @Autowired(required = false)
    public void setSubscriptionsService(MicroserviceSubscriptionsService subscriptionsService) {
        this.subscriptionsService = subscriptionsService;
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
        final Optional<String> tenant = getTenantName(context);
        if (tenant.isPresent() && (subscriptionsService != null)) {
            final Optional<MicroserviceCredentials> microservice = subscriptionsService.getCredentials(tenant.get());
            if (microservice.isPresent()) {
                return microservice.get();
            } else {
                throw new AccessDeniedException("Microservice " + applicationName + " is not subscribed by tenant " + tenant.get());
            }
        }
        return null;
    }

    private Optional<String> getTenantName(SecurityContext context) {
        JwtTokenAuthentication jwtTokenAuthentication = (JwtTokenAuthentication) context.getAuthentication();
        String tenantName = jwtTokenAuthentication.getTenantName();
        return StringUtils.hasText(tenantName) ? Optional.of(tenantName) : Optional.<String>absent();
    }
}
