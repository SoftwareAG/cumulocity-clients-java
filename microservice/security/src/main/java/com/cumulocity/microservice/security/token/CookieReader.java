package com.cumulocity.microservice.security.token;

import lombok.experimental.UtilityClass;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public final class CookieReader {

    public static final String AUTHORIZATION_KEY = "authorization";

    public static Optional<Cookie> readAuthorizationCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(cookie -> AUTHORIZATION_KEY.equals(cookie.getName())).findFirst();
    }

}
