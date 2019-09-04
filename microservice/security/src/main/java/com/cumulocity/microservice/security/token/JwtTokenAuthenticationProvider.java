package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {

    protected MessageSourceAccessor messages;
    private StandardEnvironment environment;
    private JwtAuthenticatedTokenCache tokenCache;

    public JwtTokenAuthenticationProvider(StandardEnvironment environment) {
        this.environment = environment;
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
        Optional<Authentication> tokenFromCache = tokenCache.get(jwtCredentials);
        if (tokenFromCache.isPresent()) {
            return tokenFromCache.get();
        } else {
            JwtTokenAuthentication updatedToken = updateTokenWithTenantAndUserDetails(jwtTokenAuthentication);
            tokenCache.put(jwtCredentials, updatedToken);
            return updatedToken;
        }
    }

    private JwtTokenAuthentication updateTokenWithTenantAndUserDetails(JwtTokenAuthentication jwtTokenAuthentication) {
        String baseUrl = "" + environment.getSystemEnvironment().get("C8Y_BASEURL");
        CumulocityOAuthUserDetails cumulocityOAuthUserDetails = new CumulocityOAuthUserDetails(baseUrl, jwtTokenAuthentication);
        CurrentUserRepresentation currUserRepresentation = cumulocityOAuthUserDetails.getCurrentUser();
        String tenantName = cumulocityOAuthUserDetails.getTenantName();

        jwtTokenAuthentication.setCurrentUserRepresentation(currUserRepresentation);
        updateUserCredentials(tenantName, jwtTokenAuthentication);
        return jwtTokenAuthentication;
    }

    private void updateUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
        UserCredentials userCredentials = buildUserCredentials(tenantName, jwtTokenAuthentication);
        jwtTokenAuthentication.setUserCredentials(userCredentials);
    }

    private UserCredentials buildUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication) {
        JwtCredentials jwtCredentials = jwtTokenAuthentication.getCredentials();
        return jwtCredentials.buildUserCredentials(tenantName, jwtTokenAuthentication);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setTokenCache(JwtAuthenticatedTokenCache cache) {
        this.tokenCache = cache;
    }
}






