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
public class Token extends BaseResourceRepresentation {

    @Getter(onMethod_ = @JSONProperty(value = "token", ignoreIfNull = true))
    private String tokenString;

}