package com.cumulocity.microservice.security.token;

import com.cumulocity.agent.server.context.DeviceContext;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.google.common.base.Optional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
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
@Component
public class CumulocityOAuthMicroserviceFilter extends GenericFilterBean {

    @Setter(onMethod = @_(@Autowired))
    private AuthenticationManager authenticationManager;
    @Setter(onMethod = @_(@Autowired(required = false)))
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Setter(onMethod = @_(@Autowired))
    private DeviceContextService deviceContextService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        if (shouldAuthenticate()) {
            Optional<JwtCredentials> jwtCredentials = readCredentials(request);

            if (jwtCredentials.isPresent()) {
                final boolean debug = logger.isDebugEnabled();
                try {
                    JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(jwtCredentials.get());
                    Authentication authResult = authenticationManager.authenticate(jwtTokenAuthentication);
                    if (debug) {
                        logger.debug("Authentication success: " + authResult);
                    }
                    // it is important to enter context at this point, so in later processing correct credentials are in place
                    authResult.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                    deviceContextService.enterContext(new DeviceContext(((JwtTokenAuthentication) authResult).getDeviceCredentials()));

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

    private Optional<JwtCredentials> readCredentials(HttpServletRequest req) {
        Enumeration<String> headers = req.getHeaders("Authorization");
        if (headers != null) {
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                if (header.toLowerCase().startsWith("bearer")) {
                    return Optional.of((JwtCredentials) new JwtOnlyCredentials(JwtHelper.decode(header.substring(7))));
                }
            }
        }
        Optional<Cookie> accessToken = CookieReader.readAuthorizationCookie(req);
        if (accessToken.isPresent()) {
            String xsrfToken = req.getHeader("X-XSRF-TOKEN");
            if (StringUtils.isNotBlank(xsrfToken)) {
                return Optional.of((JwtCredentials) new JwtAndXsrfTokenCredentials(
                        JwtHelper.decode(accessToken.get().getValue()),
                        xsrfToken));
            }
        }
        return Optional.absent();
    }
}

