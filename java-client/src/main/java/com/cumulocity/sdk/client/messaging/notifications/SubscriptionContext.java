package com.cumulocity.sdk.client.messaging.notifications;

public enum SubscriptionContext {
    MANAGED_OBJECT {
        public String toString() {
            return "mo";
        }
    },
    TENANT {
        public String toString() {
            return "tenant";
        }
    }
}
