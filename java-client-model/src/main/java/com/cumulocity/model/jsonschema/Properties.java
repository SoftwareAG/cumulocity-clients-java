package com.cumulocity.model.jsonschema;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Properties extends AbstractExtensibleRepresentation {

    public void addPropertyDescriptor(String propertyName, PropertyDescriptor propertyDescriptor) {
        this.set(propertyDescriptor,propertyName);
    }
}
