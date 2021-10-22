package com.cumulocity.lpwan.lns.connectivity.util;

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
public class PropertyDescription extends AbstractExtensibleRepresentation{

    String label;
    boolean required;
    String defaultValue;
    String url;
    String min;
    String max;
    int minLength;
    int maxLength;
    String regExpr;
    String type;
}
