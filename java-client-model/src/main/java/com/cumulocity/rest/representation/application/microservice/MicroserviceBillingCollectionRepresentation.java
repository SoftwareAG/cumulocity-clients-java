package com.cumulocity.rest.representation.application.microservice;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import lombok.*;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MicroserviceBillingCollectionRepresentation extends BaseCollectionRepresentation<MicroserviceBillingRepresentation> {

    @Getter(onMethod_ = @JSONTypeHint(MicroserviceBillingRepresentation.class))
    private List<MicroserviceBillingRepresentation> billings;

    @Override
    @JSONProperty(ignore = true)
    public Iterator<MicroserviceBillingRepresentation> iterator() {
        return billings.iterator();
    }

}
