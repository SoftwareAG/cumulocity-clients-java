package com.cumulocity.rest.representation.application.microservice;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.Data;

@Data
public class ExtensionRepresentation extends AbstractExtensibleRepresentation {
    private String type;
}
