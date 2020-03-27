package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public class CumulocityOAuthMicroserviceFilterTest {

    private final static String SAMPLE_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOm51bGwsImlzcyI6ImN1bXVsb2NpdHkuZGVmYXVsdC5zdmMuY2x1c3Rlci5sb2NhbCIsImF1ZCI6ImN1bXVsb2NpdHkuZGVmYXVsdC5zdmMuY2x1c3Rlci5sb2NhbCIsInN1YiI6ImFkbWluIiwidGNpIjoiZDMwMTczNjYtY2Y3Yi00MjdlLWE2OTMtNzJiYjg2MGE5MDgzIiwiaWF0IjoxNTY1NzYxMTg0LCJuYmYiOjE1NjU3NjExODQsImV4cCI6MTU2Njk3MDc4NCwidGZhIjpmYWxzZSwidGVuIjoibWFuYWdlbWVudCIsInhzcmZUb2tlbiI6InZ2VXlpS3h6c1VHQlhNbGNPb2RrIn0.TDz9k0NfKeLK5f0dwZ_gqOWyweMLpaIdEtU6snos9_0ephtI4HibCVEOV9JPoHZnaqjAUyfmhQc7WN2JLpMX6Q";
    private final static String SAMPLE_X_XSRF_TOKEN = "vvUyiKxzsUGBXMlcOodk";

    private CumulocityOAuthMicroserviceFilter filter;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private ContextService<UserCredentials> contextService;

    private AuthenticationManager authenticationManager;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private UserCredentials userCredentials;


    @Before
    public void setup() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        chain = mock(FilterChain.class);
        authenticationEntryPoint = mock(AuthenticationEntryPoint.class);
        this.userCredentials = UserCredentials.builder().build();

        authenticationManager = mock(AuthenticationManager.class);
        contextService = mock(ContextService.class);
        filter = new CumulocityOAuthMicroserviceFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationEntryPoint(authenticationEntryPoint);
        filter.setUserContextService(contextService);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void shouldAuthenticateWithAuthorizationBearer() throws IOException, ServletException {
        when(request.getHeaders("Authorization")).thenReturn(Collections.enumeration(ImmutableSet.of("Basic XXX", "Bearer " + SAMPLE_TOKEN)));
        mockAuthenticationManager();
        mockContextServiceInvokeRunnable();

        filter.doFilter(request, response, chain);

        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(captor.capture());
        Authentication actualAuthentication = captor.getValue();
        assertThat(actualAuthentication).isExactlyInstanceOf(JwtTokenAuthentication.class);
        JwtCredentials credentials = (JwtCredentials) actualAuthentication.getCredentials();
        assertThat(credentials.getJwt().getEncoded()).isEqualTo(SAMPLE_TOKEN);

        verify(chain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(actualAuthentication);
        verify(contextService).runWithinContext(any(UserCredentials.class), any(Runnable.class));
    }

    @Test
    public void shouldAuthenticateWithAuthorizationCookie() throws IOException, ServletException {
        Cookie[] cookies = {new Cookie(AUTHORIZATION_KEY, SAMPLE_TOKEN)};
        when(request.getCookies()).thenReturn(cookies);
        when(request.getHeader("X-XSRF-TOKEN")).thenReturn(SAMPLE_X_XSRF_TOKEN);
        mockAuthenticationManager();
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
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(actualAuthentication);
        verify(contextService).runWithinContext(same(userCredentials), any(Runnable.class));
    }


    @Test
    public void shouldNotAuthenticateWhenNoAuthenticationProvided() throws IOException, ServletException {
        filter.doFilter(request, response, chain);

        verify(authenticationManager, times(0)).authenticate(any(Authentication.class));

        verify(chain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void shouldHandleException() throws IOException, ServletException {
        when(request.getHeaders("Authorization")).thenReturn(Collections.enumeration(ImmutableSet.of("Basic XXX", "Bearer " + SAMPLE_TOKEN)));
        AuthenticationException exception = new UsernameNotFoundException("");
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(exception);

        filter.doFilter(request, response, chain);

        verify(authenticationEntryPoint).commence(request, response, exception);
        verify(chain, times(0)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void shouldSkipAuthenticationIfAuthenticationWasSetBefore() throws IOException, ServletException {
       //given
        authenticateForTheFirstTime();

        //when
        filter.doFilter(request, response, chain);

        //then
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(chain, times(2)).doFilter(request, response);
    }

    private void authenticateForTheFirstTime() throws IOException, ServletException {
        when(request.getHeaders("Authorization")).thenReturn(Collections.enumeration(ImmutableSet.of("Basic XXX", "Bearer " + SAMPLE_TOKEN)));
        mockAuthenticationManager();
        mockContextServiceInvokeRunnable();
        filter.doFilter(request, response, chain);
    }

    private void mockAuthenticationManager() {
        when(authenticationManager.authenticate(any(JwtTokenAuthentication.class))).thenAnswer((Answer<JwtTokenAuthentication>) invocation -> {
            JwtTokenAuthentication jwtTokenAuthentication = invocation.getArgument(0);
            jwtTokenAuthentication.setUserCredentials(userCredentials);
            return jwtTokenAuthentication;
        });
    }

    private void mockContextServiceInvokeRunnable() {
        doAnswer(invocationOnMock -> {
            Runnable runnableObject = (Runnable)invocationOnMock.getArguments()[1];
            runnableObject.run();
            return null;
        }).when(contextService).runWithinContext(or(any(UserCredentials.class), Mockito.isNull()), any(Runnable.class));
    }
}