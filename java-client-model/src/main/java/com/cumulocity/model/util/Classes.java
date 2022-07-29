package com.cumulocity.model.util;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ClassUtils;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

@UtilityClass
public class Classes {

    /**
    Check if the given type represents a "simple" property: a primitive, a String or other CharSequence, a Number, a Date, a URI, a URL, a Locale, a Class, or a corresponding array.
    Used to determine properties to check for a "simple" dependency-check.
            Params:
    clazz – the type to check
    Returns:
    whether the given type represents a "simple" property
    */
    public static boolean isSimpleProperty(Class<?> clazz) {
        Preconditions.checkNotNull(clazz, "Class must not be null");
        return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
    }

    /**
     * Check if the given type represents a "simple" value type:
     * a primitive, a String or other CharSequence, a Number, a Date,
     * a URI, a URL, a Locale or a Class.
     * @param clazz the type to check
     * @return whether the given type represents a "simple" value type
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
        return ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.isEnum() ||
                CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz) ||
                clazz.equals(URI.class) || clazz.equals(URL.class) ||
                clazz.equals(Locale.class) || clazz.equals(Class.class);
    }
}
