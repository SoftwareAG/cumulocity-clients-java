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
public class Subscription extends BaseResourceRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String id;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String filter;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String fragments;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String context;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String subscription;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private boolean isVolatile;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Source source;
}
