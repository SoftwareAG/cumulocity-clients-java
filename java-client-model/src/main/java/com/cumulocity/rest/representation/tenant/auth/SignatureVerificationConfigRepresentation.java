package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "signatureVerificationConfigRepresentation")
public class SignatureVerificationConfigRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private AADSignatureVerificationConfigRepresentation aad;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private ManualSignatureVerificationConfigRepresentation manual;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private ADFSManifestSignatureVerificationConfigRepresentation adfsManifest;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private JwksSignatureVerificationConfigRepresentation jwks;
}
