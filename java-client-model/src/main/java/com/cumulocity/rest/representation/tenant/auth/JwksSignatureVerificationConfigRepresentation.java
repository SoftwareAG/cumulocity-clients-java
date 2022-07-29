package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import lombok.*;
import org.svenson.JSONProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwksSignatureVerificationConfigRepresentation extends AbstractExtensibleRepresentation {
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String jwksUri;
}
