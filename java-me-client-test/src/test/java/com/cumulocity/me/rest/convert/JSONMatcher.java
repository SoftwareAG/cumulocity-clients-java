package com.cumulocity.me.rest.convert;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.mockito.Mockito;

import com.cumulocity.me.rest.json.JSONObject;

public class JSONMatcher extends TypeSafeMatcher<JSONObject> {
    
    public static final JSONMatcher json(String jsonString) {
        return new JSONMatcher(jsonString);
    }
    
    public static final JSONObject jsonObject(String jsonString) {
        return Mockito.argThat(json(jsonString));
    }
    
    private final String jsonString;

    private JSONMatcher(String jsonString) {
        this.jsonString = jsonString;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("a json ").appendText(jsonString);
    }
    
    @Override
    public boolean matchesSafely(JSONObject json) {
        return json.toString().equals(jsonString);
    }
}