package com.cumulocity.rest.representation.application.microservice;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.DateTime;
import org.svenson.converter.JSONConverter;

@Data
@EqualsAndHashCode(callSuper = true)
public class MicroserviceBillingRepresentation extends AbstractExtensibleRepresentation {
    @Getter(onMethod_ = @JSONConverter(type = DateTimeConverter.class))
    private DateTime dateTime;
}

