package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import lombok.*;
import org.svenson.JSONProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "adfsManifestSignatureVerificationConfigRepresentation")
public class ADFSManifestSignatureVerificationConfigRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String manifestUrl;

}
