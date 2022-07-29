package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionConfigurationRepresentation extends AbstractExtensibleRepresentation {
    public static final long MIN_ABSOLUTE_TIMEOUT_IN_MILLIS = 2L * 60L * 1000L;
    public static final long MIN_RENEWAL_TIMEOUT_IN_MILLIS = 60L * 1000L;

    @NotNull
    @Min(MIN_ABSOLUTE_TIMEOUT_IN_MILLIS)
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Long absoluteTimeoutMillis;

    @NotNull
    @Min(MIN_RENEWAL_TIMEOUT_IN_MILLIS)
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Long renewalTimeoutMillis;

    @NotNull
    @Min(1)
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Integer maximumNumberOfParallelSessions;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Boolean userAgentValidationRequired;
}
