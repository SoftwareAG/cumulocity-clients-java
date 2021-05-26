/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cumulocity.sdk.client.messaging.notifications;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.JSONProperty;

/**
{"next":"http://cumulocity.default.svc.cluster.local/subscription/subscriptions?pageSize=2&source=21079&currentPage=2",
 "self":"http://cumulocity.default.svc.cluster.local/subscription/subscriptions?pageSize=2&source=21079&currentPage=1",
 "subscriptions":[
   {"filter":"measurements",
    "context":"mo",
    "self":"http://cumulocity.default.svc.cluster.local/reliablenotification/subscriptions/21080",
    "fragments":"test-fragment",
    "id":"21080",
    "subscription":"test-subscription-0",
    "source":{
      "name":"testMeasurementDevice",
      "self":"http://cumulocity.default.svc.cluster.local/inventory/managedObjects/21079",
      "id":"21079"},
    "isVolatile":false},
   {"filter":"measurements",
    "context":"mo",
    "self":"http://cumulocity.default.svc.cluster.local/reliablenotification/subscriptions/21082",
    "fragments":"test-fragment",
    "id":"21082",
    "subscription":"test-subscription-1",
    "source":{
      "name":"testMeasurementDevice",
      "self":"http://cumulocity.default.svc.cluster.local/inventory/managedObjects/21079",
      "id":"21079"},
    "isVolatile":false}],
 "statistics":{"currentPage":1,"pageSize":2}} 
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SubscriptionPage {
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private List<Subscription> subscriptions;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private SubscriptionPageStatistics statistics;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String next;
    
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String self;
}
