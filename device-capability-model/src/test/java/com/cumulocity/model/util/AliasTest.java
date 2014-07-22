package com.cumulocity.model.util;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.util.Map.Entry;

import org.junit.Test;

public class AliasTest {

    @Test
    public void checkIfAliasesAreCorrect() throws Exception {
        AliasMapClassFinder aliasMapClassFinder = new AliasMapClassFinder();
        for (Entry<String, String> aliasAndClass : aliasMapClassFinder.alias2ClassName.entrySet()) {
            checkAlias(aliasAndClass.getKey(), aliasAndClass.getValue());
        }
    }

    private void checkAlias(String alias, String className) throws ClassNotFoundException {
        Class<?> clazz = this.getClass().getClassLoader().loadClass(className);
        Alias clazzAlias = findAliasAnnotation(clazz);
        assertThat(clazzAlias).describedAs("Class " + clazz + " has no alias annotation!").isNotNull();
        assertThat(clazzAlias.value()).describedAs("Class " + clazz + " has wrong annotation!").isEqualTo(alias);
    }

    private Alias findAliasAnnotation(Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Alias) {
                return (Alias) annotation;
            }
        }
        return null;

    }
}
