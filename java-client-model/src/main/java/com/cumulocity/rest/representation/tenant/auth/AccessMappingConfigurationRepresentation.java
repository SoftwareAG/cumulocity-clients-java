package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

/**
 * Stores configuration used for changing behaviour of dynamic access mapping
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "accessMappingConfigurationRepresentation")
public class AccessMappingConfigurationRepresentation extends AbstractExtensibleRepresentation {

    /**
     * Whether roles should be mapped from external source only when user is created
     */
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Boolean mapRolesOnlyForNewUser;


}
