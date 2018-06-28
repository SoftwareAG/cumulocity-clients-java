package com.cumulocity.agent.packaging.uploadMojo.platform.model;

import lombok.*;
import lombok.experimental.Wither;

@Wither
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
public class Tenant {
    private String id;
    private ApplicationsReferences applications;

    public boolean isSubscribed(Application target) {
        for (final ApplicationReference application : applications.getReferences()) {
            if (application.getApplication().getId().equals(target.getId())) {
                return true;
            }
        }
        return false;
    }
}