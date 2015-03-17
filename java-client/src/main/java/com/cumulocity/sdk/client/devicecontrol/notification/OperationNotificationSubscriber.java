package com.cumulocity.sdk.client.devicecontrol.notification;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.notification.Subscriber;
import com.cumulocity.sdk.client.notification.SubscriberBuilder;
import com.cumulocity.sdk.client.notification.Subscription;
import com.cumulocity.sdk.client.notification.SubscriptionListener;
import com.cumulocity.sdk.client.notification.SubscriptionNameResolver;

public class OperationNotificationSubscriber implements Subscriber<GId, OperationRepresentation> {
    public static final String DEVICE_CONTROL_NOTIFICATIONS_URL = "devicecontrol/notifications";

    private final Subscriber<GId, OperationRepresentation> subscriber;

    public OperationNotificationSubscriber(PlatformParameters parameters) {
        subscriber = createSubscriber(parameters);
    }

    private Subscriber<GId, OperationRepresentation> createSubscriber(PlatformParameters parameters) {
        // @formatter:off
        return SubscriberBuilder.<GId,OperationRepresentation>anSubscriber()
                    .withEndpoint(DEVICE_CONTROL_NOTIFICATIONS_URL)
                    .withSubscriptionNameResolver(new AgentDeviceIdAsSubscriptonName())
                    .withParameters(parameters)
                    .withDataType(OperationRepresentation.class)
                    .withMessageDeliveryAcknowlage(true)
                    .build();
        // @formatter:on
    }

    public Subscription<GId> subscribe(final GId agentId, final SubscriptionListener<GId, OperationRepresentation> handler)
            throws SDKException {
        return subscriber.subscribe(agentId, handler);
    }

    public void disconnect() {
        subscriber.disconnect();
    }

    private static final class AgentDeviceIdAsSubscriptonName implements SubscriptionNameResolver<GId> {
        @Override
        public String apply(GId id) {
            return "/" + id.getValue();
        }
    }
}
