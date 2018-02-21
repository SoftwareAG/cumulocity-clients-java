package com.cumulocity.microservice.security.annotation;

import com.cumulocity.microservice.subscription.repository.application.ApplicationApi;
import lombok.Data;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class EnableGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Autowired(required = false)
    private ApplicationApi applicationApi;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        if (applicationApi != null) {
            return new CustomMethodSecurityExpressionHandler(applicationApi);
        }
        return super.createExpressionHandler();
    }

    @Data
    public static class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
        private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
        private final ApplicationApi applicationApi;

        @Override
        protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
            CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication, applicationApi);
            root.setTarget(invocation.getThis());
            root.setPermissionEvaluator(getPermissionEvaluator());
            root.setTrustResolver(this.trustResolver);
            root.setRoleHierarchy(getRoleHierarchy());
            return root;
        }
    }

    @Data
    public static class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

        private final ApplicationApi applicationApi;
        private Object filterObject;
        private Object returnObject;
        private Object target;

        public CustomMethodSecurityExpressionRoot(Authentication authentication, ApplicationApi applicationApi) {
            super(authentication);
            this.applicationApi = applicationApi;
        }

        public boolean isFeatureEnabled(String featureName) {
            if (!featureName.startsWith("feature-")) {
//                todo review: is the assumption that every feature name will start with "feature-" is correct?
                return applicationApi.getByName("feature-" + featureName).isPresent();
            }

            return applicationApi.getByName(featureName).isPresent();
        }

        public boolean isServiceUser(String service) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                final String username = ((UserDetails) principal).getUsername();

                return Objects.equals(username, "service_" + service);
            }
            return false;
        }

        public Object getThis() {
            return target;
        }
    }
}
