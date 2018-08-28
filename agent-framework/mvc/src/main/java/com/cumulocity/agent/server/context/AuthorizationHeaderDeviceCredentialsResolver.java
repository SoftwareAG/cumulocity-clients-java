package com.cumulocity.agent.server.context;

import static com.cumulocity.sdk.client.PagedCollectionResource.PAGE_SIZE_KEY;
import static com.cumulocity.sdk.client.RestConnector.X_CUMULOCITY_APPLICATION_KEY;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static java.lang.Integer.parseInt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Optional;

@Component
public class AuthorizationHeaderDeviceCredentialsResolver implements DeviceCredentailsResolver<HttpServletRequest> {

    private static final String TFA_TOKEN = "TFAToken";
    private static final String XSRF_TOKEN_HEADER = "X-XSRF-TOKEN";

    private static final Integer DEFAULT_PAGE_SIZE = 5;

    private static final Function<String, Integer> toInt = new Function<String, Integer>() {
        @Override
        public Integer apply(String s) {
            return parseInt(s);
        }
    };

    @Override
    public DeviceCredentials get(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        String oAuthAccessToken = obtainOAuthAccessToken(request);
        String xsrfToken = request.getHeader(XSRF_TOKEN_HEADER);
        String applicationKey = request.getHeader(X_CUMULOCITY_APPLICATION_KEY);
        String tfaToken = request.getHeader(TFA_TOKEN);

        int pageSize = Optional.fromNullable(request.getParameter(PAGE_SIZE_KEY)).transform(toInt).or(DEFAULT_PAGE_SIZE);
        return DeviceCredentials.from(authorization, oAuthAccessToken, xsrfToken, applicationKey, tfaToken, pageSize);
    }

    private String obtainOAuthAccessToken(HttpServletRequest request) {
        if (request == null || request.getCookies() == null) {
            return null;
        }

        Optional<Cookie> cookieOptional = FluentIterable.of(request.getCookies())
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
    public boolean supports(Object credentialSource) {
        return credentialSource instanceof HttpServletRequest
                && !isNullOrEmpty(((HttpServletRequest) credentialSource).getHeader(AUTHORIZATION));
    }
}
