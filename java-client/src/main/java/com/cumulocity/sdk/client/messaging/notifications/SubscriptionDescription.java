package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.rest.representation.BaseResourceRepresentation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.svenson.JSONProperty;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
/**
 * <pre>
 *   {
 *     "source": { "id": "14800" },
 *     "context": "mo",
 *     "subscription": "test-subscription-5",
 *     "filter": "measurements",
 *     "fragments": "test-fragment",
 *     "isVolatile": false
 *   }
 * </pre>
 */
public class SubscriptionDescription extends BaseResourceRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Source source;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String context;

    @Getter(onMethod_ = @JSONProperty(value = "subscription", ignoreIfNull = true))
    private String name;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String filter;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String fragments;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private boolean isVolatile;
}