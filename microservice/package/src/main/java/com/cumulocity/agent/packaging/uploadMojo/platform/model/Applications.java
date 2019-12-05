package com.cumulocity.agent.packaging.uploadMojo.platform.model;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.List;

@Wither
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Applications {
    private List<Application> applications;

    public Optional<Application> first() {
        if (applications == null || applications.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(applications.get(0));
    }
}
