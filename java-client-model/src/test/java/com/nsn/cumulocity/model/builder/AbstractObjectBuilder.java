package com.nsn.cumulocity.model.builder;

import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for Implementation of the Domain Object Builder Pattern. See also the <a
 * href="https://startups.jira.com/wiki/display/MTM/DomainObject+Patterns">wiki</a>.
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class AbstractObjectBuilder<T> {

    protected final Map<String, Object> values = new LinkedHashMap<String, Object>();

    protected final Map<String, Collection<?>> collectionValues = new LinkedHashMap<String, Collection<?>>();

    protected final Map<String, AbstractObjectBuilder<?>> builders = new LinkedHashMap<String, AbstractObjectBuilder<?>>();

    protected final Map<String, List<AbstractObjectBuilder<?>>> collectionBuilders = new LinkedHashMap<String, List<AbstractObjectBuilder<?>>>();

    private T domainObject = null;

    public synchronized T build() {
        if (domainObject == null) {
            domainObject = createDomainObject();
            fillInValues(domainObject);
            fillInBuilderValues(domainObject);
            fillInCollectionValues(domainObject);
            fillInCollectionBuilderValues(domainObject);
        }
        return domainObject;
    }

    protected abstract T createDomainObject();

    protected Object getFieldValue(final String fieldName) {
        return values.get(fieldName);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<String, Object>(values);

        for (Map.Entry<String, AbstractObjectBuilder<?>> entry : builders.entrySet()) {
            result.put(entry.getKey(), entry.getValue().build());
        }

        for (Map.Entry<String, Collection<?>> entry : collectionValues.entrySet()) {
            Collection value = createListValue(List.class, entry.getValue());
            result.put(entry.getKey(), value);
        }

        for (Map.Entry<String, List<AbstractObjectBuilder<?>>> entry : collectionBuilders.entrySet()) {
            Collection value = createBuilderListValue(List.class, entry.getValue());
            result.put(entry.getKey(), value);
        }
        return result;
    }

    protected void setFieldValue(final String fieldName, final Object value) {
        values.put(fieldName, value);
        builders.remove(fieldName);
    }

    protected void setFieldValues(final AbstractObjectBuilder<?> other) {
        for (String fieldName : other.values.keySet()) {
            values.put(fieldName, other.values.get(fieldName));
        }
    }

    protected void setFieldValueBuilder(final String fieldName, final AbstractObjectBuilder<?> builder) {
        builders.put(fieldName, builder);
    }

    protected void addCollectionFieldValue(final String fieldName, final Object value) {
        Collection values = collectionValues.get(fieldName);
        if (values == null) {
            values = new ArrayList();
            collectionValues.put(fieldName, values);
        }
        values.add(value);
    }

    protected void addCollectionFieldValueBuilder(final String fieldName, final AbstractObjectBuilder<?> builder) {
        List<AbstractObjectBuilder<?>> builderList = collectionBuilders.get(fieldName);
        if (builderList == null) {
            builderList = new ArrayList<AbstractObjectBuilder<?>>();
            collectionBuilders.put(fieldName, builderList);
        }
        builderList.add(builder);
    }

    protected void setCollectionFieldValueBuilders(String fieldName, List<AbstractObjectBuilder<?>> groupBuilders) {
        collectionBuilders.put(fieldName, groupBuilders);
    }

    protected void fillInValues(final T domainObject) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            setFieldValue(domainObject, entry.getKey(), entry.getValue());
        }
    }

    protected void fillInBuilderValues(final T domainObject) {
        for (Map.Entry<String, AbstractObjectBuilder<?>> entry : builders.entrySet()) {
            setFieldValue(domainObject, entry.getKey(), entry.getValue().build());
        }
    }

    protected void fillInCollectionValues(T domainObject) {
        for (Map.Entry<String, Collection<?>> entry : collectionValues.entrySet()) {
            Field field = findField(domainObject.getClass(), entry.getKey());
            Collection value = createListValue(field.getType(), entry.getValue());
            makeAccessible(field);
            setField(field, domainObject, value);
        }
    }

    private Collection createListValue(Class<?> collectionType, Collection<?> valuesList) {
        Collection builtValues = newCollection(collectionType);
        builtValues.addAll(valuesList);
        return builtValues;
    }

    private void fillInCollectionBuilderValues(T domainObject) {
        for (Map.Entry<String, List<AbstractObjectBuilder<?>>> entry : collectionBuilders.entrySet()) {
            Field field = findField(domainObject.getClass(), entry.getKey());
            Collection value = createBuilderListValue(field.getType(), entry.getValue());
            setField(field, domainObject, value);
        }
    }

    private Collection createBuilderListValue(Class<?> collectionType, List<AbstractObjectBuilder<?>> builderList) {
        Collection builtValues = newCollection(collectionType);
        for (AbstractObjectBuilder<?> builder : builderList) {
            builtValues.add(builder.build());
        }
        return builtValues;
    }

    private Collection newCollection(Class<?> collectionType) {
        if (Set.class.isAssignableFrom(collectionType)) {
            return new HashSet();
        }
        if (List.class.isAssignableFrom(collectionType)) {
            return new ArrayList();
        }
        throw new IllegalArgumentException("Unknown collection type: " + collectionType + "!");
    }

    private void setFieldValue(Object target, String fieldName, Object value) {
        Field field = findField(target.getClass(), fieldName);
        makeAccessible(field);
        setField(field, target, value);
    }


}
