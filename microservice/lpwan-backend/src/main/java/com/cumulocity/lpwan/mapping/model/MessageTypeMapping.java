package com.cumulocity.lpwan.mapping.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MessageTypeMapping {

    @JsonProperty("c8y_Registers")
    private Integer[] registerIndexes; 
}
