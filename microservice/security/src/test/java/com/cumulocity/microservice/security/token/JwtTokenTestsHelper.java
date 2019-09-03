package com.cumulocity.microservice.security.token;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

public class JwtTokenTestsHelper {
    protected static final String SAMPLE_ENCODED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    protected static final String SAMPLE_XSRF_TOKEN = "QIR12IEKqP";
    protected static final String SAMPLE_TENANT_NAME = "tenant1234";
    protected static final String SAMPLE_USERNAME = "user1234";

    protected Jwt mockedJwtImpl() {
        return new Jwt() {
            @Override
            public String getClaims() {
                throw new IllegalStateException("Method should not be used in this test");
            }

            @Override
            public String getEncoded() {
                return SAMPLE_ENCODED_TOKEN;
            }

            @Override
            public void verifySignature(SignatureVerifier verifier) {
                throw new IllegalStateException("Method should not be used in this test");
            }

            @Override
            public byte[] bytes() {
                throw new IllegalStateException("Method should not be used in this test");
            }
        };
    }
}
