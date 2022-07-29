package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import lombok.*;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadedTrustedCertInfoRepresentation extends AbstractExtensibleRepresentation {

    static final String WRONG_VALUE_MESSAGE = "field should contain value ENABLED or DISABLED";
    static final String CHECK_VALUE_REGEX =  "ENABLED|DISABLED";

    @NonNull
    @NotNull(operation = Command.CREATE)
    private String certInPemFormat;

    @NonNull
    @NotNull(operation = Command.CREATE)
    @Pattern(regexp = CHECK_VALUE_REGEX, message = WRONG_VALUE_MESSAGE)
    private String status;

    private String name;
    private boolean autoRegistrationEnabled;
}
