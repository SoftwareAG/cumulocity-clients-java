package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "defaultAccessRepresentation")
public class DefaultAccessRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Singular
    private List<String> defaultRoles;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Singular
    private List<String> defaultGroups;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Singular
    private List<String> defaultApplications;

}
