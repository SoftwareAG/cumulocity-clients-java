package com.cumulocity.rest.representation.reliable.notification;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.JSONProperty;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class NotificationTokenRequestRepresentation extends AbstractExtensibleRepresentation {

    @NotNull(operation = {Command.CREATE})
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String subscriber;

    @NotNull(operation = {Command.CREATE})
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String subscription;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private long expiresInMinutes;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private boolean shared;

    // support for isReadOnly, isWriteOnly, isVolatile to be added later

}
