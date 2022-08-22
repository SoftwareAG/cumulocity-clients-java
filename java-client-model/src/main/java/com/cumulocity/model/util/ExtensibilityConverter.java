package com.cumulocity.model.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.time.Duration;

/**
 * Converts between Java types and JSON according to the extensibility naming rules.
 */
public class ExtensibilityConverter {

    private static class SingletonHelper {
        private static final ExtensibilityConverter INSTANCE = new ExtensibilityConverter(Duration.ofHours(12), new AliasMapClassFinder());
    }

    @Getter(AccessLevel.PROTECTED)
    @SuppressWarnings("rawtypes")
    private final Cache<String, Class> foundClasses;

    @SuppressWarnings("rawtypes")
    private final Cache<Class, String> canonicalNames;

    private final ClassFinder classFinder;

    protected ExtensibilityConverter(final Duration cacheTimeout, final ClassFinder classFinder) {
        this.foundClasses = CacheBuilder.newBuilder()
                .expireAfterAccess(cacheTimeout)
                .build();
        this.canonicalNames = CacheBuilder.newBuilder()
                .expireAfterAccess(cacheTimeout)
                .build();
        this.classFinder = classFinder;
    }

    private static ExtensibilityConverter getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * Converts the given name into a class using the extensibility rules where underscores ("_") are
     * converted to dots ("."). For example:<br>
     * name = "com_cumulocity_model_idtype_Reference" would return the class com.cumulocity.mode.idtype.Reference
     *
     * @throws ClassNotFoundException if the name cannot be converted to a class using the extensibility rules
     */
    @SuppressWarnings("rawtypes")
    public static Class classFromExtensibilityString(final String name) throws ClassNotFoundException {
        return getInstance().classFromExtensibilityString2(name);
    }

    public static boolean isConvertable(final String name) {
        return getInstance().foundClasses.getIfPresent(name) == null || getInstance().foundClasses.getIfPresent(name) != ExtensibilityMappingFailed.class;
    }

    @SuppressWarnings("rawtypes")
    protected Class classFromExtensibilityString2(final String name) throws ClassNotFoundException {
        Class alreadyFoundClass = foundClasses.getIfPresent(name);
        if (alreadyFoundClass != null) {
            if (alreadyFoundClass.equals(ExtensibilityMappingFailed.class)) {
                throw new ClassNotFoundException();
            } else {
                return alreadyFoundClass;
            }
        } else {
            return findClass(name);
        }
    }

    @SuppressWarnings("rawtypes")
    private Class findClass(String name) throws ClassNotFoundException {
        Class foundClass = null;
        try {
            foundClass = classFinder.findClassByAlias(name);
            if (foundClass == null) {
                String className = name.replace('_', '.');
                foundClass = classFinder.findClassByClassName(className);
            }
        } finally {
            if (foundClass == null) {
                foundClasses.put(name, ExtensibilityMappingFailed.class);
                throw new ClassNotFoundException();
            }
            foundClasses.put(name, foundClass);
        }
        return foundClass;
    }

    /**
     * Returns a string representation of the canonical name of the given class
     * but with dots (".") replaced with underscores ("_").
     * Useful for creating JSON property names from class names.
     */
    public static <T> String classToStringRepresentation(final Class<T> clazz) {
        return getInstance().classToStringRepresentation2(clazz);
    }

    private <T> String classToStringRepresentation2(final Class<T> clazz) {
        String alias = tryGetAlias(clazz);
        if (alias != null) {
            return alias;
        }

        String canonicalName = canonicalNames.getIfPresent(clazz);
        if (canonicalName == null) {
            canonicalName = clazz.getCanonicalName().replace(".", "_").intern();
            canonicalNames.put(clazz, canonicalName);
        }
        return canonicalName;
    }

    private <T> String tryGetAlias(final Class<T> clazz) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType().equals(Alias.class)) {
                return ((Alias) annotation).value();
            }
        }
        return null;
    }

    private class ExtensibilityMappingFailed {
    }
}
