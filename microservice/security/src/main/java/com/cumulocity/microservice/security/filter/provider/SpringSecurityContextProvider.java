package com.cumulocity.microservice.security.filter.provider;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.security.filter.util.HttpRequestUtils;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SpringSecurityContextProvider implements PostAuthorizationContextProvider<SecurityContext> {

    private final ContextService<UserCredentials> userContext;
    private final MicroserviceSubscriptionsService subscriptionsService;
    private final String applicationName;

    /**
     * true if context.authentication.principal instance of UserDetails
     * false otherwise
     */
    @Override
    public boolean supports(SecurityContext context) {
        if (subscriptionsService == null) {
            log.warn("Context service not available.");
            return false;
        }

        if (context == null || context.getAuthentication() == null) {
            log.warn("Security context not available.");
            return false;
        }

        return context.getAuthentication().getPrincipal() instanceof UserDetails;
    }

    @Override
    public MicroserviceCredentials get(SecurityContext context) {
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
        final UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
        final String[] split = HttpRequestUtils.splitUsername(principal.getUsername());

        if (split.length > 1) {
            return split[0];
        } else if (userContext != null && userContext.isInContext()) {
            return userContext.getContext().getTenant();
        }
        return null;
    }
}
