package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.ContextService;

import java.text.ParseException;
import java.util.Optional;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Purpose of this class is to take care of authentication against platform of
 * 1. Authorization Bearer header
 * 2. Access token in cookies
 */
@Slf4j
public class CumulocityOAuthMicroserviceFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final ContextService<UserCredentials> userContextService;

    public CumulocityOAuthMicroserviceFilter(AuthenticationManager authenticationManager,
                                             AuthenticationEntryPoint authenticationEntryPoint,
                                             ContextService<UserCredentials> userContextService) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.userContextService = userContextService;
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        if (shouldAuthenticate()) {
            Optional<JwtCredentials> jwtCredentials;
            try {
                jwtCredentials = readCredentials(request);
            } catch (ParseException e) {
                log.error("Failed to decode access token", e);
                throw new AuthenticationServiceException("Authentication failed: token is wrong format", e);
            }

            if (jwtCredentials.isPresent()) {
                final boolean debug = logger.isDebugEnabled();
                try {
                    JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(jwtCredentials.get());
                    // we build the core client with additional information from the request
                    CumulocityCoreAuthenticationClient.setRequest(request);
                    Authentication authResult = authenticationManager.authenticate(jwtTokenAuthentication);
                    if (debug) {
                        logger.debug("Authentication success: " + authResult);
                    }
                    // it is important to enter context at this point, so in later processing correct credentials are in place
                    authResult.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                    userContextService.runWithinContext(
                            ((JwtTokenAuthentication)authResult).getUserCredentials(),
                            () -> {
                                try {
                                    chain.doFilter(req, res);
                                } catch (Exception e) {
                                    throw new AuthenticationServiceException("Error on login attempt", e);
                                }
                            });
                    return;
                } catch (AuthenticationException failed) {
                    log.warn("Error {}", failed);
                    logger.warn(failed);
                    SecurityContextHolder.clearContext();
                    if (debug) {
                        logger.debug("Authentication request for failed: " + failed);
                    }
                    authenticationEntryPoint.commence(request, response, failed);
                    return;
                }
            }
        }
        chain.doFilter(req, res);
    }

    private boolean shouldAuthenticate() {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        }
        return false;
    }

    private Optional<JwtCredentials> readCredentials(HttpServletRequest req) throws ParseException {
        Enumeration<String> headers = req.getHeaders("Authorization");
        if (headers != null) {
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                if (header.toLowerCase().startsWith("bearer")) {
                    return Optional.of(new JwtOnlyCredentials(JWTParser.parse(header.substring(7))));
                }
            }
        }
        Optional<Cookie> accessToken = CookieReader.readAuthorizationCookie(req);
        if (accessToken.isPresent()) {
            String xsrfToken = req.getHeader("X-XSRF-TOKEN");
            if (!StringUtils.isEmpty(xsrfToken)) {
                return Optional.of(new JwtAndXsrfTokenCredentials(
                        JWTParser.parse(accessToken.get().getValue()),
                        xsrfToken));
            }
        }
        return Optional.empty();
    }
}
