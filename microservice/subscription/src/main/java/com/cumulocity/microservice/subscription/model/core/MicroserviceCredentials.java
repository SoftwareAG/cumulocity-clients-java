package com.cumulocity.microservice.subscription.model.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Wither;

@Data
@Builder
@ToString(of = {"tenant", "name"})
@NoArgsConstructor
@AllArgsConstructor
public class MicroserviceCredentials implements Credentials {

    @Wither
    @JsonProperty("tenant")
    private String tenant;

    @Wither
    @JsonProperty("name")
    private String name;

    @Wither
    @JsonIgnore
    private String password;

    @Wither
    @JsonIgnore
    private String tfaToken;

    public MicroserviceCredentials(String tenant, String name, String password) {
        this(tenant, name, password, null);
    }

    public DeviceCredentials withIdentifier(String identifier) {
        return DeviceCredentials.builder().tenant(tenant).name(name).password(password).identifier(identifier).tfaToken(tfaToken).build();
    }
}
