package com.cumulocity.microservice.subscription.model;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

import java.util.List;

@Data
@Builder(builderMethodName = "microserviceMetadataRepresentation")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MicroserviceMetadataRepresentation extends AbstractExtensibleRepresentation {

    @Singular
    private List<String> requiredRoles;

    @Singular
    private List<String> roles;

    private String url;

    @JSONProperty(ignoreIfNull = true)
    public List<String> getRequiredRoles() {
        return requiredRoles;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getUrl() {
        return url;
    }

    @JSONProperty(ignoreIfNull = true)
    public List<String> getRoles() {
        return roles;
    }
}
