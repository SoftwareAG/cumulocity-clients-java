package com.cumulocity.microservice.security.filter;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;

@Component
public class PostAuthenticateServletFilter extends OncePerRequestFilter {
    @Autowired(required = false)
    private List<PostAuthorizationContextProvider<SecurityContext>> credentialsResolvers;

    @Autowired(required = false)
    private ContextService<Credentials> contextService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) {
        Runnable runnable = new Runnable() {
            @SneakyThrows
            public void run() {
                filterChain.doFilter(request, response);
            }
        };
        if (contextService != null && credentialsResolvers != null) {
            final ImmutableList<Credentials> credentials = from(credentialsResolvers)
                    .filter(new Predicate<PostAuthorizationContextProvider<SecurityContext>>() {
                        public boolean apply(PostAuthorizationContextProvider<SecurityContext> provider) {
                            return provider.supports(SecurityContextHolder.getContext());
                        }
                    })
                    .transform(new Function<PostAuthorizationContextProvider<SecurityContext>, Credentials>() {
                        public Credentials apply(PostAuthorizationContextProvider<SecurityContext> provider) {
                            return provider.get(SecurityContextHolder.getContext());
                        }
                    })
                    .filter(new Predicate<Credentials>() {
                        public boolean apply(Credentials credentials) {
                            return credentials != null;
                        }
                    })
                    .toList();

            for (final Credentials credential : credentials) {
                runnable = contextService.withinContext(credential, runnable);
            }
        }

        runnable.run();
    }
}
