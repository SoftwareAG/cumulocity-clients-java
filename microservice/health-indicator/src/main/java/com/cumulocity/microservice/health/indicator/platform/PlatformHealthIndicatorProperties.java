package com.cumulocity.microservice.health.indicator.platform;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("management.health.platform")
public class PlatformHealthIndicatorProperties {
    private String path = "/user/currentUser";
    private Boolean detailsEnabled = true;
    private String detailsPath = "/tenant/health";
}
