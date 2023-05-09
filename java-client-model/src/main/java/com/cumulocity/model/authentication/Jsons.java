package com.cumulocity.model.authentication;

import com.google.common.base.Optional;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.svenson.JSONParser;

import java.util.Map;

@Slf4j
@UtilityClass
public class Jsons {

    public static Optional<String> readField(String field, String json) {
        try {
            return readValueAsString(field, json);
        } catch (Exception e) {
            return Optional.absent();
        }
    }

    private static Optional<String> readValueAsString(String field, String json) {
        if (StringUtils.isEmpty(field)) {
            return Optional.absent();
        }
        Map<?, ?> data = JSONParser.defaultJSONParser().parse(Map.class, json);
        return Optional.fromNullable(data.get(field)).transform(String::valueOf);
    }
}
