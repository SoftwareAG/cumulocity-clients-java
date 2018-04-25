package com.cumulocity.agent.packaging.uploadMojo.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfiguration {
    private String groupId;
    private String artifactId;
    private String name;
    private Boolean delete;
    private Boolean create;
    private Boolean skip;
    private List<String> subscriptions;
}
