package com.cumulocity.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ClassNameMapperTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void getTypeHintShouldReturnClass() {
        //given
        String parsePathInfo = "jsonPathPart.com_cumulocity_model_idtype_GId";
        Class typeHint = String.class; // the unexpected type
        ClassNameMapper cnm = new ClassNameMapper();
        
        //when
        Class clazz = cnm.getTypeHint(null, parsePathInfo, typeHint);
        
        //then
        assertEquals(com.cumulocity.model.idtype.GId.class, clazz);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void getTypeHintShouldReturnOriginalTypeHintForUnknownClass() {
        //given
        String parsePathInfo = "jsonPathPart.some_unknown_class_GId";
        Class typeHint = String.class; // the expected type
        ClassNameMapper cnm = new ClassNameMapper();
        
        //when
        Class clazz = cnm.getTypeHint(null, parsePathInfo, typeHint);
        
        //then
        assertEquals(String.class, clazz);
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void getTypeHintShouldReturnOriginalTypeHintForMalformed() {
        //given
        String parsePathInfo = "jsonPathPart.com_cumulocity_model_idtype.Reference";
        Class typeHint = String.class; // the expected type
        ClassNameMapper cnm = new ClassNameMapper();
        
        //when
        Class clazz = cnm.getTypeHint(null, parsePathInfo, typeHint);
        
        //then
        assertEquals(String.class, clazz);
    }
}
