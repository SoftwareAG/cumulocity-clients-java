package com.cumulocity.rest.representation.reliable.notification;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.JSONProperty;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class NotificationTokenRequestRepresentation extends AbstractExtensibleRepresentation {

    public NotificationTokenRequestRepresentation(String subscriber, String subscription, long expiresInMinutes, boolean shared){
        this(subscriber, subscription, null, true, expiresInMinutes, shared, false);
    }

    public NotificationTokenRequestRepresentation(final String subscriber, final String subscription, final long expiresInMinutes, final boolean shared, final boolean nonPersistent) {
        this(subscriber, subscription, null, true, expiresInMinutes, shared, nonPersistent);
    }

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String subscriber;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String subscription;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String type;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private boolean signed = true;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private long expiresInMinutes;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private boolean shared;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private boolean nonPersistent;

    // support for isReadOnly & isWriteOnly to be added later
}
