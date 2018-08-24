package com.cumulocity.microservice.security.filter.provider;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.security.filter.util.HttpRequestUtils;
import com.cumulocity.microservice.security.filter.util.HttpRequestUtils.AuthorizationHeader;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static com.cumulocity.microservice.security.filter.util.HttpRequestUtils.TFA_TOKEN_HEADER;
import static com.cumulocity.microservice.security.filter.util.HttpRequestUtils.X_CUMULOCITY_APPLICATION_KEY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class HttpContextProvider implements PreAuthorizationContextProvider<HttpServletRequest> {
    @Override
    public UserCredentials get(HttpServletRequest request) {
        final String authorization = request.getHeader(AUTHORIZATION);
        final String applicationKey = request.getHeader(X_CUMULOCITY_APPLICATION_KEY);
        final String tfaToken = request.getHeader(TFA_TOKEN_HEADER);
        final AuthorizationHeader from = HttpRequestUtils.authorizationHeader(authorization);

        if (StringUtils.hasText(from.getTenant())) {
            return UserCredentials.builder()
                    .tenant(from.getTenant())
                    .username(from.getUsername())
                    .password(from.getPassword())
                    .oAuthAccessToken(obtainOAuthAccessToken(request))
                    .appKey(applicationKey)
                    .tfaToken(tfaToken)
                    .build();
        }
        return null;
    }

    private String obtainOAuthAccessToken(HttpServletRequest request) {
        if (request == null || request.getCookies() == null) {
            return null;
        }
        Optional<Cookie> cookieOptional = FluentIterable.from(request.getCookies())
                .firstMatch(new Predicate<Cookie>() {
                    @Override
                    public boolean apply(Cookie cookie) {
                        return AUTHORIZATION.equalsIgnoreCase(cookie.getName());
                    }
                });

        if (cookieOptional.isPresent()) {
            return cookieOptional.get().getValue();
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(HttpServletRequest credentialSource) {
        return HttpRequestUtils.hasAuthorizationHeader(credentialSource);
    }
}
