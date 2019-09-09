package com.cumulocity.microservice.security.token;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

    protected MessageSourceAccessor messages;
    private StandardEnvironment environment;
    private JwtAuthenticatedTokenCache tokenCache;

    public JwtTokenAuthenticationProvider(StandardEnvironment environment, JwtAuthenticatedTokenCache tokenCache) {
        this.environment = environment;
        this.tokenCache = tokenCache;
        this.messages = SpringSecurityMessageSource.getAccessor();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtTokenAuthentication.class.isAssignableFrom(authentication));
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        final JwtTokenAuthentication jwtTokenAuthentication = (JwtTokenAuthentication) authentication;
        JwtCredentials jwtCredentials = jwtTokenAuthentication.getCredentials();
        try {
            return tokenCache.get(jwtCredentials, new Callable<JwtTokenAuthentication>() {
                @Override
                public JwtTokenAuthentication call() {
                    String baseUrl = "" + environment.getSystemEnvironment().get("C8Y_BASEURL");
                    return new CumulocityOAuthUserDetails(baseUrl, jwtTokenAuthentication).updateTokenWithTenantAndUserDetailsUsingRequestsToCore();
                }
            });
        } catch (ExecutionException e) {
            throw new TokenCacheException("Problem with token cache.", e);
        }
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}






