package com.cumulocity.agent.server.context;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Throwables;

@Component
public class ServletDeviceContextFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ServletDeviceContextFilter.class);

    private final DeviceContextService contextService;

    private final DeviceBootstrapDeviceCredentialsSupplier deviceBootstrapDeviceCredentialsSupplier;

    private final List<DeviceCredentailsResolver<HttpServletRequest>> deviceCredentailsResolvers;

    @Autowired
    public ServletDeviceContextFilter(DeviceContextService contextService,
            List<DeviceCredentailsResolver<HttpServletRequest>> deviceCredentailsResolvers,
            DeviceBootstrapDeviceCredentialsSupplier deviceBootstrapDeviceCredentialsSupplier) {
        this.contextService = contextService;
        this.deviceCredentailsResolvers = deviceCredentailsResolvers;
        this.deviceBootstrapDeviceCredentialsSupplier = deviceBootstrapDeviceCredentialsSupplier;
    }

    private DeviceCredentials getCredentials(final HttpServletRequest request) {

        for (DeviceCredentailsResolver<HttpServletRequest> resolver : deviceCredentailsResolvers) {
            if (resolver.supports(request)) {
                final DeviceCredentials deviceCredentials = resolver.get(request);
                log.debug("credientials resolved using {} : {}", resolver.getClass(), deviceCredentials);
                return deviceCredentials;
            }
        }
        return deviceBootstrapDeviceCredentialsSupplier.get();

    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        try {
            contextService.callWithinContext(new DeviceContext(getCredentials(request)), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    filterChain.doFilter(request, response);
                    return null;
                }
            });
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }
    }
}
