package com.cumulocity.model.util;


public abstract class ClassFinder {
    
    @SuppressWarnings("rawtypes")
    public Class findClassByClassName(final String className) throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // try with different classLoader (osgi)
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            return Class.forName(className, true, contextClassLoader);
        }
    }
    
    @SuppressWarnings("rawtypes")
    public abstract Class findClassByAlias(String aliasValue);

}
