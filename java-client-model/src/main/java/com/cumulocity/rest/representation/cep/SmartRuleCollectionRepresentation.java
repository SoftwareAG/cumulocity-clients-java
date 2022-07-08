package com.cumulocity.rest.representation.cep;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.svenson.AbstractDynamicProperties;

import java.util.List;

@Data
@AllArgsConstructor
public class SmartRuleCollectionRepresentation extends AbstractDynamicProperties {

    private List<SmartRuleRepresentation> rules;

}
