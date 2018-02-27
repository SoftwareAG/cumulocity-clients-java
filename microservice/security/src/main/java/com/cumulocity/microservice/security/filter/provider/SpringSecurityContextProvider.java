package com.cumulocity.microservice.security.filter.provider;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.security.filter.util.HttpRequestUtils;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class SpringSecurityContextProvider implements PostAuthorizationContextProvider<SecurityContext> {

    @Autowired(required = false)
    private MicroserviceSubscriptionsService subscriptionsService;

    @Autowired(required = false)
    private ContextService<UserCredentials> user;

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
        } else if (user != null) {
            return user.getContext().getTenant();
        }
        return null;
    }
}
