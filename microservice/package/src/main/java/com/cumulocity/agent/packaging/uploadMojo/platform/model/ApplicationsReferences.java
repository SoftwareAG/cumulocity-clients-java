package com.cumulocity.agent.packaging.uploadMojo.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.List;

@Wither
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationsReferences {
    private List<ApplicationReference> references;
}
