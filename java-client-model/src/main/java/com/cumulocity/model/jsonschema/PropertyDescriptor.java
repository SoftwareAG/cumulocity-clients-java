package com.cumulocity.model.jsonschema;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PropertyDescriptor extends AbstractExtensibleRepresentation{

    String name;
    String description;
    String type;
    int minimum;
    int maximum;
}
