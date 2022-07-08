package com.cumulocity.model.authentication;

import com.google.common.base.Optional;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@UtilityClass
public class Jsons {

    public static Optional<String> readField(String field, String json) {
        try {
            return readValueAsString(field, json);
        } catch (PathNotFoundException e) {
            return Optional.absent();
        }
    }

    private static Optional<String> readValueAsString(String field, String json) {
        if (StringUtils.isEmpty(field)) {
            return Optional.absent();
        }
        Object value = JsonPath.read(json, "$." + field);
        return value == null ? Optional.absent() : Optional.of(value.toString());
    }
}
