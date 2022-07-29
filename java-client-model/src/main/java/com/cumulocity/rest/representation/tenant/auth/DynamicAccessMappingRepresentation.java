package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a list of dynamic mappings @see com.cumulocity.rest.representation.tenant.auth.AccessMappingRepresentation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "dynamicAccessMappingRepresentation")
public class DynamicAccessMappingRepresentation extends AbstractExtensibleRepresentation {

    /**
     * List of mappings
     */
    @Singular
    @Getter(onMethod_ = @JSONTypeHint(AccessMappingRepresentation.class))
    @Setter(onMethod_ = @JSONTypeHint(AccessMappingRepresentation.class))
    private List<AccessMappingRepresentation> mappings = new ArrayList<>();


    /**
     * Configuration of dynamic access mapping
     */
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private AccessMappingConfigurationRepresentation configuration;
}
