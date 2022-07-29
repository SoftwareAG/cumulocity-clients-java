package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import com.cumulocity.rest.representation.annotation.Null;
import lombok.*;
import org.svenson.JSONProperty;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "authConfigRepresentation")
public class AuthConfigRepresentation extends AbstractExtensibleRepresentation {

    @Null(operation = Command.CREATE)
    @NotNull(operation = Command.UPDATE)
    private String id;
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String type;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String providerName;
    private Boolean visibleOnLoginPage;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String userManagementSource;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String grantType;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private RequestRepresentation authorizationRequest;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private RequestRepresentation tokenRequest;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private RequestRepresentation refreshRequest;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private RequestRepresentation logoutRequest;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String buttonName;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String clientId;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String audience;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String issuer;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private UserIdConfigRepresentation userIdConfig;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private SignatureVerificationConfigRepresentation signatureVerificationConfig;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private OnNewUserRepresentation onNewUser;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String redirectToPlatform;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String template;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private AccessTokenToUserDataMappingsRepresentation accessTokenToUserDataMappings;
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private AuthenticationRestrictionsRepresentation authenticationRestrictions;
    @Valid
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private SessionConfigurationRepresentation sessionConfiguration;
}
