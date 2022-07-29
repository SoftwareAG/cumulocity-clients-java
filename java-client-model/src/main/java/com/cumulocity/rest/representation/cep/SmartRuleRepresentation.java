package com.cumulocity.rest.representation.cep;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.ResourceRepresentationWithId;
import lombok.*;
import org.joda.time.DateTime;
import org.svenson.converter.JSONConverter;

import java.util.List;
import java.util.Map;

/**
 * Represents "c8y.SmartRuleRepresentation" fragment.
 */
@Data
@With
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SmartRuleRepresentation extends AbstractExtensibleRepresentation implements ResourceRepresentationWithId, Cloneable{

    /**
     * SmartRule name (Scenario name).
     */
    private String ruleTemplateName;

    /**
     * Smartrule cep module managed object id.
     */
    @Getter(onMethod_ = @JSONConverter(type = IDTypeConverter.class))
    private GId id;

    /**
     * Esper module managed object id or Apama scenario instance id.
     *
     */
    @Getter(onMethod_ = @JSONConverter(type = IDTypeConverter.class))
    private GId cepModuleId;

    private Map<String, Object> config;

    private List<String> enabledSources;

    private List<String> disabledSources;

    private Boolean enabled;

    private String type;

    private String name;

    @Getter(onMethod_ = @JSONConverter(type = DateTimeConverter.class))
    private DateTime lastUpdated;

    @Getter(onMethod_ = @JSONConverter(type = DateTimeConverter.class))
    private DateTime creationTime;

    /**
     * Esper module parsed body.
     */
    private String body;

    @Override
    public SmartRuleRepresentation clone() {
        return toBuilder().build();
    }
}
