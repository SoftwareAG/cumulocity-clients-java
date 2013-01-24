package com.cumulocity.me.rest.convert.base;

import java.util.Date;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.HashSet;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.lang.Set;
import com.cumulocity.me.rest.convert.JsonConversionService;
import com.cumulocity.me.rest.convert.JsonConversionServiceAware;
import com.cumulocity.me.rest.convert.JsonConverter;
import com.cumulocity.me.rest.json.JSONArray;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationException;
import com.cumulocity.me.rest.validate.RepresentationValidator;
import com.cumulocity.me.util.DateUtils;

public abstract class BaseRepresentationConverter implements JsonConverter, JsonConversionServiceAware, RepresentationValidator {

    private JsonConversionService conversionService;

    public BaseRepresentationConverter() {
        super();
    }

    public void setJsonConversionService(JsonConversionService conversionService) {
        this.conversionService = conversionService;
    }

    protected JsonConversionService getConversionService() {
        return conversionService;
    }

    public boolean supports(Class representationType) {
        return supportedRepresentationType().equals(representationType);
    }

    protected abstract Class supportedRepresentationType();

    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        return true;
    }

    protected int getInt(JSONObject json, String propertyName) {
        return json.optInt(propertyName);
    }

    protected Integer getIntObj(JSONObject json, String propertyName) {
        if (json.has(propertyName)) {
            return new Integer(getInt(json, propertyName));
        } else {
            return null;
        }
    }

    protected long getLong(JSONObject json, String propertyName) {
        return json.optLong(propertyName);
    }

    protected Long getLongObj(JSONObject json, String propertyName) {
        if (json.has(propertyName)) {
            return new Long(getLong(json, propertyName));
        } else {
            return null;
        }
    }

    protected float getFloat(JSONObject json, String propertyName) {
        String string = json.optString(propertyName);
        if (string == null) {
            return 0.0f;
        } else {
            return Float.valueOf(string).floatValue();
        }
    }

    protected Float getFloatObj(JSONObject json, String propertyName) {
        if (json.has(propertyName)) {
            return new Float(getFloat(json, propertyName));
        } else {
            return null;
        }
    }

    protected double getDouble(JSONObject json, String propertyName) {
        String string = json.optString(propertyName);
        if (string == null) {
            return 0.0;
        } else {
            return Double.valueOf(string).doubleValue();
        }
    }

    protected Double getDoubleObj(JSONObject json, String propertyName) {
        if (json.has(propertyName)) {
            return new Double(getDouble(json, propertyName));
        } else {
            return null;
        }
    }

    protected String getString(JSONObject json, String propertyName) {
        return json.optString(propertyName);
    }

    protected Date getDate(JSONObject json, String propertyName) {
        String dateString = json.optString(propertyName);
        if (dateString == null || dateString.length() == 0) {
            return null;
        }
        return DateUtils.parse(dateString);
    }

    protected Object getObject(JSONObject json, String propertyName, Class propertyType) {
        return conversionService.fromJson(json.optJSONObject(propertyName), propertyType);
    }

    protected List getList(JSONObject json, String propertyName, Class propertyElementType) {
        JSONArray jsonArray = json.optJSONArray(propertyName);
        if (jsonArray == null) {
            return null;
        }
        List list = new ArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(conversionService.fromJson(jsonArray.getJSONObject(i), propertyElementType));
        }
        return list;
    }

    protected Set getSet(JSONObject json, String propertyName, Class propertyElementType) {
        JSONArray jsonArray = json.optJSONArray(propertyName);
        if (jsonArray == null) {
            return null;
        }
        Set set = new HashSet();
        for (int i = 0; i < jsonArray.length(); i++) {
            set.add(conversionService.fromJson(jsonArray.getJSONObject(i), propertyElementType));
        }
        return set;
    }

    protected void putInt(JSONObject json, String key, int value) {
        json.put(key, value);
    }

    protected void putIntObj(JSONObject json, String key, Integer value) {
        if (value != null) {
            putInt(json, key, value.intValue());
        }
    }

    protected void putLong(JSONObject json, String key, long value) {
        json.put(key, value);
    }

    protected void putLongObj(JSONObject json, String key, Long value) {
        if (value != null) {
            putLong(json, key, value.longValue());
        }
    }

    protected void putFloat(JSONObject json, String key, float value) {
        json.put(key, Float.toString(value));
    }

    protected void putFloatObj(JSONObject json, String key, Float value) {
        if (value != null) {
            putFloat(json, key, value.floatValue());
        }
    }

    protected void putDouble(JSONObject json, String key, double value) {
        json.put(key, Double.toString(value));
    }

    protected void putDoubleObj(JSONObject json, String key, Double value) {
        if (value != null) {
            putDouble(json, key, value.doubleValue());
        }
    }

    protected void putString(JSONObject json, String key, String value) {
        json.putOpt(key, value);
    }

    protected void putDate(JSONObject json, String key, Date value) {
        if (value != null) {
            String dateString = DateUtils.format(value);
            json.putOpt(key, dateString);
        }
    }

    protected void putObject(JSONObject json, String key, Object representation) {
        if (representation instanceof String) {
            String representationString = (String) representation;
            if (representationString.startsWith("{") && representationString.endsWith("}")) {
                json.putOpt(key, new JSONObject(representationString));
            } else {
                json.putOpt(key, representationString);
            }
        } else {
            json.putOpt(key, getConversionService().toJson(representation));
        }
    }

    protected void putList(JSONObject json, String key, List array) {
        JSONArray jsonarray = new JSONArray();
        Iterator iterator = array.iterator();
        while (iterator.hasNext()) {
            jsonarray.put(conversionService.toJson((CumulocityResourceRepresentation) iterator.next()));
        }
        json.putOpt(key, jsonarray);
    }

    protected void assertNull(String name, Object value) {
        if (value != null) {
            throw new RepresentationValidationException(name + " should be null!");
        }
    }

    protected void assertNotNull(String name, Object value) {
        if (value == null) {
            throw new RepresentationValidationException(name + " should not be null!");
        }
    }

    protected void putSet(JSONObject json, String key, Set set) {
        if (set != null) {
            JSONArray jsonarray = new JSONArray();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                jsonarray.put(conversionService.toJson((CumulocityResourceRepresentation) iterator.next()));
            }
            json.putOpt(key, jsonarray);
        }
    }
}
