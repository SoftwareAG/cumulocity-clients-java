package com.cumulocity.microservice.security.token;

import java.util.concurrent.Callable;

public interface JwtTokenAuthenticationLoader extends Callable<JwtTokenAuthentication> {
}
