package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import lombok.*;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ManualSignatureVerificationConfigRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Boolean certIdFromField;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String certIdField;

    @Getter(onMethod_ = {
            @JSONProperty(ignoreIfNull = true),
            @JSONTypeHint(SigningCertificateRepresentation.class)
    })
    private Map<String, SigningCertificateRepresentation> certificates;

    @Builder(builderMethodName = "manualSignatureVerificationConfigRepresentation")
    public ManualSignatureVerificationConfigRepresentation(boolean certIdFromField, String certIdField, @Singular Map<String, SigningCertificateRepresentation> certificates) {
        this.certIdFromField = certIdFromField;
        this.certIdField = certIdField;
        this.certificates = certificates == null ? null : new HashMap<>(certificates);
    }
}
