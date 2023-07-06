package com.cumulocity.model.jsonschema;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

public class Properties{
    @JsonProperty("Rotor/Blade/Inclination_P01")
    public RotorBladeInclinationP01 rotorBladeInclination_P01;
}
