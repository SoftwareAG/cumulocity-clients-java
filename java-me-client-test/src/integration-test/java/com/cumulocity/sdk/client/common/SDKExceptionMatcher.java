package com.cumulocity.sdk.client.common;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.cumulocity.me.sdk.SDKException;

public class SDKExceptionMatcher extends TypeSafeMatcher<SDKException> {
    
    public static Matcher<SDKException> sdkException(int httpStatus) {
        return new SDKExceptionMatcher(httpStatus);
    }

    private final int httpStatus;

    private SDKExceptionMatcher(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public boolean matchesSafely(SDKException e) {
        return httpStatus == e.getHttpStatus();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an SDKException with status ").appendValue(httpStatus);
    }
}
