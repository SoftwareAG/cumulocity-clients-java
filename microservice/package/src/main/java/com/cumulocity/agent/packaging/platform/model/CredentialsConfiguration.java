package com.cumulocity.agent.packaging.platform.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class CredentialsConfiguration {
    private String baseUrl;
    private String tenant;
    private String username;
    private String password;
}