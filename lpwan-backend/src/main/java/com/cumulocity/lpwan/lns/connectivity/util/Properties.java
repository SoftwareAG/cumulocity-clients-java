package com.cumulocity.lpwan.lns.connectivity.util;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
@Setter
/**
 * A dynamic set of Properties
 */
public class Properties extends AbstractExtensibleRepresentation {

    public void addPropertyDescriptor(String propertyName, PropertyDescription propertyDescriptor) {
        this.set(propertyDescriptor,propertyName);
    }

}
