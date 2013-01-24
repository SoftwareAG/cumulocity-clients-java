package com.cumulocity.me.rest.validate;

import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

public interface RepresentationValidationService {
    
    void register(RepresentationValidator validator);

    boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context);
}
