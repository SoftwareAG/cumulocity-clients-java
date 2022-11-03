package com.cumulocity.rest.representation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.EqualsAndHashCode;
import org.svenson.DynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.util.ExtensibilityConverter;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class AbstractExtensibleRepresentation  extends BaseResourceRepresentation implements DynamicProperties {

    private Map<String, Object> attrs = new HashMap<String, Object>();

    @JSONProperty(ignore = true)
    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    public Object getProperty(String name) {
        return attrs.get(name);
    }

    public void setProperty(String name, Object value) {
        attrs.put(name, value);
    }

    public Object removeProperty(String name) {
        return attrs.remove(name);
    }

    public boolean hasProperty(String name) {
        return attrs.containsKey(name);
    }

    public Set<String> propertyNames() {
        return attrs.keySet();
    }

    /**
     * Sets a property referring to the given object.
     * The name of the property will be the fully qualified class name
     * with dots replaced by underscores.<br>
     * For example, if the object is of type:<br>
     *      <ul><li>com.cumulocity.model.Coordinate</li></ul>
     *   then the property name will be:<br>
     *      <ul><li>"com_cumulocity_model_Coordinate"</li></ul>
     * @param object an object to set
     */
    @JSONProperty(ignore = true)
    public void set(Object object) {
        set(object, object.getClass());
    }

    /**
     * Sets a property referring to the given object,
     * using an arbitrary property name.
     * @param object property value
     * @param propertyName property name
     */
    @JSONProperty(ignore = true)
    public void set(Object object, String propertyName) {
        setProperty(propertyName, object);
    }

    /**
     * Sets a property referring to the given object.
     * The name of the property will be the fully qualified class name
     * of the given class, with dots replaced by underscores.<br>
     * This can be useful if you want to name the property after the base class
     * rather than the actual class of object.<br>
     * For example, if clazz is of type:<br>
     *      <ul><li>com.cumulocity.model.Coordinate</li></ul>
     *   then the property name will be:<br>
     *      <ul><li>"com_cumulocity_model_Coordinate"</li></ul>
     * @param object object to set
     * @param clazz object class
     * @param <T> generic type of the class
     */
    @JSONProperty(ignore = true)
    public <T> void set(Object object, Class<T> clazz) {
        setProperty(ExtensibilityConverter.classToStringRepresentation(clazz), object);
    }

    /**
     * Returns the object whose parameter name is given by clazz, or null
     * if no such property exists, or it is invalid.
     *
     * @param clazz a class of the property
     * @param <T> generic type fo the class
     * @return property value
     * @see #set(Object)
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        Object property = getProperty(ExtensibilityConverter.classToStringRepresentation(clazz));
        return clazz.isInstance(property) ? (T) property : null;
    }

    /**
     * Returns the object associated with the given property name,
     * or null if no such property exists.
     * @param name property name
     * @return value of the property
     */
    public Object get(String name) {
        return getProperty(name);
    }
}
