package com.cumulocity.microservice.security.filter;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.security.filter.provider.PostAuthorizationContextProvider;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;

@Slf4j
@Component
public class PostAuthenticateServletFilter extends OncePerRequestFilter {
    @Autowired(required = false)
    private List<PostAuthorizationContextProvider<SecurityContext>> credentialsResolvers;

    @Autowired(required = false)
    private ContextService<Credentials> contextService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    filterChain.doFilter(request, response);
                } catch (final Exception ex) {
                    Throwables.propagate(ex);
                }
            }
        };
        if (contextService == null) {
            log.warn("Context service not available.");
        }

        if (contextService != null && credentialsResolvers != null) {
            try {
                final ImmutableList<Credentials> credentials = from(credentialsResolvers).filter(new Predicate<PostAuthorizationContextProvider<SecurityContext>>() {
                    public boolean apply(PostAuthorizationContextProvider<SecurityContext> provider) {
                        return provider.supports(SecurityContextHolder.getContext());
                    }
                }).transform(new Function<PostAuthorizationContextProvider<SecurityContext>, Credentials>() {
                    public Credentials apply(PostAuthorizationContextProvider<SecurityContext> provider) {
                        return provider.get(SecurityContextHolder.getContext());
                    }
                }).filter(new Predicate<Credentials>() {
                    public boolean apply(Credentials credentials) {
                        return credentials != null;
                    }
                }).toList();

                for (final Credentials credential : credentials) {
                    runnable = contextService.withinContext(credential, runnable);
                }
            } catch (AccessDeniedException e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
                return;
            }
        }

        runnable.run();
    }
}
