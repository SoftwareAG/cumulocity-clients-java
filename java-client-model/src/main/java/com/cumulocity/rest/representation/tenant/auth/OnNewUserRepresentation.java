package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "onNewUserRepresentation")
public class OnNewUserRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Map doNothing;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private DefaultAccessRepresentation defaultAccess;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private DynamicAccessMappingRepresentation dynamicMapping;

}
