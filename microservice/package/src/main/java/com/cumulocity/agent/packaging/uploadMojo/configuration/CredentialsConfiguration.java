package com.cumulocity.agent.packaging.uploadMojo.configuration;

import lombok.*;
import org.apache.commons.lang.StringUtils;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class CredentialsConfiguration {
    private String url;
    private String username;
    private String password;

    public boolean isPresent() {
        return StringUtils.isNotBlank(url)
                && StringUtils.isNotBlank(username)
                && StringUtils.isNotBlank(password);

    }
}