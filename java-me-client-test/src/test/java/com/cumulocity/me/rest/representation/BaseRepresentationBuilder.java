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
        setObjectField("id", id);
        return (B) this;
    }

    public B withID(final IDBuilder id) {
        setObjectFieldBuilder("id", id);
        return (B) this;
    }

    public B withSelf(final String value) {
        setObjectField("self", value);
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
