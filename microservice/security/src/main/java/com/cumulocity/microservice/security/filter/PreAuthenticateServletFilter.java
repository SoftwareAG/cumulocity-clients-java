package com.cumulocity.microservice.security.filter;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreAuthenticateServletFilter extends OncePerRequestFilter {

    @Autowired(required = false)
    private List<PreAuthorizationContextProvider<HttpServletRequest>> credentialsResolvers;

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
                    .filter(new Predicate<PreAuthorizationContextProvider<HttpServletRequest>>() {
                        public boolean apply(PreAuthorizationContextProvider<HttpServletRequest> provider) {
                            return provider.supports(request);
                        }
                    })
                    .transform(new Function<PreAuthorizationContextProvider<HttpServletRequest>, Credentials>() {
                        public Credentials apply(PreAuthorizationContextProvider<HttpServletRequest> provider) {
                            return provider.get(request);
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
