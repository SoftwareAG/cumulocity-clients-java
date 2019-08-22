package com.cumulocity.microservice.security.token;

import com.cumulocity.agent.server.context.DeviceCredentials;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtTokenAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Value("${C8Y.baseURL}")
    private String baseUrl;

    private final Cache<JwtTokenAuthentication, Authentication> userCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtTokenAuthentication.class.isAssignableFrom(authentication));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final JwtTokenAuthentication jwtTokenAuthentication = (JwtTokenAuthentication) authentication;
        try {
            return userCache.get(jwtTokenAuthentication, new Callable<Authentication>() {
                @Override
                public Authentication call() throws Exception {
                    CumulocityOAuthUserDetails cumulocityOAuthUserDetails = CumulocityOAuthUserDetails.from(baseUrl, jwtTokenAuthentication);
                    jwtTokenAuthentication.setCurrentUserRepresentation(cumulocityOAuthUserDetails.getCurrentUser());
                    String tenantName = cumulocityOAuthUserDetails.getTenantName();
                    jwtTokenAuthentication.setDeviceCredentials(buildDeviceCredentials(tenantName, jwtTokenAuthentication));
                    return jwtTokenAuthentication;
                }
            });
        } catch (ExecutionException e) {
            log.info("Error while authenticating", e);
            return null;
        }
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

}

