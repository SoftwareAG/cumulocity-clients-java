package com.cumulocity.model.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ExtensibilityConverterTest {

    public static final String SOME_UNKNOWN_CLASS_REFERENCE = "some_unknown_class_GId";

    @SuppressWarnings("rawtypes")
    @Test
    public void classFromExtensibilityStringShouldReturnClass() throws ClassNotFoundException {
        //given
        String name = "com_cumulocity_model_idtype_GId";

        //when
        Class clazz = ExtensibilityConverter.classFromExtensibilityString(name);

        //then
        assertEquals(com.cumulocity.model.idtype.GId.class, clazz);
    }

    @Test
    public void classFromExtensibilityStringAliasShouldReturnClass() throws ClassNotFoundException {
        //given
        String name = SimpleBean.ALIAS;
        AliasMapClassFinder aliasMapClassFinder = new AliasMapClassFinder();
        aliasMapClassFinder.alias2ClassName.put(name, SimpleBean.class.getName());
        ExtensibilityConverter converter = new ExtensibilityConverter(Duration.ofHours(12), aliasMapClassFinder);

        //when
        Class<?> clazz = converter.classFromExtensibilityString2(name);

        //then
        assertEquals(SimpleBean.class, clazz);
    }

    @Test
    public void classToStringRepresentationReturnAlias() {
        //when
        String alias = ExtensibilityConverter.classToStringRepresentation(SimpleBean.class);

        //then
        assertEquals(SimpleBean.ALIAS, alias);
    }

    @Test
    public void shouldRaiseExceptionIfClassNotInClasspath() {
        //when
        Throwable catched = catchThrowable(() -> ExtensibilityConverter.classFromExtensibilityString(SOME_UNKNOWN_CLASS_REFERENCE));

        //then
        assertThat(catched).isInstanceOf(ClassNotFoundException.class);
    }

    @Test
    public void shouldRaiseExceptionIfNameDoesNotContainUnderscore() {
        //given
        String name = "someNameWithoutUnderscores";

        //when
        Throwable catched = catchThrowable(() -> ExtensibilityConverter.classFromExtensibilityString(name));

        //then
        assertThat(catched).isInstanceOf(ClassNotFoundException.class);
    }

    @Test
    public void shouldNotCallClassForNameSecondTimeSameNameIsPassedForFoundClass() throws Exception {
        // given
        String name = "java_lang_String";
        String convertedName = name.replace('_', '.');
        AliasMapClassFinder classFinder = spy(new AliasMapClassFinder());
        ExtensibilityConverter converter = new ExtensibilityConverter(Duration.ofHours(12), classFinder);

        // when
        converter.classFromExtensibilityString2(name);
        converter.classFromExtensibilityString2(name);

        // then
        verify(classFinder, times(1)).findClassByAlias(name);
        verify(classFinder, times(1)).findClassByClassName(convertedName);
    }

    @Test
    public void shouldNotCallClassForNameSecondTimeSameNameIsPassedForNotFoundClass() throws Exception {
        // given
        String name = SOME_UNKNOWN_CLASS_REFERENCE;
        String convertedName = name.replace('_', '.');
        AliasMapClassFinder classFinder = spy(new AliasMapClassFinder());
        ExtensibilityConverter converter = new ExtensibilityConverter(Duration.ofHours(12), classFinder);

        // when
        try {
            converter.classFromExtensibilityString2(name);
            fail("ClassNotFound expected");
        } catch (ClassNotFoundException e) {
        }
        try {
            converter.classFromExtensibilityString2(name);
            fail("ClassNotFound expected");
        } catch (ClassNotFoundException e) {
        }

        // then
        verify(classFinder, times(1)).findClassByClassName(convertedName);
    }

    @Test
    void shouldInvalidateCacheAfterTimeout() throws Exception {
        //given
        String name = "com_cumulocity_model_idtype_GId";
        ExtensibilityConverter converter = new ExtensibilityConverter(Duration.ofMillis(50), new AliasMapClassFinder());

        //when
        converter.classFromExtensibilityString2(name);

        //then
        assertThat(converter.getFoundClasses().getIfPresent(name)).isNotNull();

        //when
        Thread.sleep(60);

        //then
        assertThat(converter.getFoundClasses().getIfPresent(name)).isNull();
    }
}
