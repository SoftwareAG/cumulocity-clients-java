package com.cumulocity.microservice.monitoring.version.indicator;

import com.cumulocity.microservice.monitoring.MockMvcTestBase;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.info.build.location=classpath:META-INF/test-build-info.properties"
})
public class VersionIndicatorWithPropertySetTest extends MockMvcTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_ACTUATOR")
    public void versionShouldBeUp() {

        ValidatableMockMvcResponse response = when()
                .get("/version")
                .then()
                .statusCode(SC_OK);

        assertThat(response.extract().body().asString())
                .isEqualTo("43.42.41.40");
    }
}
