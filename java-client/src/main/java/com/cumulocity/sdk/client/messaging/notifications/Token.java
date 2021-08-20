package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.BaseResourceRepresentation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.JSONProperty;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Token extends BaseResourceRepresentation {

    private String tokenString;

    @JSONProperty(value = "token", ignoreIfNull = true)
    public String getTokenString() {
        return this.tokenString;
    }
}