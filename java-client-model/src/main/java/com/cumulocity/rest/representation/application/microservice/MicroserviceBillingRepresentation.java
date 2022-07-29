package com.cumulocity.rest.representation.application.microservice;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.DateTime;
import org.svenson.converter.JSONConverter;

import static com.cumulocity.rest.representation.annotation.Command.CREATE;

@Data
@EqualsAndHashCode(callSuper = true)
public class MicroserviceBillingRepresentation extends AbstractExtensibleRepresentation {
    @NotNull(operation = CREATE)
    @Getter(onMethod_ = @JSONConverter(type = DateTimeConverter.class))
    private DateTime dateTime;
}

