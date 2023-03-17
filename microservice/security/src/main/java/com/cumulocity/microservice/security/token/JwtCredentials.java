package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.nimbusds.jwt.JWT;

public interface JwtCredentials {
     JWT getJwt();
    UserCredentials toUserCredentials(String tenantName, JwtTokenAuthentication jwtTokenAuthentication);
}
