package com.cumulocity.microservice.security.annotation;

import com.cumulocity.microservice.security.service.SecurityExpressionService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;
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

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class EnableGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Autowired
    private SecurityExpressionService expressionService;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        if (expressionService != null) {
            return new CustomMethodSecurityExpressionHandler(expressionService);
        }
        return super.createExpressionHandler();
    }

    @Data
    public static class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
        private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
        private final SecurityExpressionService expressionService;

        @Override
        protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
            CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication, expressionService);
            root.setTarget(invocation.getThis());
            root.setPermissionEvaluator(getPermissionEvaluator());
            root.setTrustResolver(getTrustResolver());
            root.setRoleHierarchy(getRoleHierarchy());
            return root;
        }
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

        @Delegate
        private final SecurityExpressionService expressionService;
        private Object filterObject;
        private Object returnObject;
        private Object target;

        public CustomMethodSecurityExpressionRoot(Authentication authentication, SecurityExpressionService expressionService) {
            super(authentication);
            this.expressionService = expressionService;
        }

        public Object getThis() {
            return target;
        }
    }
}
