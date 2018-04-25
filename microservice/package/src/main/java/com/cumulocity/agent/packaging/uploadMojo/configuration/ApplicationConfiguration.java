package com.cumulocity.agent.packaging.uploadMojo.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.BooleanUtils;
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

    public boolean isPresent() {
        if (BooleanUtils.isTrue(skip)) {
            return false;
        }
        if (BooleanUtils.isFalse(skip)) {
            return true;
        }
        return !isEmpty();
    }

    private boolean isEmpty() {
        return StringUtils.isEmpty(groupId)
                && StringUtils.isEmpty(artifactId)
                && StringUtils.isEmpty(name)
                && delete == null
                && create == null
                && isEmpty(subscriptions);
    }

    private boolean isEmpty(List<String> subscriptions) {
        return subscriptions == null || subscriptions.isEmpty();
    }
}
