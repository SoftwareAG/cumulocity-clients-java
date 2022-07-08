package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import lombok.*;
import org.svenson.JSONProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "aadSignatureVerificationConfigRepresentation")
public class AADSignatureVerificationConfigRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String publicKeyDiscoveryUrl;

}
