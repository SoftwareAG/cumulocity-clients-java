package com.cumulocity.rest.representation.tenant;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.svenson.DynamicProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionsRepresentation extends AbstractExtensibleRepresentation implements DynamicProperties {
    @Singular
    private Map<String, Object> properties = new HashMap<>();

    public String getProperty(String name) {
        final Object o = properties.get(name);
        if (o != null) {
            return o.toString();
        }
        return null;
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    public Set<String> propertyNames() {
        return properties.keySet();
    }

    @Override
    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }

    @Override
    public Object removeProperty(String name) {
        return properties.remove(name);
    }
}
