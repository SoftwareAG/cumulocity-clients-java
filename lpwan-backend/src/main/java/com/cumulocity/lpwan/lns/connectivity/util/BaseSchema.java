package com.cumulocity.lpwan.lns.connectivity.util;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
/**
 * This is a general base class to model JSON Schema. This is used for generating JSON schemas that are used
 * by the "Generic UI" feature to render LWM2M Object UIs
 *
 * See https://docs.google.com/document/d/1iux9XA-RvmkQ3B_JLu3OKIqic7it_9s4I1IuHYq0_uU/edit#heading=h.bqw50lhopz2p
 * for Model description
 *
 */
abstract public class BaseSchema extends AbstractExtensibleRepresentation {

    public enum APPLICABLE_TO_KEY {
        GENERIC_UI
    }

    public Map<APPLICABLE_TO_KEY, Boolean> appliesTo = new HashMap<>();


    public String type = "c8y_JsonSchema";
}
