package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.jsonpredicate.JSONPredicateRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents information of mapping with condition in {@code when}, in result access to groups and applications
 * based on {@code thenApplications and thenGroups}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "accessMappingRepresentation")
public class AccessMappingRepresentation extends AbstractExtensibleRepresentation {

    /**
     * Requirements
     */
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private JSONPredicateRepresentation when;

    /**
     * Groups to be assigned
     */
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Singular
    private List<Long> thenGroups = new ArrayList<>();

    /**
     * Applications to be assigned
     */
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Singular
    private List<Long> thenApplications = new ArrayList<>();

}
