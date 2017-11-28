package com.cumulocity.microservice.context.credentials;

import lombok.*;
import lombok.experimental.Wither;

@Data
@Wither
@Builder
@ToString(of = {"tenant", "username"})
@NoArgsConstructor
@AllArgsConstructor
public class MicroserviceCredentials implements Credentials {
    private String tenant;
    private String username;
    private String password;
    private String tfaToken;
    private String appKey;

    public UserCredentials withIdentifier(String identifier) {
        return UserCredentials.builder()
                .tenant(tenant)
                .username(username)
                .password(password)
                .identifier(identifier)
                .tfaToken(tfaToken)
                .appKey(appKey)
                .build();
    }
}
