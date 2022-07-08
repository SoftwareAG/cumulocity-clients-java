package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import lombok.*;
import org.joda.time.DateTime;
import org.svenson.JSONProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "signingCertificateRepresentation")
public class SigningCertificateRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String alg;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String publicKey;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String validFrom;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String validTill;

}
