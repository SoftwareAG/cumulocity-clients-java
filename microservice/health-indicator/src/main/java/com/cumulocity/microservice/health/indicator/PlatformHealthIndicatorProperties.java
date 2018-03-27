package com.cumulocity.microservice.health.indicator;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("management.health.platform")
public class PlatformHealthIndicatorProperties {
    private String path = "/tenant/health";
    private Boolean details = true;
}
