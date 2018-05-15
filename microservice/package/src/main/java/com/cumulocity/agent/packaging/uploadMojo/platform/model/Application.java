package com.cumulocity.agent.packaging.uploadMojo.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;

@Wither
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "name"})
public class Application {
    private String id;
    private String name;
    private String key;
    private String type = "MICROSERVICE";
}
