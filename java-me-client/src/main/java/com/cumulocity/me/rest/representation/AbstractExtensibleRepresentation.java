package com.cumulocity.me.rest.representation;

import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.lang.Set;
import com.cumulocity.me.model.util.ExtensibilityConverter;

public class AbstractExtensibleRepresentation extends BaseCumulocityResourceRepresentation {

    private Map attrs = new HashMap();

    public Map getAttrs() {
        return attrs;
    }

    public void setAttrs(Map attrs) {
        this.attrs = attrs;
    }

    public Object getProperty(String name) {
        return attrs.get(name);
    }

    public void setProperty(String name, Object value) {
        attrs.put(name, value);
    }

    public Set propertyNames() {
        return attrs.keySet();
    }

    /**
     * Sets a property referring to the given object.
     * The name of the property will be the fully qualified class name 
     * with dots replaced by underscores.<br>
     * For example, if the object is of type:<br>
     *      <ul>com.cumulocity.model.Coordinate</ul>
     *   then the property name will be:<br>
     *      <ul>"com_cumulocity_model_Coordinate"</ul>
     * @param object
     */
    public void set(Object object) {
        set(object, object.getClass());
    }

    /**
     * Sets a property referring to the given object,
     * using an arbitrary property name.
     * @param object
     * @param propertyName
     */
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
     *      <ul>com.cumulocity.model.Coordinate</ul>
     *   then the property name will be:<br>
     *      <ul> "com_cumulocity_model_Coordinate"</ul>
     * @param object
     * @param clazz
     */
    public void set(Object object, Class clazz) {
        setProperty(ExtensibilityConverter.classToStringRepresentation(clazz), object);
    }

    /**
     * Returns the object whose parameter name is given by clazz, or null
     * if no such property exists.
     * @see #set(Object)
     * @param clazz
     * @return
     */
    public Object get(Class clazz) {
        return getProperty(ExtensibilityConverter.classToStringRepresentation(clazz));
    }

    /**
     * Returns the object associated with the given property name, 
     * or null if no such property exists.
     * @param name
     * @return
     */
    public Object get(String name) {
        return getProperty(name);
    }

}
