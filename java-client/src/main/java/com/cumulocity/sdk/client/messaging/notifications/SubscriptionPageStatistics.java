package com.cumulocity.sdk.client.messaging.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.JSONProperty;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SubscriptionPageStatistics {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private int currentPage;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private int pageSize;
}
