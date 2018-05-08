package com.cumulocity.agent.packaging.uploadMojo.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfiguration {
    private String groupId;
    private String artifactId;
    private String name;
    private Boolean delete;
    private Boolean create;
    private Boolean skip;
    private List<String> subscriptions;

    public static String normalizeName(String name) {
        if (name == null) {
            return null;
        }

        final String lowerCase = name.toLowerCase();
        final String noDots = StringUtils.replace(lowerCase, ".", "");
        final String noColons = StringUtils.replace(noDots, ":", "");
        final String result = StringUtils.replace(noColons, "-", "");
        return shortenName(result);
    }

    public static String shortenName(String result) {
        final int maxLength = 20;
        if (result.length() > maxLength) {
            return result.substring(0, maxLength);
        }
        return result;
    }
}
