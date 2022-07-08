package com.cumulocity.model.util;

import java.util.HashMap;
import java.util.Map;

public class AliasMapClassFinder extends ClassFinder{
    
    private final static Map<String, String> ALIASES =  new HashMap<String, String>();
    
    final Map<String, String> alias2ClassName = new HashMap<String, String>();
    
    public AliasMapClassFinder() {
        alias2ClassName.putAll(ALIASES);
    }
    
    @SuppressWarnings("rawtypes")
    public Class findClassByAlias(String aliasValue) {
        String className = alias2ClassName.get(aliasValue);
        if(className == null){
            return null;
        }
        return tryFindClassByClassName(className);
    }
    
    @SuppressWarnings("rawtypes")
    private Class tryFindClassByClassName(final String className) {
        try {
            return findClassByClassName(className);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
}
