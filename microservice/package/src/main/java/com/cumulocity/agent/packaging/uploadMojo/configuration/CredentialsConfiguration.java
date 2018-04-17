package com.cumulocity.agent.packaging.uploadMojo.configuration;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class CredentialsConfiguration {
    private String url;
    private String username;
    private String password;
}