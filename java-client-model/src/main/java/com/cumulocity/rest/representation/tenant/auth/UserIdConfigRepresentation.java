package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "userIdConfigRepresentation")
public class UserIdConfigRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private boolean useConstantValue;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String jwtField;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String constantValue;

}
