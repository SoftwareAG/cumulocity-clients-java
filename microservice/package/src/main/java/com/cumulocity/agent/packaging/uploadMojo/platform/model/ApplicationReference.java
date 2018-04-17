package com.cumulocity.agent.packaging.uploadMojo.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Wither
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReference {
    private Application application;
}
