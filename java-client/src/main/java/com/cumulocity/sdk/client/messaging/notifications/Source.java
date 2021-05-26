package com.cumulocity.sdk.client.messaging.notifications;

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
public class Source {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String id;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String name;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String self;
}
