package com.cumulocity.rest.representation.application.microservice;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String name;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String domain;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String support;

}
