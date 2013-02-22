package com.cumulocity.me.rest.validate;

import com.cumulocity.me.rest.representation.ResourceRepresentation;

public class NoOpRepresentationValidator implements RepresentationValidator {

    private static final NoOpRepresentationValidator INSTANCE = new NoOpRepresentationValidator();

    public static NoOpRepresentationValidator getInstance() {
        return INSTANCE;
    }

    private NoOpRepresentationValidator() { }

    public boolean supports(Class representationType) {
        return true;
    }

    public boolean isValid(ResourceRepresentation representation, RepresentationValidationContext context) {
        return true;
    }
}
