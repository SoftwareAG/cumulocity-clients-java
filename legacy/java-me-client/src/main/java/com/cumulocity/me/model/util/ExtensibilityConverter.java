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
package com.cumulocity.me.model.util;

import com.cumulocity.me.model.ExtensibleEnum;

/**
 * Converts between Java types and JSON according to the extensibility naming rules.
 */
public class ExtensibilityConverter {

    private static ExtensibilityConverter instance = new ExtensibilityConverter();
    private ClassFinder classFinder = new ClassFinder();

    protected void setClassFinder(ClassFinder classFinder) {
        this.classFinder = classFinder;
    }

    protected static void setInstance(ExtensibilityConverter instance) {
        ExtensibilityConverter.instance = instance;
    }

    protected static ExtensibilityConverter getInstance() {
        return instance;
    }

    /**
     * Converts the given name into a class using the extensibility rules where underscores are
     * converted to dots ("_" => "."). For example:<br>
     * name = "com_cumulocity_model_idtype_Reference" would return the class com.cumulocity.mode.idtype.Reference
     *
     * @throws ClassNotFoundException if the name cannot be converted to a class using the extensibility rules
     */
    public static Class classFromExtensibilityString(final String name) throws ClassNotFoundException {
        return instance.classFromExtensibilityString2(name);
    }

    public Class classFromExtensibilityString2(final String name) throws ClassNotFoundException {
        return findClass(name);
    }

    private Class findClass(String name) throws ClassNotFoundException {
        String className = name.replace('_', '.');
        Class foundClass = ExtensibilityMappingFailed.class;
        foundClass = classFinder.findClass(className);
        return foundClass;
    }

    /**
     * Returns a string representation of the canonical name of the given class
     * but with dots (".") replaced with underscores ("_").
     * Useful for creating JSON property names from class names.
     */
    public static String classToStringRepresentation(final Class clazz) {
        return instance.classToStringRepresentation2(clazz);
    }

    public String classToStringRepresentation2(final Class clazz) {
        return clazz.getName().replace('.', '_');
    }

    /**
     * Returns a string representation of the given extensible enum
     * but with dots (".") in the class name replaced with underscores ("_").
     * For example:<br>
     * com.othercorp.statuses.SOMESTATUS => "com_othercorp_statuses.SOMESTATUS"
     * Useful for creating JSON property names from enums.
     */
    public static String extensibleEnumToStringRepresentation(final ExtensibleEnum e) {
        return instance.extensibleEnumToStringRepresentation2(e);
    }

    public String extensibleEnumToStringRepresentation2(final ExtensibleEnum e) {
        String classPart = classToStringRepresentation2(e.getClass());

        return classPart + "." + e.name();
    }

    protected static class ClassFinder {
    	
        protected Class findClass(final String className) throws ClassNotFoundException {
            return Class.forName(className);
        }
    }

    private static class ExtensibilityMappingFailed {
    }
}
