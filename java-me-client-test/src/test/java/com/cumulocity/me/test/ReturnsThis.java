package com.cumulocity.me.test;

import java.lang.reflect.Method;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsThis implements Answer<Object> {

    private static final ReturnsThis INSTANCE = new ReturnsThis();
    
    public static final ReturnsThis doReturnsThis() {
        return INSTANCE;
    }
    
    private ReturnsThis() {
    }
    
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object mock = invocation.getMock();
        if (method.getReturnType().isAssignableFrom(mock.getClass())) {
            return mock;
        } else {
            return null;
        }
    }
    
}