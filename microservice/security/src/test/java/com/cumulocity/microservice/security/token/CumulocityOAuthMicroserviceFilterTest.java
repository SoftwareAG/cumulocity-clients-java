package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.cumulocity.microservice.security.token.CookieReader.AUTHORIZATION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SecurityContextHolder.class})
public class CumulocityOAuthMicroserviceFilterTest {

    private final static String SAMPLE_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOm51bGwsImlzcyI6ImN1bXVsb2NpdHkuZGVmYXVsdC5zdmMuY2x1c3Rlci5sb2NhbCIsImF1ZCI6ImN1bXVsb2NpdHkuZGVmYXVsdC5zdmMuY2x1c3Rlci5sb2NhbCIsInN1YiI6ImFkbWluIiwidGNpIjoiZDMwMTczNjYtY2Y3Yi00MjdlLWE2OTMtNzJiYjg2MGE5MDgzIiwiaWF0IjoxNTY1NzYxMTg0LCJuYmYiOjE1NjU3NjExODQsImV4cCI6MTU2Njk3MDc4NCwidGZhIjpmYWxzZSwidGVuIjoibWFuYWdlbWVudCIsInhzcmZUb2tlbiI6InZ2VXlpS3h6c1VHQlhNbGNPb2RrIn0.TDz9k0NfKeLK5f0dwZ_gqOWyweMLpaIdEtU6snos9_0ephtI4HibCVEOV9JPoHZnaqjAUyfmhQc7WN2JLpMX6Q";
    private final static String SAMPLE_X_XSRF_TOKEN = "vvUyiKxzsUGBXMlcOodk";

    CumulocityOAuthMicroserviceFilter filter;
    AuthenticationEntryPoint authenticationEntryPoint;
    ContextService contextService;

    AuthenticationManager authenticationManager;

    HttpServletRequest request;
    HttpServletResponse response;
    FilterChain chain;


    @Before
    public void setup() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        authenticationEntryPoint = mock(AuthenticationEntryPoint.class);

        authenticationManager = mock(AuthenticationManager.class);
        contextService = mock(ContextService.class);
        filter = new CumulocityOAuthMicroserviceFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationEntryPoint(authenticationEntryPoint);
        filter.setUserContextService(contextService);
    }

    @Test
    public void shouldAuthenticateWithAuthorizationBearer() throws IOException, ServletException {
        SecurityContext context = mockSecurityContext();
        when(request.getHeaders("Authorization")).thenReturn(Collections.enumeration(ImmutableSet.of("Basic XXX", "Bearer " + SAMPLE_TOKEN)));
        mockAuthenticationMangerReturnArgument();
        mockContextServiceInvokeRunnable();

        filter.doFilter(request, response, chain);

        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(captor.capture());
        Authentication actualAuthentication = captor.getValue();
        assertThat(actualAuthentication).isExactlyInstanceOf(JwtTokenAuthentication.class);
        JwtCredentials credentials = (JwtCredentials) actualAuthentication.getCredentials();
        assertThat(credentials.getJwt().getEncoded()).isEqualTo(SAMPLE_TOKEN);

        verify(chain).doFilter(request, response);
        verify(context).setAuthentication(actualAuthentication);
        verify(contextService).runWithinContext(any(UserCredentials.class), any(Runnable.class));
    }

    @Test
    public void shouldAuthenticateWithAuthorizationCookie() throws IOException, ServletException {
        SecurityContext context = mockSecurityContext();
        Cookie[] cookies = {new Cookie(AUTHORIZATION_KEY, SAMPLE_TOKEN)};
        when(request.getCookies()).thenReturn(cookies);
        when(request.getHeader("X-XSRF-TOKEN")).thenReturn(SAMPLE_X_XSRF_TOKEN);
        mockAuthenticationMangerReturnArgument();
        mockContextServiceInvokeRunnable();

        filter.doFilter(request, response, chain);

        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(captor.capture());
        Authentication actualAuthentication = captor.getValue();
        assertThat(actualAuthentication).isExactlyInstanceOf(JwtTokenAuthentication.class);
        JwtAndXsrfTokenCredentials credentials = (JwtAndXsrfTokenCredentials) actualAuthentication.getCredentials();
        assertThat(credentials.getJwt().getEncoded()).isEqualTo(SAMPLE_TOKEN);
        assertThat(credentials.getXsrfToken()).isEqualTo(SAMPLE_X_XSRF_TOKEN);

        verify(chain).doFilter(request, response);
        verify(context).setAuthentication(actualAuthentication);
        verify(contextService).runWithinContext(any(UserCredentials.class), any(Runnable.class));
    }


    @Test
    public void shouldNotAuthenticateWhenNoAuthenticationProvided() throws IOException, ServletException {
        SecurityContext context = mockSecurityContext();
        filter.doFilter(request, response, chain);

        verify(authenticationManager, times(0)).authenticate(any(Authentication.class));

        verify(chain).doFilter(request, response);
        verify(context, times(0)).setAuthentication(any(Authentication.class));
    }

    @Test
    public void shouldHandleException() throws IOException, ServletException {
        SecurityContext context = mockSecurityContext();
        when(request.getHeaders("Authorization")).thenReturn(Collections.enumeration(ImmutableSet.of("Basic XXX", "Bearer " + SAMPLE_TOKEN)));
        AuthenticationException exception = new UsernameNotFoundException("");
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(exception);

        filter.doFilter(request, response, chain);

        verify(authenticationEntryPoint).commence(request, response, exception);
        verify(chain, times(0)).doFilter(request, response);
        verify(context, times(0)).setAuthentication(any(Authentication.class));
    }

    @Test
    public void shouldNotAuthenticateIfAuthenticationInContext() throws IOException, ServletException {
        when(request.getHeaders("Authorization")).thenReturn(Collections.enumeration(ImmutableSet.of("Basic XXX", "Bearer " + SAMPLE_TOKEN)));
        SecurityContext context = mockSecurityContext();

        Authentication authentication = mock(AnonymousAuthenticationToken.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(context.getAuthentication()).thenReturn(authentication);

        filter.doFilter(request, response, chain);

        verify(authenticationManager, times(0)).authenticate(any(Authentication.class));
        verify(chain, times(1)).doFilter(request, response);
        verify(context, times(0)).setAuthentication(any(Authentication.class));
    }

    private SecurityContext mockSecurityContext() {
        mockStatic(SecurityContextHolder.class);
        SecurityContext context = mock(SecurityContext.class);
        PowerMockito.when(SecurityContextHolder.getContext()).thenReturn(context);
        return context;
    }

    private void mockAuthenticationMangerReturnArgument() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenAnswer(new Answer<Authentication>() {
            @Override
            public Authentication answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (Authentication) args[0];
            }
        });
    }

    private void mockContextServiceInvokeRunnable() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Runnable runnableObject = (Runnable)invocationOnMock.getArguments()[1];
                runnableObject.run();
                return null;
            }
        }).when(contextService).runWithinContext(any(UserCredentials.class), any(Runnable.class));
    }
}