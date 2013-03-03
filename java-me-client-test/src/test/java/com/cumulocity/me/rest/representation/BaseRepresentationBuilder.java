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
package com.cumulocity.me.rest.representation;

import static com.cumulocity.me.model.util.ExtensibilityConverter.classToStringRepresentation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.cumulocity.me.model.ID;
import com.cumulocity.model.builder.IDBuilder;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

@SuppressWarnings("unchecked")
public abstract class BaseRepresentationBuilder<T extends AbstractExtensibleRepresentation, B extends BaseRepresentationBuilder<T, B>>
        extends AbstractObjectBuilder<T> {

    Map<String, Object> dynamicProperties = new LinkedHashMap<String, Object>();

    public B withID(final ID id) {
        setFieldValue("id", id);
        return (B) this;
    }

    public B withID(final IDBuilder id) {
        setFieldValueBuilder("id", id);
        return (B) this;
    }

    public B withSelf(final String value) {
        setFieldValue("self", value);
        return (B) this;
    }

    public B with(final Object object) {
        return with(classToStringRepresentation(object.getClass()), object);
    }
    
    public B with(final String name, final Object object) {
        dynamicProperties.put(name, object);
        return (B) this;
    }

    protected void fillInValues(T domainObject) {
        super.fillInValues(domainObject);
        for (Map.Entry<String, Object> entry : dynamicProperties.entrySet()) {
            domainObject.set(entry.getValue(), entry.getKey());
        }
    }

}
