/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
