package com.cumulocity.microservice.logging.model;

import lombok.Data;

@Data
public class LoggingConfiguration {
    private String directory;
    private String file;
}
