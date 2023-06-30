package com.cumulocity.model.jsonschema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class JsonSchema extends BaseSchema{

    private String type = "object";
    private Properties properties;
    private String[] required;
    private boolean additionalProperties;

}
