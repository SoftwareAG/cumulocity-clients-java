package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.BaseResourceRepresentation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.JSONProperty;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class TokenClaims extends BaseResourceRepresentation {

    @Getter(onMethod_ = @JSONProperty(value = "sub", ignoreIfNull = true))
    private String subscriber;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String topic;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String jti;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private long iat;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private long exp;

}