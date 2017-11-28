package com.cumulocity.microservice.context.credentials;

import lombok.*;
import lombok.experimental.Wither;

@Wither
@Data
@Builder
@ToString(of = {"tenant", "username"})
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials implements Credentials {
    private String tenant;
    private String username;
    private String password;
    private String identifier;
    private String tfaToken;
    private String appKey;
}
