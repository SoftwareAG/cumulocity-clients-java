package com.cumulocity.rest.representation.connector;

import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.ResourceRepresentation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.svenson.AbstractDynamicProperties;
import org.svenson.converter.JSONConverter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorReferenceRepresentation extends AbstractDynamicProperties implements ResourceRepresentation {

    @Getter(onMethod_ = @JSONConverter(type = IDTypeConverter.class))
    private GId id;
    private String name;
    private String domain;
}
