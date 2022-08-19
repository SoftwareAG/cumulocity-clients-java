package com.cumulocity.rest.representation.application.microservice;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourcesRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String cpu;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String memory;

}
