package com.cumulocity.microservice.security.token;

import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class JwtTokenAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

    protected MessageSourceAccessor messages;
    private final Environment environment;
    private final JwtAuthenticatedTokenCache tokenCache;


    public JwtTokenAuthenticationProvider(Environment environment, JwtAuthenticatedTokenCache tokenCache) {
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
            return tokenCache.get(jwtCredentials, new JwtTokenAuthenticationLoader() {
                @Override
                public JwtTokenAuthentication call() {
                    String baseUrl = environment.getProperty("C8Y.baseURL");
                    return CumulocityCoreAuthenticationClient.authenticateUserAndUpdateToken(baseUrl, jwtTokenAuthentication, getRequest());
                }
            });
        } catch (ExecutionException e) {
            throw new TokenCacheException("Problem with token cache.", e);
        }
    }

    private HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) attributes).getRequest();
        } else {
            return null;
        }
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}






