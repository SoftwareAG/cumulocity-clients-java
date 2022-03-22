package com.cumulocity.lpwan.lns.instance.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@JsonDeserialize(using = LnsInstanceDeserializer.class)
@Getter
@Setter
public abstract class LnsInstance{
    private String name;
    private String description;
}

