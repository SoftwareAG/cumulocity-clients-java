package com.cumulocity.microservice.monitoring.health.controller.configuration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

@AutoConfigureMockMvc
@SpringBootTest(classes = {
        TestConfiguration.MainConfiguration.class,
        IndicatorConfiguration.class
})
public class TestConfiguration {

    @SpringBootApplication
    public static class MainConfiguration {
    }

    @Autowired
    private MockMvc mvc;

    @PostConstruct
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(() -> mvc);
    }
}
