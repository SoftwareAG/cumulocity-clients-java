package com.cumulocity.microservice.health.controller.configuration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;

import javax.annotation.PostConstruct;

@AutoConfigureMockMvc
@SpringBootTest(classes = {
        IndicatorConfiguration.class,
        TestConfiguration.MainConfiguration.class,
})
public class TestConfiguration {

    @SpringBootApplication
    public static class MainConfiguration {
    }

    @Autowired
    private MockMvc mvc;

    @PostConstruct
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(new MockMvcBuilder() {
            public MockMvc build() {
                return mvc;
            }
        });
    }
}
