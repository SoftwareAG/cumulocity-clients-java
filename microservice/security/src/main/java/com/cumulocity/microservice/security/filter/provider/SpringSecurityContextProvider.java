package com.cumulocity.microservice.security.filter.provider;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.security.filter.util.HttpRequestUtils;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SpringSecurityContextProvider implements PostAuthorizationContextProvider<SecurityContext> {

    @Autowired(required = false)
    private MicroserviceSubscriptionsService subscriptionsService;

    @Autowired(required = false)
    private ContextService<UserCredentials> user;

    @Override
    public boolean supports(SecurityContext context) {
        return subscriptionsService != null
                && context != null
                && context.getAuthentication() != null
                && context.getAuthentication().getPrincipal() instanceof User;
    }

    @Override
    public MicroserviceCredentials get(SecurityContext context) {
        String tenant = getTenantName(context);

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
        final User principal = (User) context.getAuthentication().getPrincipal();
        final String[] split = HttpRequestUtils.splitUsername(principal.getUsername());

        if (split.length > 1) {
            return split[0];
        } else if (user != null) {
            return user.getContext().getTenant();
        }
        return null;
    }
}
