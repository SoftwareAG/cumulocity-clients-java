package com.cumulocity.agent.packaging.upload.configuration;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationConfiguration {
    private String name;
    private Boolean delete;
    private Boolean create;
    private Boolean failOnError;
    private List<String> subscriptions;
}
