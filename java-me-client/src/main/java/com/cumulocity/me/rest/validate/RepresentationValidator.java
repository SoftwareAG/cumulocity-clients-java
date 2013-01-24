package com.cumulocity.me.rest.validate;

import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

public interface RepresentationValidator {
    
    boolean supports(Class representationType);
    
    boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context);
}
