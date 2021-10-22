package com.cumulocity.lpwan.lns.connectivity.util;

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
/**
 * The actual JSON Schema class.
 *
 * See https://docs.google.com/document/d/1iux9XA-RvmkQ3B_JLu3OKIqic7it_9s4I1IuHYq0_uU/edit#heading=h.bqw50lhopz2p
 * for Model description
 *
 */
public class JsonSchema extends BaseSchema{

    public static final String TYPE_STRING = "string";
    public static final String TYPE_NUMBER = "number";

    private String fragmentType;
    private String type = "object";
    private String title;
    private String titleDisplay;
    private String description;

    private Properties properties;

}
