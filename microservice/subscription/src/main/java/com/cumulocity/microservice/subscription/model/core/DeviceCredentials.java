package com.cumulocity.microservice.subscription.model.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCredentials implements Credentials {
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
    @JsonProperty
    private String identifier;

    @Wither
    @JsonIgnore
    private String tfaToken;

}
