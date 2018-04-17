package com.cumulocity.agent.packaging.uploadMojo.platform.client.impl;

import lombok.experimental.UtilityClass;

import static org.apache.maven.shared.utils.StringUtils.stripEnd;
import static org.apache.maven.shared.utils.StringUtils.stripStart;

@UtilityClass
public class UrlUtils {
    public static String concat(String baseUrl, String path) {
        return stripEnd(baseUrl, "/") + "/" + stripStart(path, "/");
    }

    public static String ensureHttpSchema(String baseUrl) {
        if (baseUrl.startsWith("http://") || baseUrl.startsWith("https://")) {
            return baseUrl;
        }
        return "http://" + stripStart(baseUrl, "/");
    }
}
