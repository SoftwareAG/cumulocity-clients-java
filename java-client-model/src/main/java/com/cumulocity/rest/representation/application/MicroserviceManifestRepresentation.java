package com.cumulocity.rest.representation.application;

import com.cumulocity.rest.representation.application.microservice.*;
import lombok.*;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MicroserviceManifestRepresentation extends ManifestRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String apiVersion;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String contextPath;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String version;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private ProviderRepresentation provider;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String isolation;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String expose;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String scale;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String price;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private ProbeRepresentation livenessProbe;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private ProbeRepresentation readinessProbe;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private ResourcesRepresentation resources;

    @Singular
    @Getter(onMethod_ = {
            @JSONProperty(ignoreIfNull = true)
            , @JSONTypeHint(ConfigurationOptionRepresentation.class)
    })
    private List<ConfigurationOptionRepresentation> settings;

    @Singular
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private List<String> requiredRoles;

    @Singular
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private List<String> roles;

    @Singular
    @Getter(onMethod_ = {
            @JSONProperty(ignoreIfNull = true)
            , @JSONTypeHint(ExtensionRepresentation.class)
    })
    private List<ExtensionRepresentation> extensions;
}
