package com.cumulocity.model.jsonschema;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@Setter

abstract public class BaseSchema extends AbstractExtensibleRepresentation {
    public String type = "c8y_JsonSchema";
}
