package com.cumulocity.model.jsonschema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class RotorBladeInclinationP01{
    public String type;
    public String description;
    @Pattern(regexp = ".+@.+\\\\.[a-z]+")
    public String name;
    @Min(0)
    public int minimum;
    @Max(45)
    public int maximum;
}
