package com.cumulocity.microservice.security.token;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;

public class JwtTokenTestsHelper {
    protected static final String SAMPLE_ENCODED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIn0.dFG59id6EtICUV34DqALlPt3ptzuIq1IJ7tArEuQirU";
    protected static final String SAMPLE_XSRF_TOKEN = "QIR12IEKqP";
    protected static final String SAMPLE_TENANT_NAME = "tenant1234";
    protected static final String SAMPLE_USERNAME = "user1234";

    public static JWT mockedJwtImpl() {
        try {
            return JWTParser.parse(SAMPLE_ENCODED_TOKEN);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
