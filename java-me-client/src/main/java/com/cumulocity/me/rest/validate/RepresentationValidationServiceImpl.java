package com.cumulocity.me.rest.validate;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.Collection;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.rest.convert.ConversionException;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;

public class RepresentationValidationServiceImpl implements RepresentationValidationService {

    private final Collection validators = new ArrayList();
    
    public RepresentationValidationServiceImpl() {
    }
    
    public RepresentationValidationServiceImpl(Collection collection) {
		Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object validator = iterator.next();
            if (!(validator instanceof RepresentationValidator)) {
                throw new IllegalArgumentException(validator + " must be a RepresentationValidator!");
            }
            register((RepresentationValidator) validator);
        }
    }

    public void register(RepresentationValidator validator) {
        validators.add(validator);
    }
    
    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (representation == null) {
            return true; // FIXME should it be this way?
        }
        return findValidator(representation.getClass()).isValid(representation, context);
    }
    
    private RepresentationValidator findValidator(Class representationType) {
    	Iterator iterator = validators.iterator();
        while (iterator.hasNext()) {
            RepresentationValidator validator = ((RepresentationValidator) iterator.next());
            if (validator.supports(representationType)) {
                return validator;
            }
        }
        throw new ConversionException("Unsupported representation: " + representationType);
    }

}
