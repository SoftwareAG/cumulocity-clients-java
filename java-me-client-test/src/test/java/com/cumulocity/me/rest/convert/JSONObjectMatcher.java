package com.cumulocity.me.rest.convert;

import java.util.Enumeration;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.cumulocity.me.rest.json.JSONArray;
import com.cumulocity.me.rest.json.JSONObject;

public class JSONObjectMatcher extends TypeSafeMatcher<JSONObject> {

    public static final JSONObjectMatcher aJSONObjectLike(JSONObject expected) {
        return new JSONObjectMatcher(expected);
    }
    
    private final JSONObject expected;

    private JSONObjectMatcher(JSONObject expected) {
        this.expected = expected;
    }
    
    @Override
    public void describeTo(Description description) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean matchesSafely(JSONObject item) {
        // FIXME recursive matching does not work
        Enumeration<?> keys = expected.keys();
        
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (item.has(key)) {
                if (expected.get(key) instanceof JSONObject) {
                    matchesSafely(item.getJSONObject(key));
                }
                if (expected.get(key) instanceof JSONArray) {
                    matchesSafely(item.getJSONArray(key));
                }
                else {
                    String value = expected.getString(key);
                    
                    if (! item.getString(key).equals(value)) {
                        return false;
                    }
                }
            }
            else {
                return false;
            }
        }
        return true;
    }
    
    private boolean matchesSafely(JSONArray item) {
        for(int i = 0; i < item.length(); i++) {
            if (item.get(i) instanceof JSONObject) {
                matchesSafely(item.getJSONObject(i));
            }
            if (item.get(i) instanceof JSONArray) {
                matchesSafely(item.getJSONArray(i));
            }
            else {
                String value = item.getString(i);
                
                if (! item.getString(i).equals(value)) {
                    return false;
                }
            }
        }
        return true;
    }
}