package com.cumulocity.microservice.monitoring;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ContextConfiguration(classes = MockMvcTestBase.TestConfig.class)
public abstract class MockMvcTestBase {

    @TestConfiguration
    @SpringBootConfiguration
    @EnableAutoConfiguration
    public static class TestConfig {
    }

    @Autowired
    protected MockMvc mvc;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(() -> mvc);
    }
}
