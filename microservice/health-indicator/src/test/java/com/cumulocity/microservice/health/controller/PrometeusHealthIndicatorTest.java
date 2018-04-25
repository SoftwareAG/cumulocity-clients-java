package com.cumulocity.microservice.health.controller;

import com.cumulocity.microservice.health.controller.configuration.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.http.ContentType.TEXT;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = AFTER_CLASS)
@SpringBootTest(classes = TestConfiguration.class)
public class PrometeusHealthIndicatorTest {

    @Test
    @WithMockUser(authorities = "ROLE_ACTUATOR")
    public void prometeusShouldBeUp() {
        when()
                .get("/prometheus").

        then()
                .statusCode(200)
                .contentType(TEXT);
    }

    @Test
    public void prometeusShouldBeSecured() {
        when()
                .get("/prometheus").

        then()
                .statusCode(401);
    }
}
