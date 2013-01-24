package com.cumulocity.sdk.client.common;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.cumulocity.sdk.client.SDKException;

public class SdkExceptionMatcher {

    public static Matcher<Exception> sdkException(final int httpStatus) {
        return new TypeSafeMatcher<Exception>() {
            @Override
            public boolean matchesSafely(Exception e) {
                if (!(e instanceof SDKException)) {
                    return false;
                }
                SDKException sdkE = (SDKException) e;
                return httpStatus == sdkE.getHttpStatus();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("an SDKException with status ").appendValue(httpStatus);
            }
        };
    }

}
